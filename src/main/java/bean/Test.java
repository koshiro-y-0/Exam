package bean;

import java.io.Serializable;

public class Test implements Serializable {

    /////// フィールド ////////

    // 学生(オブジェクトとして持つ)
    private Student student;

    // 科目(オブジェクトとして持つ)
    private Subject subject;

    // 学校(オブジェクトとして持つ)
    private School school;

    // 回数
    private int no;

    // 得点
    private int point;

    // クラス番号
    private String classNum;

    ////////////////////////////


    /////// ゲッタ・セッタ /////////////

    ////// 学生
    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    ////// 科目
    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    ////// 学校
    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
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
    public String getClassNum() {
        return classNum;
    }

    public void setClassNum(String classNum) {
        this.classNum = classNum;
    }

    ///////////////////////////////////
}