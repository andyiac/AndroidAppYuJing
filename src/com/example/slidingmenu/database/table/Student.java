package com.example.slidingmenu.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.slidingmenu.database.AttendanceHelper;
import com.example.slidingmenu.database.AttendanceHelper.TableCreateInterface;


public class Student implements TableCreateInterface{
	
	private static String tableName = "student";
	private static String _id ="_id";
	private static String cid  = "cid"; 
	private static String sno = "sno"; 
	private static String sname = "sname";
	private static String num = "num";
	private static String grade = "grade";
	private static String attendance = "attendance";
	
	
	
    private static Student student= new Student();
	
	public static Student getInstance() {
		
		return Student.student;
	}

	/**
	 * 创建学生表
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		String sql = "CREATE TABLE "
				+ Student.tableName
				+ " (  "
				+ Student._id +" integer primary key,"
				+ Student.cid+ " integer,"
				+ Student.sno + " TEXT,"
				+ Student.sname + " TEXT,"
				+ Student.num + " TEXT,"
				+ Student.grade + " TEXT,"
				+ Student.attendance + " TEXT)";
		db.execSQL( sql );
	}
	
	/**
	 * 更新数据库
	 */
    @Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if ( oldVersion < newVersion ) {
			String sql = "DROP TABLE IF EXISTS " + Student.tableName;
			db.execSQL( sql );
			this.onCreate( db );
		}
	}
	/**
	 * 向表中插入数据
	 * @param attendhelper 数据库辅助类
	 * @param classname   班级
	 * @param sno   学号
	 * @param name  姓名
	 * @param num   电话号码
	 * @param grade  成绩
	 * @param attendance  考勤标志
	 */
	public static void insertStudent(AttendanceHelper attendhelper, int cid, String sno, String name, String num, String grade, String attendance) {
		SQLiteDatabase db = attendhelper.getWritableDatabase();
		
		ContentValues contentValues = new ContentValues();
		contentValues.put(Student.cid, cid);
		contentValues.put(Student.sno, sno);
		contentValues.put(Student.sname, name);
		contentValues.put(Student.num, num);
		contentValues.put(Student.grade, grade);
		contentValues.put(Student.attendance, attendance);
		db.insert(tableName, null, contentValues);
		db.close();
	}
	/**
	 * 删除该学号数据   
	 * @param attendhelper  数据库辅助类
	 * @param classname  班级
	 * @param sno 学号
	 */
	public static void deleteStudent(AttendanceHelper attendhelper ,int cid, int sid) {
		SQLiteDatabase db = attendhelper.getWritableDatabase();
		db.delete(tableName, Student._id + "=?" +" and " + Student.cid + "=?",new String[] { sid + "" , cid + "" }  );
		db.close();
	}
	
	/**
	 * 删除该班级的所有学生
	 * @param attendhelper 数据库辅助类
	 * @param classname  班级
	 */
	public static void deleteAllStudent(AttendanceHelper attendhelper , int id) {
		SQLiteDatabase db = attendhelper.getWritableDatabase();
		db.delete(tableName, Student.cid + "=?",new String[] { id +"" }  );
		db.close();
	}
	
	/**
	 * 得到该班级的所有学生
	 * @param attendhelper 数据库辅助类 
	 * @param classname    传递过来的班级名字
	 * @return   Cursor
	 */
	public static Cursor getAllStudentName(AttendanceHelper attendhelper,int id) {
		SQLiteDatabase db = attendhelper.getReadableDatabase();
		Cursor cursor = db.query(tableName, null, Student.cid + "=?", new String[]{ id + "" },
				null, null,Student.sno + " COLLATE LOCALIZED ASC");
		cursor.moveToFirst();
		return cursor;
   }
	/**
	 * 获得学生相关信息
	 * @param attendhelper
	 * @param classname
	 * @param sno
	 * @return
	 */
	public static Cursor getStudentName(AttendanceHelper attendhelper,int id,int sid) {
		SQLiteDatabase db = attendhelper.getReadableDatabase();
		Cursor cursor = db.query(tableName, null, Student.cid + "=?" + " and " + Student._id +"=?", new String[]{id + "", sid + "" },
				null, null, null);
		 cursor.moveToFirst();
		 return cursor;
		
	}
	/**
	 * 获得学生姓名
	 * @param attendhelper   数据库辅助类
	 * @param classname   学生的班级
	 * @param sid   学生表中的学生id
	 * @return  String类型
	 *//*
	public static String getStudentName(AttendanceHelper attendhelper,int id,int sid) {
		SQLiteDatabase db = attendhelper.getReadableDatabase();
		Cursor cursor = db.query(tableName, new String[] {Student.sname}, Student.cid + "=?" + " and " + Student._id +"=?", new String[]{id+"" , sid + ""},
				null, null, null);
		cursor.moveToFirst();
		String name =cursor.getString(cursor.getColumnIndex(Student.sname));
		return name;
		
	}*/
	
	/**
	 * 获得学生成绩
	 * @param attendhelper
	 * @param id
	 * @param sid
	 * @return
	 */
	public static String getStudentGrade(AttendanceHelper attendhelper,int id,int sid) {
		SQLiteDatabase db = attendhelper.getReadableDatabase();
		Cursor cursor = db.query(tableName, null,Student._id + "=?" + " and " + Student.cid + "=?", new String[]{sid + "", id + ""}, null ,null,Student._id + " COLLATE LOCALIZED ASC");
		cursor.moveToFirst();
		String grade = cursor.getString(cursor.getColumnIndex(Student.grade));
		return grade;
	}
	/**
	 * 得到该班级id最大学生
	 * @param attendhelper 数据库辅助类 
	 * @param classname    传递过来的班级名字
	 * @return   Cursor
	 */
	public static int getMaxIdStudent(AttendanceHelper attendhelper,int id) {
		String strSql = "select max(_id) AS maxId from student" + " where cid = " + id;
		SQLiteDatabase db = attendhelper.getReadableDatabase();
		Cursor cursor = db.rawQuery(strSql, null);
		cursor.moveToNext();
		int maxId = cursor .getInt(cursor.getColumnIndex("maxId")); 
		/*Cursor cursor = db.query("tableName", null, Student.classname + "=?", new String[]{ classname }, null, null, Student._id + " DESC");
		cursor.moveToNext(); 
		int id = cursor.getInt(cursor.getColumnIndex("_id"));*/
		// 这个id就是最大值
		return maxId ;
		
   }
	
	/**
	 * 更新学生成绩
	 * @param attendhelper
	 * @param id
	 * @param sid
	 * @param grade
	 */
	public static void updateStudentGrade(AttendanceHelper attendhelper,int id,int sid,String grade) {
		SQLiteDatabase db = attendhelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(Student.grade, grade);
		db.update(tableName, contentValues, Student._id + "=?" + " and " +Student.cid + "=?" ,new String[]{sid + "",id + ""});
	    db.close();
	}
	
	/***
	 * 更新学生考勤标记
	 * @param attendhelper
	 * @param id
	 * @param sid
	 * @param attendance
	 */
	public static void updateStudentAttend(AttendanceHelper attendhelper,int id,int sid,String attendance) {
		SQLiteDatabase db = attendhelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(Student.attendance, attendance);
		db.update(tableName, contentValues, Student._id + "=?" + " and " +Student.cid + "=?" ,new String[]{ sid +"",id + ""});
	    db.close();
	}
	
	/**
	 * 更新学生信息
	 * @param attendhelper
	 * @param id
	 * @param sid
	 * @param sno
	 * @param name
	 * @param num
	 * @param grade
	 */
	public static void updateStudent(AttendanceHelper attendhelper, int id, int sid,String sno, String name, String num, String grade) {
		SQLiteDatabase db = attendhelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(Student.sno, sno);
		contentValues.put(Student.sname, name);
		contentValues.put(Student.num, num);
		contentValues.put(Student.grade, grade);
		db.update(tableName, contentValues, Student._id + "=?" + " and " +Student.cid + "=?",new String[]{
				sid+"", id+""
		});
	    db.close();
	}
}
