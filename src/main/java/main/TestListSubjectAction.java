package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestListSubjectAction extends Action {
    @Override
    public void execute(HttpServletRequest request,
                        HttpServletResponse response) throws Exception {

        // ===== 共通: ログインチェック =====
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (teacher == null) {
            response.sendRedirect(request.getContextPath()
                + "/scoremanager/login.jsp");
            return;
        }

        // ===== プルダウン用データ =====
        int year = LocalDate.now().getYear();
        List<Integer> entYearSet = new ArrayList<>();
        for (int i = year - 10; i <= year + 1; i++) entYearSet.add(i);

        ClassNumDao cDao = new ClassNumDao();
        List<String> classNumSet = new ArrayList<>();
        try { classNumSet = cDao.filter(teacher.getSchool()); }
        catch (Exception e) {}

        SubjectDao subDao = new SubjectDao();
        List<Subject> subjects = subDao.filter(teacher.getSchool());

        request.setAttribute("ent_year_set", entYearSet);
        request.setAttribute("class_num_set", classNumSet);
        request.setAttribute("subject_set", subjects);

        // ===== 絞込パラメータ取得 =====
        String f1 = request.getParameter("f1");
        String f2 = request.getParameter("f2");
        String f3 = request.getParameter("f3");

        if (f1 != null && !f1.isEmpty() && !f1.equals("0")
                && f2 != null && !f2.equals("0")
                && f3 != null && !f3.equals("0")) {

            int entYear = Integer.parseInt(f1);
            Subject subject = subDao.get(f3, teacher.getSchool());

            if (subject != null) {
                TestDao tDao = new TestDao();

                // 全回数のデータを取得
                List<Test> allTests = tDao.filterBySubject(entYear, f2, subject, teacher.getSchool());

                // ピボット形式に整形
                // studentNo → (no → point) のマップ
                Map<String, Map<Integer, Integer>> pointMap = new LinkedHashMap<>();
                // studentNo → Student オブジェクト
                Map<String, Student> studentMap = new LinkedHashMap<>();
                // 存在する回数の一覧(1回, 2回...)
                TreeSet<Integer> noSet = new TreeSet<>();

                for (Test t : allTests) {
                    String studentNo = t.getStudent().getNo();
                    studentMap.put(studentNo, t.getStudent());
                    noSet.add(t.getNo());

                    if (!pointMap.containsKey(studentNo)) {
                        pointMap.put(studentNo, new TreeMap<>());
                    }
                    pointMap.get(studentNo).put(t.getNo(), t.getPoint());
                }

                request.setAttribute("subject", subject);
                request.setAttribute("student_map", studentMap);
                request.setAttribute("point_map", pointMap);
                request.setAttribute("no_set", new ArrayList<>(noSet));

                System.out.println("★デバッグ: 科目=" + subject.getName()
                    + " 学生数=" + studentMap.size()
                    + " 回数=" + noSet);
            }

            request.setAttribute("f1", Integer.parseInt(f1));
            request.setAttribute("f2", f2);
            request.setAttribute("f3", f3);
        }

        request.getRequestDispatcher("test_list_subject.jsp")
               .forward(request, response);
    }
}