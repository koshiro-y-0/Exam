package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

        // ===== ★追加: プルダウン用データ =====
        int year = LocalDate.now().getYear();
        List<Integer> entYearSet = new ArrayList<>();
        for (int i = year - 10; i <= year + 1; i++) {
            entYearSet.add(i);
        }

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
                // ★回数は全回数を取得(回数指定なし)
                List<Test> tests = tDao.filter(entYear, f2, subject, 1, teacher.getSchool());

                request.setAttribute("tests", tests);
                request.setAttribute("subject", subject);

                System.out.println("★デバッグ: 科目=" + subject.getName()
                    + " 成績件数=" + tests.size());
            }

            request.setAttribute("f1", entYear);
            request.setAttribute("f2", f2);
            request.setAttribute("f3", f3);
        }

        request.getRequestDispatcher("test_list_subject.jsp")
               .forward(request, response);
    }
}