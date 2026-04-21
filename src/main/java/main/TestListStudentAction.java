package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestListStudentAction extends Action {
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

        // ===== ★追加: プルダウン用データ(科目情報の検索フォーム用) =====
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

        // ===== 学生番号パラメータ取得 =====
        String studentNo = request.getParameter("no");

        if (studentNo != null && !studentNo.isEmpty()) {
            // 学生情報を取得
            StudentDao sDao = new StudentDao();
            Student student = sDao.get(studentNo);

            if (student != null) {
                // 成績リストを取得
                TestDao tDao = new TestDao();
                List<Test> tests = tDao.filter(student);

                request.setAttribute("student", student);
                request.setAttribute("tests", tests);

                System.out.println("★デバッグ: 学生=" + student.getName()
                    + " 成績件数=" + tests.size());
            } else {
                System.out.println("★デバッグ: 学生番号 " + studentNo + " が見つかりません");
                request.setAttribute("error_msg", "学生番号 " + studentNo + " は存在しません");
            }
        }

        // ★JSPへフォワード(リダイレクトではない)
        request.getRequestDispatcher("test_list_student.jsp")
               .forward(request, response);
    }
}