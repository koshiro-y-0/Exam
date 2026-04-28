package bean;

import java.io.Serializable;

public class Teacher extends User implements Serializable {
    /**
     * 教員ID:String
     */
    private String id;

    /**
     * パスワード:String
     */
    private String password;

    /**
     * 教員名:String
     */
    private String name;

    /**
     * 所属校:School
     */
    private School school;

    /**
     * ゲッター・セッター
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // --- ここから下が不足している分です ---

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 所属校を取得します。
     * これがないと StudentListAction でエラーになります。
     */
    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
    
 // ★追加: LINE ユーザーID
    private String lineUserId;
     
    public String getLineUserId() { 
    	return lineUserId; 
    	}
    public void setLineUserId(String lineUserId) { 
    	this.lineUserId = lineUserId; 
    	}

}