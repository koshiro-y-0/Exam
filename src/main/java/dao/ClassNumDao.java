package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;

public class ClassNumDao extends Dao {

    /**
     * 学校を指定してクラス番号の一覧を取得する
     */
    public List<String> filter(School school) throws Exception {
        List<String> list = new ArrayList<>();

        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet rSet = null;

        try {
            // ★ 修正: 列名を CLASS_NUM → NAME に変更
            statement = connection.prepareStatement(
                "select name from class_num where school_cd = ? order by name asc"
            );
            statement.setString(1, school.getCd());
            rSet = statement.executeQuery();

            while (rSet.next()) {
                list.add(rSet.getString("name"));
            }

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

        return list;
    }
}