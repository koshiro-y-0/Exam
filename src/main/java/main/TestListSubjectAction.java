package main;
 
import java.util.List;

import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;
 
public class TestListSubjectAction extends Action {
    @Override
    public void execute(HttpServletRequest request,
                        HttpServletResponse response) throws Exception {
 
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (teacher == null) {
            response.sendRedirect(request.getContextPath()
                + "/scoremanager/login.jsp");
            return;
        }
 
        // パラメータを取得して数値変換
        int entYear = Integer.parseInt(request.getParameter("f1"));
        String classNum = request.getParameter("f2");
        String subjectCd = request.getParameter("f3");
        int no = Integer.parseInt(request.getParameter("f4"));
 
        // 科目を取得
        SubjectDao subDao = new SubjectDao();
        Subject subject = subDao.get(subjectCd, teacher.getSchool());
 
        // TestDao.filter で成績リスト取得
        TestDao tDao = new TestDao();
        List<Test> tests = tDao.filter(entYear, classNum, subject,
                                       no, teacher.getSchool());
 
        request.setAttribute("tests", tests);
        request.setAttribute("subject", subject);
        request.setAttribute("no", no);
 
        request.getRequestDispatcher("test_list_subject.jsp")
               .forward(request, response);
    }
}
