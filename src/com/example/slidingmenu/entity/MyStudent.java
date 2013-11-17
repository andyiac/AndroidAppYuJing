package com.example.slidingmenu.entity;

/**
 * 学生类
 */
public class MyStudent {
	private int id;//学生编号
	
	private String className;
	
	private String studentNo;
	
	private String StudentName;//姓名
	
	private String score;//分数

	
	public MyStudent() { }
	
	public MyStudent(int id) {
		super();
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getStudentNo() {
		return studentNo;
	}

	public void setStudentNo(String studentNo) {
		this.studentNo = studentNo;
	}

	public String getStudentName() {
		return StudentName;
	}

	public void setStudentName(String studentName) {
		StudentName = studentName;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	@Override
	public String toString() {
		return "MyStudent [id=" + id + ", className=" + className
				+ ", studentNo=" + studentNo + ", StudentName=" + StudentName
				+ ", score=" + score + "]";
	}
	
	
	
}
