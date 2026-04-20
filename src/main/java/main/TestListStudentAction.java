package main; 

  

import java.util.List;

import bean.Student;
import bean.Teacher;
import bean.Test;
import dao.StudentDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action; 

  

public class TestListStudentAction extends Action { 

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

  

        // URL から学生番号を取得 

        String studentNo = request.getParameter("no"); 

  

        // 学生情報を取得 

        StudentDao sDao = new StudentDao(); 

        Student student = sDao.get(studentNo); 

  

        if (student == null) { 

            response.sendRedirect("TestList.action"); 

            return; 

        } 

  

        // 成績リストを取得(filterに学生オブジェクトを渡す版) 

        TestDao tDao = new TestDao(); 

        List<Test> tests = tDao.filter(student); 

  

        request.setAttribute("student", student); 
        
        request.setAttribute("tests", tests); 

        

        request.getRequestDispatcher("test_list_student.jsp") 

               .forward(request, response); 

    } 

} 