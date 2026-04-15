package main; 

  

import bean.Teacher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action; 

  

public class SubjectCreateAction extends Action { 

  

    @Override 

    public void execute(HttpServletRequest request, 

                        HttpServletResponse response) throws Exception { 

  

        // ログインチェック 

        HttpSession session = request.getSession(); 

        Teacher teacher = (Teacher) session.getAttribute("user"); 

        if (teacher == null) { 

            response.sendRedirect( 

                request.getContextPath() 

                + "/scoremanager/login.jsp"); 

            return; 

        } 

  

        // 登録画面(subject_create.jsp)へフォワード 

        request.getRequestDispatcher("subject_create.jsp") 

               .forward(request, response); 

    } 

} 