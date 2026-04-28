package main;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class ClassRankingAction extends Action {

    @Override
    public void execute(HttpServletRequest request,
                        HttpServletResponse response) throws Exception {

        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (teacher == null) {
            response.sendRedirect(request.getContextPath() + "/scoremanager/login.jsp");
            return;
        }

        // ===== プルダウン用データ =====
        SubjectDao subDao = new SubjectDao();
        List<Subject> subjects = subDao.filter(teacher.getSchool());
        request.setAttribute("subject_set", subjects);

        // 入学年度リスト
        int year = java.time.LocalDate.now().getYear();
        List<Integer> entYearSet = new ArrayList<>();
        for (int i = year - 5; i <= year + 1; i++) entYearSet.add(i);
        request.setAttribute("ent_year_set", entYearSet);

        String step = request.getParameter("step");
        String f1   = request.getParameter("f1"); // 入学年度
        String f2   = request.getParameter("f2"); // クラス
        String f3   = request.getParameter("f3"); // 科目コード
        String f4   = request.getParameter("f4"); // 回数

        // ===== Step1: ランキング表示 =====
        if (f1 != null && !f1.equals("0")
                && f2 != null && !f2.isEmpty()
                && f3 != null && !f3.equals("0")
                && f4 != null && !f4.isEmpty()) {

            int entYear = Integer.parseInt(f1);
            int no      = Integer.parseInt(f4);
            Subject subject = subDao.get(f3, teacher.getSchool());

            TestDao tDao = new TestDao();
            List<Test> tests = tDao.filter(entYear, f2, subject, no, teacher.getSchool());
            tests.sort((a, b) -> b.getPoint() - a.getPoint());

            List<String>  nos    = new ArrayList<>();
            List<String>  names  = new ArrayList<>();
            List<Integer> scores = new ArrayList<>();
            for (Test t : tests) {
                nos.add(t.getStudent().getNo());
                names.add(t.getStudent().getName());
                scores.add(t.getPoint());
            }

            List<Map<String, Object>> rankList = buildRankList(nos, names, scores);
            request.setAttribute("rank_list", rankList);
            request.setAttribute("subject",   subject);
            request.setAttribute("f1", f1);
            request.setAttribute("f2", f2);
            request.setAttribute("f3", f3);
            request.setAttribute("f4", f4);
        }

        // ===== Step2: 重み付きポイント集計 =====
        if ("step2".equals(step)) {

            String[] studentNos  = request.getParameterValues("student_nos");
            String[] rankPoints  = request.getParameterValues("rank_points");

            // 科目重み（subject_cd → weight）
            Map<String, Double> weightMap = new LinkedHashMap<>();
            SubjectDao sd = new SubjectDao();
            List<Subject> subList = sd.filter(teacher.getSchool());
            for (Subject s : subList) {
                String wParam = request.getParameter("w_" + s.getCd());
                double w = 1.0;
                try { if (wParam != null && !wParam.isEmpty()) w = Double.parseDouble(wParam); }
                catch (NumberFormatException ignored) {}
                weightMap.put(s.getCd(), w);
            }

            double weightSum = weightMap.values().stream().mapToDouble(Double::doubleValue).sum();
            if (weightSum == 0) weightSum = 1;

            Map<String, Double> totalPointMap = new LinkedHashMap<>();
            Map<String, String> nameMap       = new LinkedHashMap<>();
            String[] hiddenNames = request.getParameterValues("student_names");

            if (studentNos != null && rankPoints != null) {
                for (int i = 0; i < studentNos.length; i++) {
                    String no  = studentNos[i];
                    double pt  = Double.parseDouble(rankPoints[i]);
                    totalPointMap.put(no, Math.round(pt * 10.0) / 10.0);
                    if (hiddenNames != null && i < hiddenNames.length) {
                        nameMap.put(no, hiddenNames[i]);
                    }
                }
            }

            List<Map<String, Object>> weightedList = new ArrayList<>();
            totalPointMap.entrySet().stream()
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .forEach(e -> {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("student_no",   e.getKey());
                    row.put("student_name", nameMap.getOrDefault(e.getKey(), e.getKey()));
                    row.put("total_point",  e.getValue());
                    weightedList.add(row);
                });

            session.setAttribute("weighted_list", weightedList);

            request.setAttribute("weighted_list", weightedList);
            request.setAttribute("weight_map",    weightMap);
            request.setAttribute("subject_set",   subList);
            request.setAttribute("student_nos",   studentNos);
            request.setAttribute("rank_points",   rankPoints);
            request.setAttribute("student_names", hiddenNames);
            request.setAttribute("f1", f1);
            request.setAttribute("f2", f2);
            request.setAttribute("f3", f3);
            request.setAttribute("f4", f4);
            request.setAttribute("step", "step2");
        }

        // ===== Step3: グループ編成 =====
        if ("step3".equals(step)) {

            @SuppressWarnings("unchecked")
            List<Map<String, Object>> weightedList =
                (List<Map<String, Object>>) session.getAttribute("weighted_list");

            if (weightedList == null || weightedList.isEmpty()) {
                String[] studentNos = request.getParameterValues("student_nos");
                String[] rankPoints = request.getParameterValues("rank_points");
                String[] names      = request.getParameterValues("student_names");
                weightedList = new ArrayList<>();
                if (studentNos != null && rankPoints != null) {
                    for (int i = 0; i < studentNos.length; i++) {
                        Map<String, Object> row = new LinkedHashMap<>();
                        row.put("student_no",   studentNos[i]);
                        row.put("student_name", names != null && i < names.length ? names[i] : studentNos[i]);
                        row.put("total_point",  Double.parseDouble(rankPoints[i]));
                        weightedList.add(row);
                    }
                }
            }

            String groupCountStr = request.getParameter("group_count");
            String groupSizeStr  = request.getParameter("group_size");

            int groupCount;
            if (groupCountStr != null && !groupCountStr.isEmpty()) {
                groupCount = Integer.parseInt(groupCountStr);
            } else if (groupSizeStr != null && !groupSizeStr.isEmpty()) {
                groupCount = (int) Math.ceil((double) weightedList.size() / Integer.parseInt(groupSizeStr));
            } else {
                groupCount = 4;
            }
            if (groupCount < 1) groupCount = 1;
            if (groupCount > weightedList.size()) groupCount = weightedList.size();

            // スネーク順ドラフト方式
            List<List<Map<String, Object>>> rawGroups = new ArrayList<>();
            for (int g = 0; g < groupCount; g++) rawGroups.add(new ArrayList<>());

            boolean forward = true;
            int g = 0;
            for (Map<String, Object> student : weightedList) {
                rawGroups.get(g).add(student);
                if (forward) {
                    g++;
                    if (g == groupCount) { g = groupCount - 1; forward = false; }
                } else {
                    g--;
                    if (g < 0) { g = 0; forward = true; }
                }
            }

            List<Map<String, Object>> groupResult = new ArrayList<>();
            for (int gi = 0; gi < rawGroups.size(); gi++) {
                List<Map<String, Object>> members = rawGroups.get(gi);
                double total = members.stream()
                    .mapToDouble(m -> ((Number) m.get("total_point")).doubleValue())
                    .sum();
                Map<String, Object> grp = new LinkedHashMap<>();
                grp.put("name",    "グループ" + (char)('A' + gi));
                grp.put("members", members);
                grp.put("total",   Math.round(total * 10.0) / 10.0);
                groupResult.add(grp);
            }

            request.setAttribute("group_result",   groupResult);
            request.setAttribute("group_count",    groupCount);
            request.setAttribute("group_size_str", groupSizeStr);
            request.setAttribute("f1", f1);
            request.setAttribute("f2", f2);
            request.setAttribute("f3", f3);
            request.setAttribute("f4", f4);
            request.setAttribute("step", "step3");
        }

        request.getRequestDispatcher("class_ranking.jsp").forward(request, response);
    }

    // ================================================================
    // 平均ポイント方式でランキングリストを生成
    // ★ キーはキャメルケース(isTie)、型は Boolean.valueOf で明示
    // ================================================================
    private List<Map<String, Object>> buildRankList(
            List<String> nos, List<String> names, List<Integer> scores) {

        int n = scores.size();
        List<Map<String, Object>> result = new ArrayList<>();
        if (n == 0) return result;

        double[] pts = new double[n];
        if (n == 1) {
            pts[0] = 100.0;
        } else {
            for (int i = 0; i < n; i++) {
                pts[i] = 100.0 * (n - 1 - i) / (n - 1);
            }
        }

        int i = 0;
        while (i < n) {
            int j = i;
            while (j < n && scores.get(j).equals(scores.get(i))) j++;

            double ptSum = 0;
            for (int k = i; k < j; k++) ptSum += pts[k];
            double avgPt  = Math.round((ptSum / (j - i)) * 10.0) / 10.0;
            boolean isTie = (j - i) > 1;
            int rank      = i + 1;

            for (int k = i; k < j; k++) {
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("rank",         rank);
                row.put("isTie",        Boolean.valueOf(isTie)); // ★ キャメルケース & 型明示
                row.put("student_no",   nos.get(k));
                row.put("student_name", names.get(k));
                row.put("score",        scores.get(k));
                row.put("rank_point",   avgPt);
                result.add(row);
            }
            i = j;
        }
        return result;
    }
}