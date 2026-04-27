package bean;

import java.io.Serializable;

public class Student implements Serializable { // [ を { に修正

	/**
	 * 学生番号 : String
	 */
	private String no;

	/**
	 * 学生名 : String
	 */
	private String name;

	/**
	 * 入学年度 : int
	 */
	private int entYear;

	/**
	 * クラス番号 : String
	 */
	private String classNum;

	/**
	 * 在学フラグ : boolean
	 */
	private boolean isAttend;

	/**
	 * 学校 : school
	 */
	private School school;
	
	private String lineUserId;

	/**
	 * ゲッタ・セッタ
	 */
	public String getNo() { // [ を { に修正
		return no;
	}

	public void setNo(String no) { // [ を { に修正
		this.no = no;
	}

	public String getName() { // [ を { に修正
		return name;
	}

	public void setName(String name) { // [ を { に修正
		this.name = name;
	}

	public int getEntYear() { // [ を { に修正
		return entYear;
	}

	public void setEntYear(int entYear) { // [ を { に修正
		this.entYear = entYear;
	}

	public String getClassNum() { // [ を { に修正
		return classNum;
	}

	public void setClassNum(String classNum) { // [ を { に修正
		this.classNum = classNum;
	}

	public boolean isAttend() { // [ を { に修正
		return isAttend;
	}
	
	public void setAttend(boolean isAttend) {
		this.isAttend = isAttend;
	}

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}
	
	//////LINE ユーザーID
	public String getLineUserId() {
	 return lineUserId;
	}
	
	public void setLineUserId(String lineUserId) {
	 this.lineUserId = lineUserId;
	}
}