package com.example.slidingmenu.entity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import android.app.Activity;
import android.content.Context;


/**
 * 数据管理类
 */
public class DateManager//单例模式 
{	
	private static DateManager uniqueInstance = new DateManager();
	
	private FileOutputStream fos;//文件
	private FileInputStream fis;
	private DataOutputStream dos;
	private DataInputStream dis;	

	private String fileName = "date.txt"; 
	
	private DateManager(){}//构造方法		
	
	public static DateManager getInstance()//可以通过类名直接调用
	{
		return uniqueInstance;
	}
	
	/**
	 * 存储数据
	 * @param activity 
	 */
	public void save(Activity activity) 
	{
		try
		{			
			//如果date.txt创建了，就打开，未创建，就新建date.txt
			fos = activity.openFileOutput(fileName,Context.MODE_PRIVATE);
			//把	date.txt转变成数据流进行处理	
			dos = new DataOutputStream(fos);
			saveListDate();	//存储链表数据		
		} catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally 
		{
			try 
			{
				dos.close();
				fos.close();
			} catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}		
	
	/**
	 * 读取数据
	 * @param activity
	 */
	public void load(Activity activity) 
	{
		try 
		{
			if (activity.openFileInput(fileName) != null)//判断文件是否存在 
			{
				fis = activity.openFileInput(fileName);
				dis = new DataInputStream(fis);	//数据流		
				loadListDate();
			}
		} catch (FileNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally 
		{
			try 
			{
				if (activity.openFileInput(fileName) != null) 
				{
					fis.close();
				}
			} catch (FileNotFoundException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 存储链表中数据
	 */
	private void saveListDate()
	{
		//获取当前班级链表长度
		int classSize = MyData.getInstance().getClassList().size();			
		try 
		{
			//保存班级长度	
			dos.writeInt(classSize);
		} catch (IOException e1) 
		{
			e1.printStackTrace();
		}	
		
		//遍历班级链表
		for(int i=0;i<classSize;i++)
		{			
			try 
			{
				//保存第i个班级的名称
				dos.writeUTF(MyData.getInstance().getClassList().get(i).getName());
			} catch (IOException e)
			{
				e.printStackTrace();
			}			
			//获取第i个班级的学生链表长度
			int studentSize = MyData.getInstance().getClassList().get(i).getStudentList().size();			
			try 
			{
				//保存第i个班级的学生链表长度	
				dos.writeInt(studentSize);
			} catch (IOException e1) 
			{
				e1.printStackTrace();
			}
			//遍历第i个班级的学生链表
			for(int j=0;j<studentSize;j++)
			{				
				try
				{
					//保存第i个班级下第j个学生的姓名
					dos.writeUTF(MyData.getInstance().getClassList().get(i).getStudentList().get(j).getName());
					dos.writeInt(MyData.getInstance().getClassList().get(i).getStudentList().get(j).getScore());
					dos.writeUTF(MyData.getInstance().getClassList().get(i).getStudentList().get(j).getNum());
				} catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 读取链表中数据
	 */
	private void loadListDate()
	{
		int classSize = 0;//初始化
		try 
		{
			classSize = dis.readInt();//读取班级长度
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}	
		for(int i=0;i<classSize;i++)
		{
			try 
			{
				MyData.getInstance().addClass(dis.readUTF());//创建班级
			} 
			catch (IOException e)
			{
				e.printStackTrace();
			}
			int studentSize = 0;
			try 
			{
				studentSize = dis.readInt();
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
			for(int j=0;j<studentSize;j++)
			{
				try 
				{   
					MyData.getInstance().addStudent(i, dis.readUTF(),dis.readUTF(),dis.readInt());
					MyData.getInstance().getClassList().get(i).getStudentList().get(j).setScore(dis.readInt());
					MyData.getInstance().getClassList().get(i).getStudentList().get(j).setNum(dis.readUTF());
				}
				catch (IOException e) 
				{
					e.printStackTrace();
				}
			}
		}	
	}
	
}
