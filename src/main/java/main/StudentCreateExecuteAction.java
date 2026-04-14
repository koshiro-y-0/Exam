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

public class StudentCreateExecuteAction extends Action {

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

        // --- 3. 入力チェック(バリデーション) ---
        Map<String, String> errors = new HashMap<>();

        if (no == null || no.isEmpty()) {
            errors.put("no", "学生番号を入力してください");
        } else if (no.length() != 8) {
            errors.put("no", "学生番号は8桁で入力してください");
        }

        if (name == null || name.isEmpty()) {
            errors.put("name", "氏名を入力してください");
        }

        if (entYearStr == null || entYearStr.isEmpty() || entYearStr.equals("0")) {
            errors.put("ent_year", "入学年度を選択してください");
        }

        if (classNum == null || classNum.isEmpty() || classNum.equals("0")) {
            errors.put("class_num", "クラスを選択してください");
        }

        // 学生番号の重複チェック
        StudentDao sDao = new StudentDao();
        if (errors.isEmpty()) {
            Student existing = sDao.get(no);
            if (existing != null) {
                errors.put("no", "学生番号が重複しています");
            }
        }

        // --- 4. エラーがあれば登録画面に戻す ---
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            // 入力値を保持
            request.setAttribute("no", no);
            request.setAttribute("name", name);
            request.setAttribute("ent_year", entYearStr);
            request.setAttribute("class_num", classNum);

            // 入学年度とクラスのプルダウンを再生成して登録画面に戻す
            new StudentCreateAction().execute(request, response);
            return;
        }

        // --- 5. 学生情報を保存 ---
        Student student = new Student();
        student.setNo(no);
        student.setName(name);
        student.setEntYear(Integer.parseInt(entYearStr));
        student.setClassNum(classNum);
        student.setAttend(true); // 新規登録時は在学中
        student.setSchool(teacher.getSchool());

        boolean success = sDao.save(student);

        if (success) {
            // --- 6. 完了画面へ ---
            request.getRequestDispatcher("student_create_done.jsp").forward(request, response);
        } else {
            errors.put("system", "登録に失敗しました");
            request.setAttribute("errors", errors);
            new StudentCreateAction().execute(request, response);
        }
    }
}