package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Subject;

public class SubjectDao extends Dao {

    /** 全科目を取得するための基本SQL */
    private String baseSql = "select * from subject where school_cd = ? order by cd asc";

    /**
     * ResultSet から Subject インスタンスのリストを作成する
     */
    
    /**
     * 科目コードと学校を指定して科目を1件取得する
     * 存在しない場合は null を返す
     */
    public Subject get(String cd, School school) throws Exception {
        Subject subject = null;

        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet rSet = null;

        try {
            // SQL: 科目コードと学校コードを条件に1件取得
            statement = connection.prepareStatement(
                "select * from subject where cd = ? and school_cd = ?"
            );
            statement.setString(1, cd);
            statement.setString(2, school.getCd());
            rSet = statement.executeQuery();

            if (rSet.next()) {
                // データが存在すれば Subject インスタンスにセット
                subject = new Subject();
                subject.setCd(rSet.getString("cd"));
                subject.setName(rSet.getString("name"));
                subject.setSchool(school);
            }
            // 存在しなければ null のまま返る

        } catch (Exception e) {
            throw e;
        } finally {
            // リソースの解放
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

        return subject;
    }
    
    private List<Subject> postFilter(ResultSet rSet, School school) throws Exception {
        List<Subject> list = new ArrayList<>();
        try {
            while (rSet.next()) {
                Subject subject = new Subject();
                subject.setCd(rSet.getString("cd"));
                subject.setName(rSet.getString("name"));
                subject.setSchool(school);
                list.add(subject);
            }
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 学校を指定して科目一覧を取得する
     */
    public List<Subject> filter(School school) throws Exception {
        List<Subject> list = new ArrayList<>();

        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet rSet = null;

        try {
            statement = connection.prepareStatement(baseSql);
            statement.setString(1, school.getCd());
            rSet = statement.executeQuery();

            list = postFilter(rSet, school);

        } catch (Exception e) {
            throw e;
        } finally {
            // リソースの解放
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
    
    /**
     * 科目情報を保存(存在すれば更新、無ければ新規登録)する
     * 戻り値: 保存に成功すれば true
     */
    public boolean save(Subject subject) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        int count = 0;

        try {
            // まず同じ科目コード+学校コードが既に存在するか確認
            Subject old = get(subject.getCd(), subject.getSchool());

            if (old == null) {
                // --- 新規登録(INSERT) ---
                statement = connection.prepareStatement(
                    "insert into subject (cd, school_cd, name) values (?, ?, ?)"
                );
                statement.setString(1, subject.getCd());
                statement.setString(2, subject.getSchool().getCd());
                statement.setString(3, subject.getName());
            } else {
                // --- 更新(UPDATE) ---
                // ※科目コードと学校コードは複合主キーなので変更不可、name のみ更新
                statement = connection.prepareStatement(
                    "update subject set name = ? where cd = ? and school_cd = ?"
                );
                statement.setString(1, subject.getName());
                statement.setString(2, subject.getCd());
                statement.setString(3, subject.getSchool().getCd());
            }

            count = statement.executeUpdate();

        } catch (Exception e) {
            throw e;
        } finally {
            if (statement != null) {
                try { statement.close(); } catch (SQLException sqle) { throw sqle; }
            }
            if (connection != null) {
                try { connection.close(); } catch (SQLException sqle) { throw sqle; }
            }
        }
        return count > 0;
    }
}