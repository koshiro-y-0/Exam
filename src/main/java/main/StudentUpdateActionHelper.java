package main;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import bean.Teacher;
import dao.ClassNumDao;
import jakarta.servlet.http.HttpServletRequest;

public class StudentUpdateActionHelper {

    public void setupPullDowns(HttpServletRequest request, Teacher teacher) {

        // 入学年度プルダウン
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

        request.setAttribute("ent_year_set", entYearSet);
        request.setAttribute("class_num_set", classNumSet);
    }
}