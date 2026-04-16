 package main; 

  

import java.util.HashMap;
import java.util.Map;

import bean.Subject;
import bean.Teacher;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action; 

  

public class SubjectUpdateExecuteAction extends Action { 

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

  

        // パラメータ取得 

        String cd = request.getParameter("cd"); 

        String name = request.getParameter("name"); 

  

        // バリデーション 

        Map<String, String> errors = new HashMap<>(); 

        if (name == null || name.isEmpty()) { 

            errors.put("name", "科目名を入力してください"); 

        } 

  

        if (!errors.isEmpty()) { 

            response.sendRedirect("SubjectUpdate.action?cd=" + cd); 

            return; 

        } 

  

        // Subject作成して保存 

        Subject subject = new Subject(); 

        subject.setCd(cd); 

        subject.setName(name); 

        subject.setSchool(teacher.getSchool()); 

  

        SubjectDao sDao = new SubjectDao(); 

        sDao.save(subject); // 登録と同じメソッド!(存在すれば更新される) 

  

        // 完了画面へ 

        request.getRequestDispatcher("subject_update_done.jsp") 

               .forward(request, response); 

    } 

 
}
