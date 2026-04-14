package bean;

import java.io.Serializable;

public class ClassNum implements Serializable { 

	/**
	 * クラス名:class_num
	 */
	private String class_num;

	/**
	 * 学校:School
	 */
	private School school;

	/**
	 * ゲッター・セッター
	 */

	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public String getClass_num() {
		return class_num;
	}

	// （※以下、class_num のセッターが続くはずです）
	public void setClass_num(String class_num) {
		this.class_num = class_num;
	}
}