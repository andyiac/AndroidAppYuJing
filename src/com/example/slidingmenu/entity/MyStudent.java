package com.example.slidingmenu.entity;

/**
 * 学生类
 */
public class MyStudent 
{
	private int id;//学生编号
	
	private String name;//姓名
	
	private int score;//分数
	
	private String num;//电话号码
	
	private boolean attendance;//出勤标志位
	
	/**
	 * @param id 学生编号
	 * @param name 学生姓名
	 */
	public MyStudent(int id,String name,String num, int score)
	{
		this.id = id;
		this.name = name;
		this.num = num;
		this.score =score;
		attendance = true;
	}
	
	/**
	 * 设置学生编号
	 * @param id 编号
	 */
	public void setId(int id)
	{
		this.id = id;
	}
	
	/**
	 * 获取学生编号
	 * @return id 编号
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * 设置学生姓名
	 * @param name 姓名
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * 获取学生姓名
	 * @return name 姓名
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * 设置学生分数
	 * @param score
	 */
	public void setScore(int score)
	{
		this.score = score;
	}
	
	/**
	 * 获取学生分数
	 * @return
	 */
	public int getScore()
	{
		return score;
	}
	
	/**
	 * 设置学生号码
	 * @param num
	 */
	public void setNum(String num)
	{
		this.num = num;
	}
	
	/**
	 * 获取学生号码
	 * @return
	 */
	public String getNum()
	{
		return num;
	}
	
	/**
	 * 设置出勤标志位
	 * @param b
	 */
	public void setAttendance(boolean b)
	{
		attendance = b;
	}
	
	/**
	 * 获得出勤标志位
	 * @return
	 */
	public boolean getAttendance()
	{
		return attendance;
	}
}
