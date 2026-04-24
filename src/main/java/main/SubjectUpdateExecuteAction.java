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

        // ===== 共通: ログインチェック =====
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (teacher == null) {
            response.sendRedirect(request.getContextPath()
                + "/scoremanager/login.jsp");
            return;
        }

        // ===== パラメータ取得 =====
        String cd = request.getParameter("cd");
        String name = request.getParameter("name");

        // ===== バリデーション =====
        Map<String, String> errors = new HashMap<>();

        if (name == null || name.isEmpty()) {
            errors.put("name", "科目名を入力してください");
        }

        // ★追加: 更新対象の科目がまだ存在するか確認
        SubjectDao sDao = new SubjectDao();
        Subject existing = sDao.get(cd, teacher.getSchool());
        if (existing == null) {
            errors.put("cd", "科目が存在していません");
        }

        // ===== エラー時は変更画面に戻す =====
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            // 入力値を保持して画面に戻す
            Subject subject = new Subject();
            subject.setCd(cd);
            subject.setName(name != null ? name : "");
            request.setAttribute("subject", subject);
            request.getRequestDispatcher("subject_update.jsp")
                   .forward(request, response);
            return;
        }

        // ===== 科目情報を更新 =====
        Subject subject = new Subject();
        subject.setCd(cd);
        subject.setName(name);
        subject.setSchool(teacher.getSchool());

        sDao.save(subject);

        // ===== 完了画面へ =====
        request.getRequestDispatcher("subject_update_done.jsp")
               .forward(request, response);
    }
}