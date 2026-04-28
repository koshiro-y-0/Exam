package main;
 
import bean.Teacher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;
 
public class LineSettingAction extends Action {
    @Override
    public void execute(HttpServletRequest request,
                        HttpServletResponse response) throws Exception {
 
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (teacher == null) {
            response.sendRedirect(request.getContextPath() + "/scoremanager/login.jsp");
            return;
        }
 
        // 現在登録されているLINE IDをJSPに渡す
        request.setAttribute("teacher", teacher);
        request.getRequestDispatcher("line_setting.jsp").forward(request, response);
    }
}
