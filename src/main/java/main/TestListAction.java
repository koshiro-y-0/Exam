package main;
 
import java.util.List;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;
 
// 科目別 or 学生別のどちらで検索するかを選ぶ
public class TestListAction extends Action {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
 
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (teacher == null) {
            response.sendRedirect(request.getContextPath() + "/scoremanager/login.jsp");
            return;
        }
 
        // 科目一覧だけ取得してJSPに渡す
        SubjectDao subDao = new SubjectDao();
        List<Subject> subjects = subDao.filter(teacher.getSchool());
        request.setAttribute("subject_set", subjects);
 
        request.getRequestDispatcher("test_list.jsp").forward(request, response);
    }
}
