package dao;
 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;
import bean.Subject;
import bean.Test;
 
public class TestDao extends Dao {
 
    // 成績を１件取得する
    public Test get(Student student, Subject subject,
                    School school, int no) throws Exception {
        Test test = null;
        Connection con = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
 
        try {
            st = con.prepareStatement("select * from test where student_no=? and subject_cd=? " +"and school_cd=? and no=?"
            );
            st.setString(1, student.getNo());
            st.setString(2, subject.getCd());
            st.setString(3, school.getCd());
            st.setInt(4, no); 
 
            rs = st.executeQuery();
 
            if (rs.next()) {
                test = new Test();
                test.setStudent(student);
                test.setSubject(subject);
                test.setSchool(school);
                test.setNo(rs.getInt("no"));
                test.setPoint(rs.getInt("point"));
                test.setClassNum(rs.getString("class_num"));
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (st != null) try { st.close(); } catch (SQLException e) {}
            if (con != null) try { con.close(); } catch (SQLException e) {}
        }
        return test;
    }
    
    // saveメソッド
    public boolean save(Test test) throws Exception {
        Connection con = getConnection();
        PreparedStatement st = null;
        int count = 0;
     
        try {
            // 既存データがあるか確認
            Test old = get(test.getStudent(), test.getSubject(),
                              test.getSchool(), test.getNo());
     
            if (old == null) {
                // 新規登録
                st = con.prepareStatement(
                    "insert into test (student_no, subject_cd, school_cd, " +
                    "no, point, class_num) values (?, ?, ?, ?, ?, ?)"
                );
                st.setString(1, test.getStudent().getNo());
                st.setString(2, test.getSubject().getCd());
                st.setString(3, test.getSchool().getCd());
                st.setInt(4, test.getNo());
                st.setInt(5, test.getPoint());
                st.setString(6, test.getClassNum());
            } else {
                // 更新(pointだけ変える)
                st = con.prepareStatement(
                    "update test set point=? where student_no=? and " +
                    "subject_cd=? and school_cd=? and no=?"
                );
                st.setInt(1, test.getPoint());
                st.setString(2, test.getStudent().getNo());
                st.setString(3, test.getSubject().getCd());
                st.setString(4, test.getSchool().getCd());
                st.setInt(5, test.getNo());
            }
     
            count = st.executeUpdate();
     
        } finally {
            if (st != null) try { st.close(); } catch (SQLException e) {}
            if (con != null) try { con.close(); } catch (SQLException e) {}
        }
        return count > 0;
    }
     
    /**
     * 複数件まとめて保存する(for文で1件ずつsave)
     */
    public boolean save(List<Test> list) throws Exception {
        boolean allSuccess = true;
        for (Test test : list) {
            if (!save(test)) allSuccess = false;
        }
        return allSuccess;
    }
    
    // 科目別検索: 入学年度・クラス・科目・回数で絞り込み
    //ex)「2023年度入学・131クラス・英語・1回目テスト」の全員の点数を取得
    public List<Test> filter(int entYear, String classNum, Subject subject,
                             int no, School school) throws Exception {
        List<Test> list = new ArrayList<>();
        Connection con = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            // ★ポイント: testとstudentをJOINして学生名も一緒に取得
            st = con.prepareStatement(
                "select t.*, s.name as student_name " +
                "from test t join student s on t.student_no = s.no " +
                "where s.ent_year=? and s.class_num=? " +
                "and t.subject_cd=? and t.no=? and t.school_cd=? " +
                "order by t.student_no asc"
            );
            st.setInt(1, entYear);
            st.setString(2, classNum);
            st.setString(3, subject.getCd());
            st.setInt(4, no);
            st.setString(5, school.getCd());
            rs = st.executeQuery();
     
            while (rs.next()) {
                Test test = new Test();
                Student student = new Student();
                student.setNo(rs.getString("student_no"));
                student.setName(rs.getString("student_name"));
                test.setStudent(student);
                test.setSubject(subject);
                test.setSchool(school);
                test.setNo(rs.getInt("no"));
                test.setPoint(rs.getInt("point"));
                test.setClassNum(rs.getString("class_num"));
                list.add(test);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (st != null) try { st.close(); } catch (SQLException e) {}
            if (con != null) try { con.close(); } catch (SQLException e) {}
        }
        return list;
    }
     
    // 学生別検索: 1人の学生の全科目・全回数の成績を取得
    public List<Test> filter(Student student) throws Exception {
        List<Test> list = new ArrayList<>();
        Connection con = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.prepareStatement(
                "select t.*, sub.name as subject_name " +
                "from test t join subject sub on t.subject_cd = sub.cd " +
                "and t.school_cd = sub.school_cd " +
                "where t.student_no=? and t.school_cd=? " +
                "order by t.subject_cd, t.no"
            );
            st.setString(1, student.getNo());
            st.setString(2, student.getSchool().getCd());
            rs = st.executeQuery();
     
            while (rs.next()) {
                Test test = new Test();
                Subject subject = new Subject();
                subject.setCd(rs.getString("subject_cd"));
                subject.setName(rs.getString("subject_name"));
                subject.setSchool(student.getSchool());
                test.setStudent(student);
                test.setSubject(subject);
                test.setSchool(student.getSchool());
                test.setNo(rs.getInt("no"));
                test.setPoint(rs.getInt("point"));
                test.setClassNum(rs.getString("class_num"));
                list.add(test);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (st != null) try { st.close(); } catch (SQLException e) {}
            if (con != null) try { con.close(); } catch (SQLException e) {}
        }
        return list;
    }
    
    /**
     * 成績を1件削除する(複合キー: 学生番号+科目+学校+回数)
     */
    public boolean delete(Student student, Subject subject, School school, int no) throws Exception {
        Connection con = getConnection();
        PreparedStatement st = null;
        int count = 0;

        try {
            st = con.prepareStatement(
                "delete from test where student_no=? and subject_cd=? and school_cd=? and no=?"
            );
            st.setString(1, student.getNo());
            st.setString(2, subject.getCd());
            st.setString(3, school.getCd());
            st.setInt(4, no);

            count = st.executeUpdate();

        } finally {
            if (st != null) try { st.close(); } catch (SQLException e) {}
            if (con != null) try { con.close(); } catch (SQLException e) {}
        }

        return count > 0;
    }
    
    /**
     * 科目別参照用: 入学年度・クラス・科目で絞り込み(回数指定なし・全回数取得)
     */
    public List<Test> filterBySubject(int entYear, String classNum,
                                       Subject subject, School school) throws Exception {
        List<Test> list = new ArrayList<>();
        Connection con = getConnection();
        PreparedStatement st = null;
        ResultSet rs = null;
        try {
            st = con.prepareStatement(
                "select t.*, s.name as student_name, s.ent_year as s_ent_year, " +
                "s.class_num as s_class_num " +
                "from test t join student s on t.student_no = s.no " +
                "where s.ent_year=? and s.class_num=? " +
                "and t.subject_cd=? and t.school_cd=? " +
                "order by t.student_no asc, t.no asc"
            );
            st.setInt(1, entYear);
            st.setString(2, classNum);
            st.setString(3, subject.getCd());
            st.setString(4, school.getCd());
            rs = st.executeQuery();

            while (rs.next()) {
                Test test = new Test();
                Student student = new Student();
                student.setNo(rs.getString("student_no"));
                student.setName(rs.getString("student_name"));
                student.setEntYear(rs.getInt("s_ent_year"));
                student.setClassNum(rs.getString("s_class_num"));
                student.setSchool(school);
                test.setStudent(student);
                test.setSubject(subject);
                test.setSchool(school);
                test.setNo(rs.getInt("no"));
                test.setPoint(rs.getInt("point"));
                test.setClassNum(rs.getString("class_num"));
                list.add(test);
            }
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException e) {}
            if (st != null) try { st.close(); } catch (SQLException e) {}
            if (con != null) try { con.close(); } catch (SQLException e) {}
        }
        return list;
    }
}
