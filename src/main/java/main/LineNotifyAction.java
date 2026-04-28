package main;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class LineNotifyAction extends Action {

    private static final String TOKEN = "kYOuIlmqzlnyZq7WVm/kbuMCepReOfIPofovMeXQcvuTL+ZFzLS4l2LbTE8Hb3pfo4fQ8AEgDUixK5ehYsdZKIBy2mI/sP09TV4rCXSSJ9SOBUp/EDU6I2Q6UmkIcW7H7Xb4YiDNxNselxX7zePKyAdB04t89/1O/w1cDnyilFU=";
    private static final String LINE_URL = "https://api.line.me/v2/bot/message/push";

    @Override
    public void execute(HttpServletRequest request,
                        HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (teacher == null) {
            response.sendRedirect(request.getContextPath() + "/scoremanager/login.jsp");
            return;
        }

        // LINE ID チェック
        if (teacher.getLineUserId() == null || teacher.getLineUserId().isEmpty()) {
            response.sendRedirect("LineSetting.action");
            return;
        }

        // パラメータ取得
        String f1 = request.getParameter("f1");
        String f2 = request.getParameter("f2");
        String f3 = request.getParameter("f3");
        String f4 = request.getParameter("f4");

        int entYear = Integer.parseInt(f1);
        int no      = Integer.parseInt(f4);

        SubjectDao subDao = new SubjectDao();
        Subject subject = subDao.get(f3, teacher.getSchool());

        TestDao tDao = new TestDao();
        List<Test> tests = tDao.filter(entYear, f2, subject, no, teacher.getSchool());

        if (tests.isEmpty()) {
            String referer = request.getHeader("Referer");
            response.sendRedirect(referer != null ? referer : "TestRegist.action");
            return;
        }

        // ===== 成績集計 =====
        int sum = 0;
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        String maxName = "";
        String minName = "";
        int[] dist = new int[5];

        for (Test t : tests) {
            int pt = t.getPoint();
            sum += pt;
            if (pt > max) { max = pt; maxName = t.getStudent().getName(); }
            if (pt < min) { min = pt; minName = t.getStudent().getName(); }
            if      (pt >= 90) dist[0]++;
            else if (pt >= 80) dist[1]++;
            else if (pt >= 70) dist[2]++;
            else if (pt >= 60) dist[3]++;
            else               dist[4]++;
        }

        int n = tests.size();
        double avg    = Math.round((double) sum / n * 10.0) / 10.0;
        double varSum = 0;
        for (Test t : tests) varSum += Math.pow(t.getPoint() - avg, 2);
        double stdDev = Math.round(Math.sqrt(varSum / n) * 10.0) / 10.0;

        // ===== 送信 =====
        String json = buildJson(teacher, subject, no, f2,
                                n, avg, max, maxName, min, minName, stdDev, dist);
        sendMessage(json);

        String from = request.getParameter("from");
        if ("ranking".equals(from)) {
            response.sendRedirect("ClassRanking.action");
        } else {
            response.sendRedirect("TestRegist.action");
        }
    }

    private String buildJson(Teacher teacher, Subject subject, int no,
            String classNum, int n, double avg,
            int max, String maxName, int min, String minName,
            double stdDev, int[] dist) {

        // 点数分布テキスト
        String dist90 = "90-100: " + dist[0] + "(" + Math.round((double)dist[0]/n*1000)/10.0 + "%)";
        String dist80 = "80-89:  " + dist[1] + "(" + Math.round((double)dist[1]/n*1000)/10.0 + "%)";
        String dist70 = "70-79:  " + dist[2] + "(" + Math.round((double)dist[2]/n*1000)/10.0 + "%)";
        String dist60 = "60-69:  " + dist[3] + "(" + Math.round((double)dist[3]/n*1000)/10.0 + "%)";
        String dist59 = "60未満: " + dist[4] + "(" + Math.round((double)dist[4]/n*1000)/10.0 + "%)";
        String distAll = dist90 + "\\n" + dist80 + "\\n" + dist70 + "\\n" + dist60 + "\\n" + dist59;

        String title   = subject.getName() + " " + no + "回目 成績サマリー";
        String subInfo = classNum + "クラス " + entYear(teacher) + "年度";

        StringBuilder sb = new StringBuilder();
        sb.append("{\"to\":\"").append(teacher.getLineUserId()).append("\",");
        sb.append("\"messages\":[{\"type\":\"flex\",\"altText\":\"").append(title).append("\",");
        sb.append("\"contents\":{\"type\":\"bubble\",");

        // ヘッダー
        sb.append("\"header\":{\"type\":\"box\",\"layout\":\"vertical\",");
        sb.append("\"backgroundColor\":\"#1F4E78\",\"contents\":[");
        sb.append("{\"type\":\"text\",\"text\":\"").append(title).append("\",\"color\":\"#FFFFFF\",\"weight\":\"bold\"},");
        sb.append("{\"type\":\"text\",\"text\":\"").append(subInfo).append("\",\"color\":\"#DDDDDD\",\"size\":\"sm\"}]},");

        // ボディ
        sb.append("\"body\":{\"type\":\"box\",\"layout\":\"vertical\",\"contents\":[");
        sb.append(row("受験者数", n + "人")).append(",");
        sb.append(row("平均点",   avg + "点")).append(",");
        sb.append(row("最高点",   max + "点(" + maxName + ")")).append(",");
        sb.append(row("最低点",   min + "点(" + minName + ")")).append(",");
        sb.append(row("標準偏差", String.valueOf(stdDev))).append(",");
        sb.append("{\"type\":\"separator\",\"margin\":\"md\"},");
        sb.append("{\"type\":\"text\",\"text\":\"点数分布\",\"weight\":\"bold\",\"margin\":\"md\"},");
        sb.append("{\"type\":\"text\",\"text\":\"").append(distAll).append("\",\"wrap\":true,\"size\":\"sm\"}");
        sb.append("]}}");

        sb.append("}]}");
        return sb.toString();
    }

    /** 横並び1行を生成する */
    private String row(String label, String value) {
        return "{\"type\":\"box\",\"layout\":\"horizontal\",\"contents\":[" +
               "{\"type\":\"text\",\"text\":\"" + label + "\",\"flex\":3}," +
               "{\"type\":\"text\",\"text\":\"" + value + "\",\"flex\":4,\"align\":\"end\"}" +
               "]}";
    }

    private int entYear(Teacher teacher) {
        return java.time.LocalDate.now().getYear();
    }

    private void sendMessage(String json) throws Exception {
    	URL url = URI.create(LINE_URL).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Authorization", "Bearer " + TOKEN);
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
        System.out.println("★LINE Push API: " + conn.getResponseCode());
        conn.disconnect();
    }
}