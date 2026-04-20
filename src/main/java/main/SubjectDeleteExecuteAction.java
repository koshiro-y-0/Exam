package main; 

  

import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action; 

  

public class SubjectDeleteExecuteAction extends Action { 

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

   

         String cd = request.getParameter("cd"); 

         SubjectDao sDao = new SubjectDao(); 

   

         // 削除実行 

         sDao.delete(cd, teacher.getSchool()); 

   

         // 完了画面へ 

         request.getRequestDispatcher("subject_delete_done.jsp") 

                .forward(request, response); 

     } 

 } 

   