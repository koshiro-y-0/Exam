package main;

import java.util.HashMap;
import java.util.Map;

import bean.Student;
import bean.Teacher;
import dao.StudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class StudentUpdateExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // --- 1. ログインチェック ---
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (teacher == null) {
            response.sendRedirect(request.getContextPath() + "/scoremanager/login.jsp");
            return;
        }

        // --- 2. リクエストパラメータの取得 ---
        String no = request.getParameter("no");
        String name = request.getParameter("name");
        String entYearStr = request.getParameter("ent_year");
        String classNum = request.getParameter("class_num");
        String isAttendStr = request.getParameter("is_attend");

        // --- 3. 入力チェック(バリデーション) ---
        Map<String, String> errors = new HashMap<>();

        if (name == null || name.isEmpty()) {
            errors.put("name", "氏名を入力してください");
        }

        if (entYearStr == null || entYearStr.isEmpty() || entYearStr.equals("0")) {
            errors.put("ent_year", "入学年度を選択してください");
        }

        if (classNum == null || classNum.isEmpty() || classNum.equals("0")) {
            errors.put("class_num", "クラスを選択してください");
        }

        StudentDao sDao = new StudentDao();

        // --- 4. エラーがあれば変更画面に戻す ---
        if (!errors.isEmpty()) {
            // 元のデータを取得して変更画面を再表示
            request.setAttribute("errors", errors);

            // 入力中の値を保持(エラー後でも入力内容が消えないように)
            Student student = sDao.get(no);
            if (student != null) {
                student.setName(name);
                if (entYearStr != null && !entYearStr.isEmpty() && !entYearStr.equals("0")) {
                    student.setEntYear(Integer.parseInt(entYearStr));
                }
                student.setClassNum(classNum);
                student.setAttend(isAttendStr != null);
            }
            request.setAttribute("student", student);

            // プルダウン再生成のため StudentUpdateAction を呼び出すのではなく、
            // ここで直接プルダウンを再生成して画面に戻す
            new StudentUpdateActionHelper().setupPullDowns(request, teacher);

            request.getRequestDispatcher("student_update.jsp").forward(request, response);
            return;
        }

        // --- 5. 学生情報を更新 ---
        Student student = new Student();
        student.setNo(no);
        student.setName(name);
        student.setEntYear(Integer.parseInt(entYearStr));
        student.setClassNum(classNum);
        student.setAttend(isAttendStr != null); // チェックがあれば true
        student.setSchool(teacher.getSchool());

        boolean success = sDao.save(student);

        if (success) {
            // --- 6. 完了画面へ ---
            request.getRequestDispatcher("student_update_done.jsp").forward(request, response);
        } else {
            errors.put("system", "更新に失敗しました。");
            request.setAttribute("errors", errors);
            response.sendRedirect("StudentUpdate.action?no=" + no);
        }
    }
}