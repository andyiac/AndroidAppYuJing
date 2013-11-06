package com.example.slidingmenu.database.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.slidingmenu.database.AttendanceHelper;
import com.example.slidingmenu.database.AttendanceHelper.TableCreateInterface;


public class Student implements TableCreateInterface{
	
	private static String tableName = "student";
	private static String _id ="_id";
	private static String cid = "cid";
	private static String sname = "sname";
	private static String num = "num";
	private static String grade = "grade";
	private static String attendance = "attendance";
	
	
	
    private static Student student= new Student();
	
	public static Student getInstance() {
		
		return Student.student;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		
		String sql = "CREATE TABLE "
				+ Student.tableName
				+ " (  "
				+ Student._id +" integer primary key,"
				+ Student.cid + " integer,"
				+ Student.sname + " TEXT,"
				+ Student.num + " TEXT,"
				+ Student.grade + " integer,"
				+ Student.attendance + " TEXT)";
		db.execSQL( sql );
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if ( oldVersion < newVersion ) {
			String sql = "DROP TABLE IF EXISTS " + Student.tableName;
			db.execSQL( sql );
			this.onCreate( db );
		}
	}
	
	public static void insertStudent(AttendanceHelper attendhelper, int id, String name, String num, int grade, String attendance) {
		SQLiteDatabase db = attendhelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(Student.cid, id);
		contentValues.put(Student.sname, name);
		contentValues.put(Student.num, num);
		contentValues.put(Student.grade, grade);
		contentValues.put(Student.attendance, attendance);
		
		
		db.insert(tableName, null, contentValues);
		db.close();
	}
	
	public static void deleteStudent(AttendanceHelper attendhelper ,int id) {
		SQLiteDatabase db = attendhelper.getWritableDatabase();
		db.delete(tableName, Student._id + "=?",new String[] { id + "" }  );
		db.close();
	}
	
	public static Cursor getStudentName(AttendanceHelper attendhelper,int id) {
		SQLiteDatabase db = attendhelper.getReadableDatabase();
		return db.query(tableName, null, "Student.cid = ?", new String[]{id + ""},
				null, null, Student.sname + " COLLATE LOCALIZED ASC ");
		
	}
	public static Cursor getStudentGrade(AttendanceHelper attendhelper,int id,int sid) {
		SQLiteDatabase db = attendhelper.getReadableDatabase();
		return db.query(tableName, new String[] {_id, cid, grade},"Student._id = ?" , new String[]{sid + ""},"Student.cid" ,cid = "id", null);
	}
	
	public static void updateStudentGrade(AttendanceHelper attendhelper,int id,int sid,int grade) {
		SQLiteDatabase db = attendhelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(Student.grade, grade);
		db.update(tableName, contentValues, Student._id + "=" + sid + "," +Student.cid + "=" + id,null);
	    db.close();
	}
	
	public static void updateStudentAttend(AttendanceHelper attendhelper,int id,int sid,String attendance) {
		SQLiteDatabase db = attendhelper.getWritableDatabase();
		ContentValues contentValues = new ContentValues();
		contentValues.put(Student.attendance, attendance);
		db.update(tableName, contentValues, Student._id + "=" + sid + "," +Student.cid + "=" + id,null);
	    db.close();
	}

}
