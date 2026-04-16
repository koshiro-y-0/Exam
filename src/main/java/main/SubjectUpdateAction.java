package main; 

  

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action; 

  

public class SubjectUpdateAction extends Action { 

    @Override 

    public void execute(HttpServletRequest request, 

                        HttpServletResponse response) throws Exception { 

  

        // ログインチェック(写経) 

        HttpSession session = request.getSession(); 

        Teacher teacher = (Teacher) session.getAttribute("user"); 

        if (teacher == null) { 

            response.sendRedirect(request.getContextPath() 

                + "/scoremanager/login.jsp"); 

            return; 

        } 

  

        // URL: SubjectUpdate.action?cd=A02 から cd を取得 

        String cd = request.getParameter("cd"); 

        if (cd == null || cd.isEmpty()) { 

            response.sendRedirect("SubjectList.action"); 

            return; 

        } 

  

        // 既存データを DB から取得 

        SubjectDao sDao = new SubjectDao(); 

        Subject subject = sDao.get(cd, teacher.getSchool()); 

  

        if (subject == null) { 

            response.sendRedirect("SubjectList.action"); 

            return; 

        } 

  

        // JSPへ(subject オブジェクトを持たせてフォーム表示) 

        request.setAttribute("subject", subject); 

        request.getRequestDispatcher("subject_update.jsp") 

               .forward(request, response); 

    } 

} 