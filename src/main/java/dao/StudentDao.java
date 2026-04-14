package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import bean.School;
import bean.Student;

public class StudentDao extends Dao {

	/**
	 * 各メソッドで使用する Student テーブルからデータを取得する SQL のベース
	 */
	private String baseSql = "select * from student where school_cd=? ";

	/**
	 * 学生番号を指定して学生情報を1件取得する
	 */
	public Student get(String no) throws Exception {
		Student student = new Student();
		Connection connection = getConnection();
		PreparedStatement statement = null;

		try {
			statement = connection.prepareStatement("select * from student where no=?");
			statement.setString(1, no);
			ResultSet rSet = statement.executeQuery();

			SchoolDao schoolDao = new SchoolDao();

			if (rSet.next()) {
				student.setNo(rSet.getString("no"));
				student.setName(rSet.getString("name"));
				student.setEntYear(rSet.getInt("ent_year"));
				student.setClassNum(rSet.getString("class_num"));
				student.setAttend(rSet.getBoolean("is_attend"));
				student.setSchool(schoolDao.get(rSet.getString("school_cd")));
			} else {
				student = null;
			}
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
		return student;
	}

	/**
	 * リザルトセットを学生リストに変換する共通メソッド
	 */
	private List<Student> postFilter(ResultSet rSet, School school) throws Exception {
		List<Student> list = new ArrayList<>();
		try {
			while (rSet.next()) {
				Student student = new Student();
				student.setNo(rSet.getString("no"));
				student.setName(rSet.getString("name"));
				student.setEntYear(rSet.getInt("ent_year"));
				student.setClassNum(rSet.getString("class_num"));
				student.setAttend(rSet.getBoolean("is_attend"));
				student.setSchool(school);
				list.add(student);
			}
		} catch (SQLException e) {
			throw e;
		}
		return list;
	}

	/**
	 * 1. 入学年度・クラス・在学フラグを指定して検索
	 */
	public List<Student> filter(School school, int entYear, String classNum, boolean isAttend) throws Exception {
		List<Student> list = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		String condition = "and ent_year=? and class_num=? ";
		String order = "order by no asc";
		
		String conditionIsAttend = "";
		if (isAttend) {
			conditionIsAttend = "and is_attend=true ";
		}

		try {
			statement = connection.prepareStatement(baseSql + condition + conditionIsAttend + order);
			statement.setString(1, school.getCd());
			statement.setInt(2, entYear);
			statement.setString(3, classNum);
			ResultSet rSet = statement.executeQuery();
			list = postFilter(rSet, school);
		} catch (Exception e) {
			throw e;
		} finally {
			if (statement != null) { try { statement.close(); } catch (SQLException sqle) { throw sqle; } }
			if (connection != null) { try { connection.close(); } catch (SQLException sqle) { throw sqle; } }
		}
		return list;
	}

	/**
	 * 2. 入学年度・在学フラグを指定して検索（クラス指定なし）
	 */
	public List<Student> filter(School school, int entYear, boolean isAttend) throws Exception {
		List<Student> list = new ArrayList<>();
		Connection connection = getConnection();
		PreparedStatement statement = null;
		String condition = "and ent_year=? ";
		String order = "order by no asc";

		String conditionIsAttend = "";
		if (isAttend) {
			conditionIsAttend = "and is_attend=true ";
		}

		try {
			statement = connection.prepareStatement(baseSql + condition + conditionIsAttend + order);
			statement.setString(1, school.getCd());
			statement.setInt(2, entYear);
			ResultSet rSet = statement.executeQuery();
			list = postFilter(rSet, school);
		} catch (Exception e) {
			throw e;
		} finally {
			if (statement != null) { try { statement.close(); } catch (SQLException sqle) { throw sqle; } }
			if (connection != null) { try { connection.close(); } catch (SQLException sqle) { throw sqle; } }
		}
		return list;
	}

	/**
	 * 3. 学校と在学フラグのみ指定して検索（全学生）
	 */
	public List<Student> filter(School school, boolean isAttend) throws Exception {
	    List<Student> list = new ArrayList<>();
	    Connection connection = getConnection();
	    PreparedStatement statement = null;
	    String order = " order by no asc";

	    String conditionIsAttend = "";
	    if (isAttend) {
	        conditionIsAttend = " and is_attend=true";
	    }

	    try {
	        // 【修正】SQL文を変数 sql に代入する
	        String sql = baseSql + conditionIsAttend + order;
	        
	        // デバッグ出力
	        System.out.println("実行SQL: " + sql);
	        System.out.println("学校コード: " + school.getCd());

	        statement = connection.prepareStatement(sql);
	        statement.setString(1, school.getCd());
	        
	        ResultSet rSet = statement.executeQuery();
	        list = postFilter(rSet, school);
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
	    return list;
	}

	/**
	 * 学生情報を保存（存在すれば更新、なければ新規登録）する
	 */
	public boolean save(Student student) throws Exception {
		Connection connection = getConnection();
		PreparedStatement statement = null;
		int count = 0;

		try {
			Student old = get(student.getNo());
			if (old == null) {
				statement = connection.prepareStatement(
					"insert into student(no, name, ent_year, class_num, is_attend, school_cd) values(?, ?, ?, ?, ?, ?)"
				);
				statement.setString(1, student.getNo());
				statement.setString(2, student.getName());
				statement.setInt(3, student.getEntYear());
				statement.setString(4, student.getClassNum());
				statement.setBoolean(5, student.isAttend());
				statement.setString(6, student.getSchool().getCd());
			} else {
				statement = connection.prepareStatement(
					"update student set name=?, ent_year=?, class_num=?, is_attend=? where no=?"
				);
				statement.setString(1, student.getName());
				statement.setInt(2, student.getEntYear());
				statement.setString(3, student.getClassNum());
				statement.setBoolean(4, student.isAttend());
				statement.setString(5, student.getNo());
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