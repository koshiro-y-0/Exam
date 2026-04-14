package tool;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "*.action" })
public class FrontController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 1. パスを取得  例: "/scoremanager/main/StudentList.action"
            String path = request.getServletPath().substring(1);

            // 2. URLの "scoremanager/" 部分を除去
            if (path.startsWith("scoremanager/")) {
                path = path.substring("scoremanager/".length());
            }

            // 3. ".action" → "Action"、"/" → "." に変換してクラス名を作る
            String name = path.replace(".action", "Action").replace('/', '.');

            // ★ 4. パッケージ指定が無い場合は "main." を付ける(LoginActionなど用)
            if (!name.contains(".")) {
                name = "main." + name;
            }

            System.out.println("★ servlet path -> " + request.getServletPath());
            System.out.println("★ class name   -> " + name);

            // 5. アクションクラスのインスタンスを生成
            Action action = (Action) Class.forName(name).getDeclaredConstructor().newInstance();

            // 6. 実行
            action.execute(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("javax.servlet.error.exception", e);
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}