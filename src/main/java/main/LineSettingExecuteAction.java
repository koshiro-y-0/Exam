package main;
 
import bean.Teacher;
import dao.TeacherDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;
 
public class LineSettingExecuteAction extends Action {
    @Override
    public void execute(HttpServletRequest request,
                        HttpServletResponse response) throws Exception {
 
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (teacher == null) {
            response.sendRedirect(request.getContextPath() + "/scoremanager/login.jsp");
            return;
        }
 
        String lineUserId = request.getParameter("line_user_id");
 
        // DBに保存
        TeacherDao tDao = new TeacherDao();
        tDao.saveLineUserId(teacher.getId(), lineUserId, teacher.getSchool());
 
        // セッションのteacherも更新
        teacher.setLineUserId(lineUserId);
        session.setAttribute("user", teacher);
 
        System.out.println("★LINE ID 保存完了: " + lineUserId);
 
        // 設定画面に戻る
        response.sendRedirect("LineSetting.action");
    }
}
