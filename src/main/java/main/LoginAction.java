package main;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tool.Action;

public class LoginAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // ログイン画面(login.jsp)を表示するだけ
        request.getRequestDispatcher("/scoremanager/login.jsp").forward(request, response);
    }
}