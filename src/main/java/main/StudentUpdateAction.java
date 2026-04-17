package main; 

import java.util.ArrayList;
import java.util.List;

import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class StudentUpdateAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // --- 1. ログインチェック ---
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (teacher == null) {
            response.sendRedirect(request.getContextPath() + "/scoremanager/login.jsp");
            return;
        }

        // --- 2. URLパラメータから学生番号を取得 ---
        // 例: StudentUpdate.action?no=20990001
        String no = request.getParameter("no");

        if (no == null || no.isEmpty()) {
            // 学生番号が指定されていない → 一覧に戻す
            response.sendRedirect("StudentList.action");
            return;
        }

        // --- 3. 学生情報を DB から取得 ---
        StudentDao sDao = new StudentDao();
        Student student = sDao.get(no);

        if (student == null) {
            // 該当する学生が存在しない → 一覧に戻す
            response.sendRedirect("StudentList.action");
            return;
        }

        // --- 4. プルダウン用のデータ生成 ---
        // ※入学年度は変更不可（固定値表示）のためリスト生成は不要
        
        // クラス番号プルダウンのみ生成
        ClassNumDao cNumDao = new ClassNumDao();
        List<String> classNumSet = new ArrayList<>();
        try {
            classNumSet = cNumDao.filter(teacher.getSchool());
        } catch (Exception e) {
            System.out.println("★クラス一覧の取得に失敗: " + e.getMessage());
        }

        // --- 5. リクエストにセット(JSPで初期値として表示するため) ---
        request.setAttribute("student", student);
        request.setAttribute("class_num_set", classNumSet);

        // --- 6. JSPへフォワード ---
        request.getRequestDispatcher("student_update.jsp").forward(request, response);
    }
}