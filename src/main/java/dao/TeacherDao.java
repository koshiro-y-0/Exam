package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import bean.School;
import bean.Teacher;

public class TeacherDao extends Dao {

    /**
     * ID とパスワードで教員を検索する
     * 一致する教員が存在すれば Teacher を返す、存在しなければ null を返す
     */
    public Teacher login(String id, String password) throws Exception {
        Teacher teacher = null;

        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet rSet = null;

        try {
            statement = connection.prepareStatement(
                "select * from teacher where id = ? and password = ?"
            );
            statement.setString(1, id);
            statement.setString(2, password);
            rSet = statement.executeQuery();

            if (rSet.next()) {
                // 一致する教員が見つかった → Teacher インスタンスにセット
                teacher = new Teacher();
                teacher.setId(rSet.getString("id"));
                teacher.setPassword(rSet.getString("password"));
                teacher.setName(rSet.getString("name"));

                // 学校情報もセット
                School school = new School();
                school.setCd(rSet.getString("school_cd"));
                teacher.setSchool(school);
            }
            // 一致しなければ teacher は null のまま

        } catch (Exception e) {
            throw e;
        } finally {
            if (rSet != null) {
                try { rSet.close(); } catch (SQLException sqle) { throw sqle; }
            }
            if (statement != null) {
                try { statement.close(); } catch (SQLException sqle) { throw sqle; }
            }
            if (connection != null) {
                try { connection.close(); } catch (SQLException sqle) { throw sqle; }
            }
        }

        return teacher;
    }
}