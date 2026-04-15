package bean;

public class Test {
	
	/////// フィールド ////////
	
	// 学生番号:String
	private String student_no;
	// 科目コード:String
	private String subject_cd;
	// 学校コード:String
	private String school_cd;
	// 回数:int
	private int no;
	// 得点:int
	private int point;
	// クラス番号:String
	private String class_num;
	
	/////////////////////////////
	
	
	/////// ゲッタ・セッタ /////////////
	
	////// 学生番号
	public String getStudent_no() {
		return student_no;
	}
	
	public void setStudent_no(String student_no) {
		this.student_no = student_no;
	}
	
	
	////// 科目コード
	public String getSubject_cd() {
		return subject_cd;
	}
	
	public void setSubject_cd(String subject_cd) {
		this.subject_cd = subject_cd;
	}
	////// 学校コード
	public String getSchool_cd() {
		return school_cd;
	}
	
	public void setSchool_cd(String school_cd) {
		this.school_cd = school_cd;
	}
	////// 回数
	public int getNo() {
		return no;
	}
	
	public void setNo(int no) {
		this.no = no;
	}
	////// 得点
	public int getPoint() {
		return point;
	}
	
	public void setPoint(int point) {
		this.point = point;
	}
	////// クラス番号
	public String getClass_num() {
		return class_num;
	}
	
	public void setClass_num(String class_num) {
		this.class_num = class_num;
	}
	
	///////////////////////////////////
	
}