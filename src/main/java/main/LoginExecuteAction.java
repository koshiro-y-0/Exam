package main;

import java.util.HashMap;
import java.util.Map;

import bean.Teacher;
import dao.TeacherDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class LoginExecuteAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // --- 1. リクエストパラメータ取得 ---
        String id = request.getParameter("id");
        String password = request.getParameter("password");

        // --- 2. DB で認証チェック ---
        TeacherDao dao = new TeacherDao();
        Teacher teacher = dao.login(id, password);

        if (teacher != null) {
            // --- 3. 認証成功: セッションに教員情報を保存 ---
            HttpSession session = request.getSession();
            session.setAttribute("user", teacher);

            // ★ 修正: ルート相対パスにして scoremanager の2重化を防ぐ
            response.sendRedirect(request.getContextPath() + "/scoremanager/main/menu.jsp");

        } else {
            // --- 4. 認証失敗: エラーメッセージを添えて login.jsp に戻す ---
            Map<String, String> errors = new HashMap<>();
            errors.put("login", "IDまたはパスワードが間違っています");

            request.setAttribute("errors", errors);
            request.setAttribute("id", id); // 入力したIDをフォームに残す

            request.getRequestDispatcher("/scoremanager/login.jsp").forward(request, response);
        }
    }
}