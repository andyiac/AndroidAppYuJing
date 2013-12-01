/*
package com.example.slidingmenu.entity;

import java.util.ArrayList;


*/
/**
 * 数据类(单例模式)
 *//*

public class MyData {
	
		//声明 静态MyDate 类型变量 uniqueInstance 并实例化
		private static MyData uniqueInstance = new MyData();
		
		//声明ArrayList类型的变量 myClassList
		private ArrayList<MyClass> myClassList;
		
		private MyData()
		{
			//myClassList 实例化
			myClassList = new ArrayList<MyClass>();
		}
		
		*/
/**
		 * 获得uniqueInstance实例
		 * @return uniqueInstance
		 *//*

		public static MyData getInstance()
		{
			return uniqueInstance;
		}
		
		*/
/**
		 * 获得班级链表
		 * @return myClassList
		 *//*

		public ArrayList<MyClass> getClassList()
		{
			return myClassList;
		}
		
		*/
/**
		 * 增加班级
		 * @param name 班级名称
		 *//*

		public void addClass(String name)
		{
			myClassList.add(new MyClass(myClassList.size(),name));
		}
		
		*/
/**
		 * 增加学生
		 * @param i 第i个班级
		 * @param name 学生姓名
		 *//*

		public void addStudent(int i,String name,String num, int score)
		{
			if(i<myClassList.size())
			{
				myClassList.get(i).getStudentList().add(new MyStudent(myClassList.get(i).getStudentList().size(), name,num,score));
			}		
		}
	}
*/

