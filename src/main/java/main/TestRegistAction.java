package main;
 
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import dao.ClassNumDao;
import dao.StudentDao;
import dao.SubjectDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;
 
public class TestRegistAction extends Action {
    @Override
    public void execute(HttpServletRequest request,HttpServletResponse response) throws Exception {
 
        // ログインチェック
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (teacher == null) {
            response.sendRedirect(request.getContextPath()+ "/scoremanager/login.jsp");
            return;
        }
 
        // 絞り込み
        String entYearStr = request.getParameter("f1");
        String classNum = request.getParameter("f2");
        String subjectCd = request.getParameter("f3");
        String noStr = request.getParameter("f4");
 
        // プルダウン用データ
        int year = LocalDate.now().getYear();
        List<Integer> entYearSet = new ArrayList<>();
        for (int i = year - 10; i <= year + 1; i++) {
            entYearSet.add(i);
        }
 
        ClassNumDao cDao = new ClassNumDao();
        List<String> classNumSet = new ArrayList<>();
        try { classNumSet = cDao.filter(teacher.getSchool()); }
        catch (Exception e) {}
 
        // 科目プルダウン
        SubjectDao subDao = new SubjectDao();
        List<Subject> subjects = subDao.filter(teacher.getSchool());
 
        // 4条件が揃ったら学生一覧を取得
        List<Student> students = new ArrayList<>();
        if (entYearStr != null && !entYearStr.isEmpty()
                && classNum != null && !classNum.equals("0")
                && subjectCd != null && !subjectCd.equals("0")
                && noStr != null && !noStr.isEmpty()) {
 
            int entYear = Integer.parseInt(entYearStr);
            StudentDao sDao = new StudentDao();
            students = sDao.filter(teacher.getSchool(), entYear, classNum, true);
 
            request.setAttribute("students", students);
            request.setAttribute("f1", entYear);
            request.setAttribute("f2", classNum);
            request.setAttribute("f3", subjectCd);
            request.setAttribute("f4", noStr);
        }
 
        request.setAttribute("ent_year_set", entYearSet);
        request.setAttribute("class_num_set", classNumSet);
        request.setAttribute("subject_set", subjects);
 
        request.getRequestDispatcher("test_regist.jsp").forward(request, response);
    }
}
