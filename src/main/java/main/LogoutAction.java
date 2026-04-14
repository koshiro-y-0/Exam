package main;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class LogoutAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // --- 1. セッションを破棄 ---
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // セッション内の全データを削除
        }

        // --- 2. ログアウト完了画面へフォワード ---
        request.getRequestDispatcher("/scoremanager/main/logout.jsp").forward(request, response);
    }
}