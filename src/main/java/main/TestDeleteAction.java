package main;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestDeleteAction extends Action {
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

        // ===== パラメータ取得(複合キー4つ) =====
        String studentNo = request.getParameter("student_no");
        String subjectCd = request.getParameter("subject_cd");
        int no = Integer.parseInt(request.getParameter("no"));

        // ===== 削除対象の成績データを取得 =====
        StudentDao sDao = new StudentDao();
        Student student = sDao.get(studentNo);

        SubjectDao subDao = new SubjectDao();
        Subject subject = subDao.get(subjectCd, teacher.getSchool());

        TestDao tDao = new TestDao();
        Test test = tDao.get(student, subject, teacher.getSchool(), no);

        if (test == null) {
            // 対象データが見つからない場合は成績登録画面に戻す
            response.sendRedirect("TestRegist.action");
            return;
        }

        // ===== 確認画面に表示するデータをセット =====
        request.setAttribute("test", test);

        request.getRequestDispatcher("test_delete.jsp")
               .forward(request, response);
    }
}