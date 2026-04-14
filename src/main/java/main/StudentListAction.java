package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bean.School;
import bean.Student;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;

public class StudentListAction extends Action {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws Exception {

        // --- 1. 変数宣言と初期化 ---
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");

        String entYearStr = "";     // 入力された入学年度
        String classNum = "";       // 入力されたクラス番号
        String isAttendStr = "";    // 入力された在学フラグ
        int entYear = 0;            // 入学年度
        boolean isAttend = false;   // 在学フラグ
        List<Student> students = null; // 学生リスト

        LocalDate todaysDate = LocalDate.now();
        int year = todaysDate.getYear();

        StudentDao sDao = new StudentDao();
        ClassNumDao cNumDao = new ClassNumDao();
        Map<String, String> errors = new HashMap<>();

        // --- 2. ログインチェック(スタブ処理: ログイン機能ができたら削除) ---
        if (teacher == null) {
            teacher = new Teacher();
            School school = new School();
            school.setCd("oom"); // DBの学校コードと一致させる
            teacher.setSchool(school);
            session.setAttribute("user", teacher);
        }

        // --- 3. リクエストパラメータの取得 ---
        entYearStr = request.getParameter("f1");
        classNum = request.getParameter("f2");
        isAttendStr = request.getParameter("f3");

        // --- 4. ビジネスロジック(入学年度を数値変換) ---
        if (entYearStr != null && !entYearStr.isEmpty() && !entYearStr.equals("0")) {
            entYear = Integer.parseInt(entYearStr);
        }

        // 在学フラグの判定(チェックボックスがオンならパラメータが存在する)
        if (isAttendStr != null) {
            isAttend = true;
        }

        // --- 5. プルダウン用のデータ生成 ---
        // 入学年度プルダウン: 10年前から1年後まで
        List<Integer> entYearSet = new ArrayList<>();
        for (int i = year - 10; i <= year + 1; i++) {
            entYearSet.add(i);
        }

        // クラス番号プルダウン: ログインユーザーの学校コードをもとに取得
        List<String> classNumSet = new ArrayList<>();
        try {
            classNumSet = cNumDao.filter(teacher.getSchool());
        } catch (Exception e) {
            System.out.println("★クラス一覧の取得に失敗しましたが続行します: " + e.getMessage());
        }

        // --- 6. 学生一覧の取得(条件によって分岐) ---
        if (entYear != 0 && classNum != null && !classNum.equals("0")) {
            // 入学年度とクラス番号を指定
            students = sDao.filter(teacher.getSchool(), entYear, classNum, isAttend);
        } else if (entYear != 0 && (classNum == null || classNum.equals("0"))) {
            // 入学年度のみ指定
            students = sDao.filter(teacher.getSchool(), entYear, isAttend);
        } else if (entYear == 0 && (classNum == null || classNum.equals("0"))) {
            // 指定なし(初期表示時もここ): 全学生情報を取得
            students = sDao.filter(teacher.getSchool(), isAttend);
        } else {
            // クラスのみ指定された場合はエラー
            errors.put("f1", "クラスを指定する場合は入学年度も指定してください");
            students = sDao.filter(teacher.getSchool(), isAttend);
        }

        System.out.println("★デバッグ: 取得件数 -> " + (students != null ? students.size() : 0));

        // --- 7. レスポンス値をセット ---
        request.setAttribute("f1", entYear);
        request.setAttribute("f2", classNum);
        request.setAttribute("f3", isAttendStr);
        request.setAttribute("students", students);
        request.setAttribute("class_num_set", classNumSet);
        request.setAttribute("ent_year_set", entYearSet);
        request.setAttribute("errors", errors);

        // --- 8. JSPへフォワード ---
        request.getRequestDispatcher("student_list.jsp").forward(request, response);
    }
}