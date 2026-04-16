package main; 

  

import java.util.ArrayList;
import java.util.List;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action; 

  

public class TestRegistExecuteAction extends Action { 

  

    @Override 

    public void execute(HttpServletRequest request, 

                        HttpServletResponse response) throws Exception { 

  

        // --- ログインチェック ---(共通なので写経) 

        HttpSession session = request.getSession(); 

        Teacher teacher = (Teacher) session.getAttribute("user"); 

        if (teacher == null) { 

            response.sendRedirect(request.getContextPath() 

                + "/scoremanager/login.jsp"); 

            return; 

        } 

  
        // パラメータ取得 

        String subjectCd = request.getParameter("f3"); 

        int no = Integer.parseInt(request.getParameter("f4")); 

        String classNum = request.getParameter("f2"); 
        
        String[] studentNos = request.getParameterValues("student_no"); 

        String[] points = request.getParameterValues("point"); 
        
     // 科目情報を取得 

        SubjectDao subDao = new SubjectDao(); 

        Subject subject = subDao.get(subjectCd, teacher.getSchool()); 
        
     // ★重要: 学生数分の Test を for 文で作ってリストに詰める 

        List<Test> testList = new ArrayList<>(); 

        for (int i = 0; i < studentNos.length; i++) { 

            // 点数が空の行はスキップ 

            if (points[i] == null || points[i].isEmpty()) continue; 
            
            
            Student student = new Student(); 

            student.setNo(studentNos[i]); 

            student.setSchool(teacher.getSchool()); 

  

            Test test = new Test(); 

            test.setStudent(student); 

            test.setSubject(subject); 

            test.setSchool(teacher.getSchool()); 

            test.setNo(no); 

            test.setPoint(Integer.parseInt(points[i])); 

            test.setClassNum(classNum); 

  

            testList.add(test); 

        } 
        
        TestDao tDao = new TestDao(); 

        tDao.save(testList); 

  

        // 完了画面へ 

        request.getRequestDispatcher("test_regist_done.jsp") 

               .forward(request, response); 

    } 

} 

