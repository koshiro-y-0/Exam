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

  

public class SubjectCreateExecuteAction extends Action { 

  

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

  

        // --- 1. パラメータ取得 --- 

        String cd = request.getParameter("cd"); 

        String name = request.getParameter("name"); 

  

        // --- 2. バリデーション --- 
        
        Map<String, String> errors = new HashMap<>(); 

        if (cd == null || cd.isEmpty()) { 

            errors.put("cd", "科目コードを入力してください"); 

        } 

        if (name == null || name.isEmpty()) { 

            errors.put("name", "科目名を入力してください"); 

        } 

  

        // 重複チェック 

        SubjectDao sDao = new SubjectDao(); 

        if (errors.get("cd") == null) { 

            Subject existing = sDao.get(cd, teacher.getSchool()); 

            if (existing != null) { 

                errors.put("cd", "この科目コードは既に登録されています"); 

            } 

        } 

  

        // --- 3. エラー時は画面に戻す --- 

        if (!errors.isEmpty()) { 

            request.setAttribute("errors", errors); 

            request.setAttribute("cd", cd); 

            request.setAttribute("name", name); 

            request.getRequestDispatcher("subject_create.jsp") 

                   .forward(request, response); 

            return; 

        } 

  

        // --- 4. Subject作成してDBに保存 --- 

        Subject subject = new Subject(); 

        subject.setCd(cd); 

        subject.setName(name); 

        subject.setSchool(teacher.getSchool()); // 学校情報をセット 

  

        sDao.save(subject); 

  

        // --- 5. 完了画面へ --- 

        request.getRequestDispatcher("subject_create_done.jsp") 

               .forward(request, response); 

    } 

} 