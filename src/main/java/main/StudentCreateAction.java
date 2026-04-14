package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bean.Teacher;
import dao.ClassNumDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class StudentCreateAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // --- 1. ログインチェック ---
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (teacher == null) {
            response.sendRedirect(request.getContextPath() + "/scoremanager/login.jsp");
            return;
        }

        // --- 2. プルダウン用のデータ生成 ---
        // 入学年度プルダウン: 10年前から1年後まで
        int year = LocalDate.now().getYear();
        List<Integer> entYearSet = new ArrayList<>();
        for (int i = year - 10; i <= year + 1; i++) {
            entYearSet.add(i);
        }

        // クラス番号プルダウン
        ClassNumDao cNumDao = new ClassNumDao();
        List<String> classNumSet = new ArrayList<>();
        try {
            classNumSet = cNumDao.filter(teacher.getSchool());
        } catch (Exception e) {
            System.out.println("★クラス一覧の取得に失敗: " + e.getMessage());
        }

        // --- 3. リクエストにセット ---
        request.setAttribute("ent_year_set", entYearSet);
        request.setAttribute("class_num_set", classNumSet);

        // --- 4. JSPへフォワード ---
        request.getRequestDispatcher("student_create.jsp").forward(request, response);
    }
}