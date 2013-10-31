package com.example.slidingmenu.entity;

import java.util.ArrayList;


/**
 * 班级类
 */
public class MyClass 
{
	private int id;//班级编号
	
	private String name;//班级名称
	
	private ArrayList<MyStudent> myStudentList;//该班级内的学生链表
	
	/**
	 * @param id 班级编号
	 * @param name 班级名称
	 */
	public MyClass(int id,String name)
	{
		this.id = id;
		this.name = name;
		myStudentList = new ArrayList<MyStudent>();//新建学生链表
	}
	
	/**
	 * 设置编号
	 * @param id 编号
	 */
	public void setId(int id)
	{
		this.id = id;
	}	
	
	/**
	 * 获取编号
	 * @return id 编号
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * 设置名称
	 * @param id 名称
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * 获取名称
	 * @return name 名称
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * 获取该班级的学生列表
	 * @return myStudentList 学生列表
	 */
	public ArrayList<MyStudent> getStudentList()
	{
		return myStudentList;
	}
}
