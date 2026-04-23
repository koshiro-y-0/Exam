package main;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class TestDeleteExecuteAction extends Action {
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

        // ===== 削除対象のオブジェクトを取得 =====
        StudentDao sDao = new StudentDao();
        Student student = sDao.get(studentNo);

        SubjectDao subDao = new SubjectDao();
        Subject subject = subDao.get(subjectCd, teacher.getSchool());

        // ===== 削除実行 =====
        TestDao tDao = new TestDao();
        boolean success = tDao.delete(student, subject, teacher.getSchool(), no);

        if (success) {
            // 完了画面へ
            request.getRequestDispatcher("test_delete_done.jsp")
                   .forward(request, response);
        } else {
            // 削除失敗(既に削除済みなど)→ 成績登録画面に戻す
            response.sendRedirect("TestRegist.action");
        }
    }
}