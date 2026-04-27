package main;
 
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import bean.Student;
import bean.Subject;
import bean.Teacher;
import bean.Test;
import dao.StudentDao;
import dao.SubjectDao;
import dao.TestDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import tool.Action;
 
public class StudentAnalysisAction extends Action {
 
    @Override
    public void execute(HttpServletRequest request,
                        HttpServletResponse response) throws Exception {
 
        HttpSession session = request.getSession();
        Teacher teacher = (Teacher) session.getAttribute("user");
        if (teacher == null) {
            response.sendRedirect(request.getContextPath() + "/scoremanager/login.jsp");
            return;
        }
 
        String no = request.getParameter("no");
        StudentDao sDao = new StudentDao();
        Student student = sDao.get(no);
        if (student == null) { response.sendRedirect("StudentList.action"); return; }
 
        TestDao tDao = new TestDao();
        List<Test> tests = tDao.filter(student);
 
        if (tests.isEmpty()) {
            request.setAttribute("student", student);
            request.setAttribute("no_data", true);
            request.getRequestDispatcher("student_analysis.jsp").forward(request, response);
            return;
        }
 
        // 科目名 → 最新点数 / 科目コード / 回数ごとのリスト
        Map<String, Integer> latestPointMap = new LinkedHashMap<>();
        Map<String, String>  subjectCdMap   = new LinkedHashMap<>();
        Map<String, List<Integer>> trendMap = new LinkedHashMap<>();
        Map<String, Double>  deviationMap   = new LinkedHashMap<>();
 
        SubjectDao subDao = new SubjectDao();
 
        for (Test t : tests) {
            String name = t.getSubject().getName();
            subjectCdMap.put(name, t.getSubject().getCd());
            if (!trendMap.containsKey(name)) trendMap.put(name, new ArrayList<>());
            trendMap.get(name).add(t.getPoint());
            latestPointMap.put(name, t.getPoint());
        }
 
        for (String name : latestPointMap.keySet()) {
            Subject subject = subDao.get(subjectCdMap.get(name), teacher.getSchool());
            int myPt  = latestPointMap.get(name);
            int latNo = trendMap.get(name).size();
            List<Integer> all = tDao.getClassPoints(
                subject, latNo, student.getClassNum(), teacher.getSchool());
            deviationMap.put(name, all.size() > 1 ? calcDeviation(myPt, all) : null);
        }
 
        request.setAttribute("student", student);
        request.setAttribute("latest_point_map", latestPointMap);
        request.setAttribute("deviation_map", deviationMap);
        request.getRequestDispatcher("student_analysis.jsp").forward(request, response);
    }
 
    private double calcDeviation(int myPoint, List<Integer> all) {
        double sum = 0; for (int p : all) sum += p;
        double avg = sum / all.size();
        double v = 0; for (int p : all) v += Math.pow(p - avg, 2);
        double sd = Math.sqrt(v / all.size());
        if (sd == 0) return 50.0;
        return Math.round((50 + 10 * (myPoint - avg) / sd) * 10.0) / 10.0;
    }
}
