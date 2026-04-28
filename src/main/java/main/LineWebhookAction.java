package main;

import java.io.BufferedReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TeacherDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tool.Action;

public class LineWebhookAction extends Action {

    private static final String TOKEN = "kYOuIlmqzlnyZq7WVm/kbuMCepReOfIPofovMeXQcvuTL+ZFzLS4l2LbTE8Hb3pfo4fQ8AEgDUixK5ehYsdZKIBy2mI/sP09TV4rCXSSJ9SOBUp/EDU6I2Q6UmkIcW7H7Xb4YiDNxNselxX7zePKyAdB04t89/1O/w1cDnyilFU=";
    private static final String REPLY_URL = "https://api.line.me/v2/bot/message/reply";
    private static final String SYSTEM_URL =
        "https://score-by-c.duckdns.org/score/scoremanager/main/Menu.action";

    @Override
    public void execute(HttpServletRequest request,
                        HttpServletResponse response) throws Exception {

        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) sb.append(line);
        }
        String body = sb.toString();
        System.out.println("★Webhook受信: " + body);

        String replyToken = extractValue(body, "replyToken");
        String userId     = extractValue(body, "userId");

        // ===== followイベント(友達追加) =====
        if (body.contains("\"type\":\"follow\"")) {
            System.out.println("★友達追加: userId=" + userId);
            String msg = "得点管理システム Botです!\\n\\n"
                       + "あなたのLINE IDは:\\n"
                       + userId + "\\n\\n"
                       + "このIDを得点管理システムの\\n"
                       + "[LINE設定]画面に登録してください。";
            sendReply("{\"replyToken\":\"" + replyToken + "\","
                    + "\"messages\":[{\"type\":\"text\",\"text\":\"" + msg + "\"}]}");
        }

        // ===== messageイベント =====
        if (body.contains("\"type\":\"message\"") && body.contains("\"type\":\"text\"")) {
            System.out.println("★メッセージ受信: userId=" + userId);

            // 成績確認 → 3択メニュー
            if (body.contains("成績確認")) {
                sendReply(buildMenuJson(replyToken));
            }

            // 学生別成績 → 学生カルーセル
            else if (body.contains("学生別成績")) {
                TeacherDao tdao = new TeacherDao();
                Teacher teacher = tdao.getByLineUserId(userId);
                if (teacher != null) {
                    sendReply(buildStudentCarouselJson(replyToken, teacher));
                } else {
                    sendReply(buildTextReply(replyToken,
                        "LINE IDが登録されていません。システムからLINE設定を行ってください。"));
                }
            }

            // 科目別成績 → 科目選択ボタン
            else if (body.contains("科目別成績")) {
                TeacherDao tdao = new TeacherDao();
                Teacher teacher = tdao.getByLineUserId(userId);
                if (teacher != null) {
                    sendReply(buildSubjectMenuJson(replyToken, teacher));
                } else {
                    sendReply(buildTextReply(replyToken, "LINE IDが登録されていません。"));
                }
            }
        }

        // ===== postbackイベント(ボタン選択) =====
        if (body.contains("\"type\":\"postback\"")) {
            String data = extractPostbackData(body);
            System.out.println("★postback data: " + data);

            TeacherDao tdao = new TeacherDao();
            Teacher teacher = tdao.getByLineUserId(userId);

            // 学生を選択した場合
            if (data.startsWith("student=") && teacher != null) {
                String studentNo = data.substring("student=".length());
                sendReply(buildStudentGradesJson(replyToken, studentNo, teacher));
            }

            // 科目を選択した場合
            else if (data.startsWith("subject=") && teacher != null) {
                String subjectCd = data.substring("subject=".length());
                sendReply(buildSubjectGradesJson(replyToken, subjectCd, teacher));
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("OK");
    }

    // ===== 3択メニュー =====
    private String buildMenuJson(String replyToken) {
        return "{\"replyToken\":\"" + replyToken + "\","
             + "\"messages\":[{"
             + "\"type\":\"template\",\"altText\":\"成績確認メニュー\","
             + "\"template\":{\"type\":\"buttons\","
             + "\"text\":\"どの情報を確認しますか？\","
             + "\"actions\":["
             + "{\"type\":\"message\",\"label\":\"学生別成績\",\"text\":\"学生別成績\"},"
             + "{\"type\":\"message\",\"label\":\"科目別成績\",\"text\":\"科目別成績\"},"
             + "{\"type\":\"uri\",\"label\":\"システムを開く\",\"uri\":\"" + SYSTEM_URL + "\"}"
             + "]}}]}";
    }

    // ===== 学生カルーセル(10人ずつ) =====
    private String buildStudentCarouselJson(String replyToken,
                                             Teacher teacher) throws Exception {
        StudentDao sDao = new StudentDao();
        List<Student> students = sDao.filter(teacher.getSchool(), 2023, "131", true);

        if (students.isEmpty()) {
            return buildTextReply(replyToken, "学生データが見つかりません。");
        }

        StringBuilder bubbles = new StringBuilder();
        int total    = students.size();
        int pageSize = 10;
        int pages    = (int) Math.ceil((double) total / pageSize);

        for (int page = 0; page < pages; page++) {
            int from = page * pageSize;
            int to   = Math.min(from + pageSize, total);
            List<Student> group = students.subList(from, to);

            if (bubbles.length() > 0) bubbles.append(",");

            bubbles.append("{\"type\":\"bubble\",\"size\":\"kilo\",");
            bubbles.append("\"header\":{\"type\":\"box\",\"layout\":\"vertical\",");
            bubbles.append("\"backgroundColor\":\"#1F4E78\",\"contents\":[");
            bubbles.append("{\"type\":\"text\",\"text\":\"学生一覧 ")
                   .append(from + 1).append("〜").append(to).append("人目\",")
                   .append("\"color\":\"#FFFFFF\",\"size\":\"sm\",\"weight\":\"bold\"}]},");

            bubbles.append("\"body\":{\"type\":\"box\",\"layout\":\"vertical\",");
            bubbles.append("\"spacing\":\"xs\",\"contents\":[");

            for (int i = 0; i < group.size(); i++) {
                Student st = group.get(i);
                if (i > 0) bubbles.append(",");
                bubbles.append("{\"type\":\"button\",\"style\":\"link\",\"height\":\"sm\",");
                bubbles.append("\"action\":{\"type\":\"postback\",");
                bubbles.append("\"label\":\"").append(st.getName()).append("\",");
                bubbles.append("\"data\":\"student=").append(st.getNo()).append("\"}}");
            }
            bubbles.append("]}}");
        }

        return "{\"replyToken\":\"" + replyToken + "\","
             + "\"messages\":[{\"type\":\"flex\",\"altText\":\"学生一覧\","
             + "\"contents\":{\"type\":\"carousel\",\"contents\":["
             + bubbles.toString()
             + "]}}]}";
    }

    // ===== 学生の成績を Flex Message + 偏差値で返信 ★修正 =====
    private String buildStudentGradesJson(String replyToken,
                                           String studentNo,
                                           Teacher teacher) throws Exception {
        StudentDao sDao = new StudentDao();
        Student student = sDao.get(studentNo);
        if (student == null) {
            return buildTextReply(replyToken, "学生が見つかりません: " + studentNo);
        }
        student.setSchool(teacher.getSchool());

        TestDao tDao = new TestDao();
        List<Test> tests = tDao.filter(student);

        if (tests.isEmpty()) {
            return buildTextReply(replyToken,
                student.getName() + " さんの成績データはまだ登録されていません。");
        }

        // 科目ごとに最新点数・回数・コードを集計
        SubjectDao subDao = new SubjectDao();
        Map<String, Integer>       latestMap = new LinkedHashMap<>();
        Map<String, String>        cdMap     = new LinkedHashMap<>();
        Map<String, List<Integer>> trendMap  = new LinkedHashMap<>();

        for (Test t : tests) {
            String name = t.getSubject().getName();
            cdMap.put(name, t.getSubject().getCd());
            if (!trendMap.containsKey(name)) trendMap.put(name, new ArrayList<>());
            trendMap.get(name).add(t.getPoint());
            latestMap.put(name, t.getPoint());
        }

        // Flex Messageのボディ行を組み立て
        StringBuilder rows = new StringBuilder();
        for (String subName : latestMap.keySet()) {
            int myPt  = latestMap.get(subName);
            int latNo = trendMap.get(subName).size();

            Subject subject = subDao.get(cdMap.get(subName), teacher.getSchool());
            List<Integer> allPts = tDao.getClassPoints(
                subject, latNo, student.getClassNum(), teacher.getSchool());

            String devStr = allPts.size() > 1
                ? String.valueOf(calcDeviation(myPt, allPts))
                : "-";

            if (rows.length() > 0) rows.append(",");
            rows.append("{\"type\":\"box\",\"layout\":\"horizontal\",\"contents\":[")
                .append("{\"type\":\"text\",\"text\":\"").append(subName)
                .append("\",\"flex\":3,\"size\":\"sm\"},")
                .append("{\"type\":\"text\",\"text\":\"").append(myPt)
                .append("点\",\"flex\":2,\"align\":\"end\",\"size\":\"sm\"},")
                .append("{\"type\":\"text\",\"text\":\"").append(devStr)
                .append("\",\"flex\":3,\"align\":\"end\",\"size\":\"sm\",\"color\":\"#888888\"}")
                .append("]}");
        }

        String title = student.getName() + " さんの成績";
        String info  = student.getClassNum() + "クラス / " + student.getNo();

        return "{\"replyToken\":\"" + replyToken + "\","
             + "\"messages\":[{\"type\":\"flex\",\"altText\":\"" + title + "\","
             + "\"contents\":{\"type\":\"bubble\","
             + "\"header\":{\"type\":\"box\",\"layout\":\"vertical\","
             + "\"backgroundColor\":\"#1F4E78\",\"contents\":["
             + "{\"type\":\"text\",\"text\":\"" + title + "\","
             + "\"color\":\"#FFFFFF\",\"weight\":\"bold\"},"
             + "{\"type\":\"text\",\"text\":\"" + info + "\","
             + "\"color\":\"#DDDDDD\",\"size\":\"sm\"}]},"
             + "\"body\":{\"type\":\"box\",\"layout\":\"vertical\",\"contents\":["
             + "{\"type\":\"box\",\"layout\":\"horizontal\",\"contents\":["
             + "{\"type\":\"text\",\"text\":\"科目\",\"flex\":3,\"size\":\"xs\",\"color\":\"#888888\"},"
             + "{\"type\":\"text\",\"text\":\"点数\",\"flex\":2,\"align\":\"end\","
             + "\"size\":\"xs\",\"color\":\"#888888\"},"
             + "{\"type\":\"text\",\"text\":\"偏差値\",\"flex\":3,\"align\":\"end\","
             + "\"size\":\"xs\",\"color\":\"#888888\"}"
             + "]},"
             + "{\"type\":\"separator\",\"margin\":\"sm\"},"
             + rows.toString()
             + "]}}}]}";
    }

    // ===== 科目選択ボタン =====
    private String buildSubjectMenuJson(String replyToken,
                                         Teacher teacher) throws Exception {
        SubjectDao subDao = new SubjectDao();
        List<Subject> subjects = subDao.filter(teacher.getSchool());

        if (subjects.isEmpty()) {
            return buildTextReply(replyToken, "科目データが見つかりません。");
        }

        StringBuilder actions = new StringBuilder();
        int limit = Math.min(subjects.size(), 4);
        for (int i = 0; i < limit; i++) {
            Subject sub = subjects.get(i);
            if (i > 0) actions.append(",");
            actions.append("{\"type\":\"postback\",")
                   .append("\"label\":\"").append(sub.getName()).append("\",")
                   .append("\"data\":\"subject=").append(sub.getCd()).append("\"}");
        }

        return "{\"replyToken\":\"" + replyToken + "\","
             + "\"messages\":[{\"type\":\"template\",\"altText\":\"科目選択\","
             + "\"template\":{\"type\":\"buttons\","
             + "\"text\":\"科目を選択してください\","
             + "\"actions\":[" + actions + "]}}]}";
    }

    // ===== 科目別成績サマリーを Flex Message で返信 ★修正 =====
    private String buildSubjectGradesJson(String replyToken,
                                           String subjectCd,
                                           Teacher teacher) throws Exception {
        SubjectDao subDao = new SubjectDao();
        Subject subject = subDao.get(subjectCd, teacher.getSchool());
        if (subject == null) {
            return buildTextReply(replyToken, "科目が見つかりません。");
        }

        TestDao tDao = new TestDao();
        List<Test> tests = tDao.filter(2023, "131", subject, 2, teacher.getSchool());
        int no = 2;
        if (tests.isEmpty()) {
            tests = tDao.filter(2023, "131", subject, 1, teacher.getSchool());
            no = 1;
        }
        if (tests.isEmpty()) {
            return buildTextReply(replyToken, subject.getName() + " の成績データがありません。");
        }

        int sum = 0, max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;
        String maxName = "", minName = "";
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

        String title = subject.getName() + " " + no + "回目 成績サマリー";

        String[] labels = {"90-100", "80-89", "70-79", "60-69", "60未満"};
        StringBuilder distText = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            double pct = Math.round((double) dist[i] / n * 1000.0) / 10.0;
            distText.append(labels[i]).append(": ")
                    .append(dist[i]).append("人(").append(pct).append("%)\\n");
        }

        return "{\"replyToken\":\"" + replyToken + "\","
             + "\"messages\":[{\"type\":\"flex\",\"altText\":\"" + title + "\","
             + "\"contents\":{\"type\":\"bubble\","
             + "\"header\":{\"type\":\"box\",\"layout\":\"vertical\","
             + "\"backgroundColor\":\"#1F4E78\",\"contents\":["
             + "{\"type\":\"text\",\"text\":\"" + title + "\","
             + "\"color\":\"#FFFFFF\",\"weight\":\"bold\"},"
             + "{\"type\":\"text\",\"text\":\"131クラス\","
             + "\"color\":\"#DDDDDD\",\"size\":\"sm\"}]},"
             + "\"body\":{\"type\":\"box\",\"layout\":\"vertical\",\"contents\":["
             + row("受験者数", n + "人") + ","
             + row("平均点",   avg + "点") + ","
             + row("最高点",   max + "点(" + maxName + ")") + ","
             + row("最低点",   min + "点(" + minName + ")") + ","
             + row("標準偏差", String.valueOf(stdDev)) + ","
             + "{\"type\":\"separator\",\"margin\":\"md\"},"
             + "{\"type\":\"text\",\"text\":\"点数分布\","
             + "\"weight\":\"bold\",\"margin\":\"md\"},"
             + "{\"type\":\"text\",\"text\":\"" + distText.toString()
             + "\",\"wrap\":true,\"size\":\"sm\"}"
             + "]}}}]}";
    }

    // ===== 共通: 横並び1行 =====
    private String row(String label, String value) {
        return "{\"type\":\"box\",\"layout\":\"horizontal\",\"contents\":["
             + "{\"type\":\"text\",\"text\":\"" + label + "\",\"flex\":3},"
             + "{\"type\":\"text\",\"text\":\"" + value + "\","
             + "\"flex\":4,\"align\":\"end\"}"
             + "]}";
    }

    // ===== 共通: テキスト返信 =====
    private String buildTextReply(String replyToken, String text) {
        return "{\"replyToken\":\"" + replyToken + "\","
             + "\"messages\":[{\"type\":\"text\",\"text\":\"" + text + "\"}]}";
    }

    // ===== 偏差値計算 ★追加 =====
    private double calcDeviation(int myPoint, List<Integer> all) {
        double sum = 0;
        for (int p : all) sum += p;
        double avg = sum / all.size();
        double v = 0;
        for (int p : all) v += Math.pow(p - avg, 2);
        double sd = Math.sqrt(v / all.size());
        if (sd == 0) return 50.0;
        return Math.round((50 + 10 * (myPoint - avg) / sd) * 10.0) / 10.0;
    }

    private String extractValue(String json, String key) {
        String search = "\"" + key + "\":\"";
        int start = json.indexOf(search);
        if (start == -1) return "";
        start += search.length();
        int end = json.indexOf("\"", start);
        return end == -1 ? "" : json.substring(start, end);
    }

    private String extractPostbackData(String json) {
        String search = "\"data\":\"";
        int start = json.indexOf(search);
        if (start == -1) return "";
        start += search.length();
        int end = json.indexOf("\"", start);
        return end == -1 ? "" : json.substring(start, end);
    }

    private void sendReply(String json) throws Exception {
        URL url = URI.create(REPLY_URL).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Authorization", "Bearer " + TOKEN);
        conn.setDoOutput(true);
        try (OutputStream os = conn.getOutputStream()) {
            os.write(json.getBytes(StandardCharsets.UTF_8));
        }
        System.out.println("★Reply API: " + conn.getResponseCode());
        conn.disconnect();
    }
}