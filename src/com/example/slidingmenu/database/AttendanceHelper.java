package com.example.slidingmenu.database;

import com.example.slidingmenu.database.table.Myclass;
import com.example.slidingmenu.database.table.Student;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class AttendanceHelper extends SQLiteOpenHelper{
	
    private static final String DB_NAME = "Attendance.db";
    private static final int DB_VESION = 1;
    
	public AttendanceHelper(Context context) {
		
		super(context, DB_NAME, null, DB_VESION);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 创建接口
	 * 实现各表的创建
	 */
	public static interface TableCreateInterface {
		/**
		 * 创建表
		 * @param db
		 */
		public void onCreate(SQLiteDatabase db);
		
		/**
		 * 更新表
		 * @param db
		 * @param oldVersion
		 * @param newVersion
		 */
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	  
		Myclass.getInstance().onCreate(db);
		Student.getInstance().onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		Myclass.getInstance().onUpgrade(db, oldVersion, newVersion);
		Student.getInstance().onUpgrade(db, oldVersion, newVersion);
	}


    
}
