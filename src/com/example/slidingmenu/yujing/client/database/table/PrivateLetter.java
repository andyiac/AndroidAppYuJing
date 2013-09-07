package com.example.slidingmenu.yujing.client.database.table;

import java.util.ArrayList;
import java.util.HashMap;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.slidingmenu.yujing.client.database.DatabaseHelper;


public class PrivateLetter implements DatabaseHelper.TableCreateInterface {
	public static String tableName = "PrivateLetter";
	public static String _id = "_id";
	public static String UID = "UID";
	public static String PrivateLetterUID = "PrivateLetter_UID";
	public static String PrivateLetterID = "PrivateLetter_ID";
	public static String content = "PrivateLetter_Content";
	public static String time = "PrivateLetter_Time";
	public static String name = "PrivateLetter_Name";
	public static String photo = "PrivateLetter_Photo";
	public static String isSend = "PrivateLetter_isSend";

	private static PrivateLetter privateLetter = new PrivateLetter();

	public static PrivateLetter getInstance() {
		return PrivateLetter.privateLetter;
	}

	@Override
	public void onCreate( SQLiteDatabase db ) {

		String sql = "CREATE TABLE "
				+ PrivateLetter.tableName
				+ " (  "
				+ "_id integer primary key autoincrement, "				
				+ PrivateLetter.UID + " LONG, "
				+ PrivateLetter.PrivateLetterUID + " LONG, "
				+ PrivateLetter.PrivateLetterID + " LONG, "
				+ PrivateLetter.content + " TEXT, "	
				+ PrivateLetter.time + " INTEGER, "				
				+ PrivateLetter.name + " TEXT, "
				+ PrivateLetter.photo + " TEXT, "
				+ PrivateLetter.isSend + " BOOLEAN "
				+ ");";
		db.execSQL( sql );
	}

	@Override
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {

		if ( oldVersion < newVersion ) {
			String sql = "DROP TABLE IF EXISTS " + PrivateLetter.tableName;
			db.execSQL( sql );
			this.onCreate( db );
		}
	}
	
	public static void insertPrivateLetter(DatabaseHelper dbHepler, ContentValues userValues ) {

		String ID = String.valueOf(userValues.getAsLong(PrivateLetter.PrivateLetterID));
		SQLiteDatabase db = dbHepler.getWritableDatabase();
		Cursor c = db.query(PrivateLetter.tableName, null, 
				PrivateLetterID+"=?", new String[] {ID}, null, null, null);
		if(c == null || c.getCount() == 0) {
			db.insert( PrivateLetter.tableName, null, userValues );
		}
		db.close();
	}		
	
	public static Cursor getAllLetter(DatabaseHelper dbHepler) {
		SQLiteDatabase db = dbHepler.getReadableDatabase();
		Cursor c = db.query(PrivateLetter.tableName, 
				null, 
				null, 
				null, 
				null, 
				null, 
				PrivateLetter.time + " COLLATE LOCALIZED DESC");
		return c;
	}
	
	public static int getLetterCount(DatabaseHelper dbHepler) {
		
		SQLiteDatabase db = dbHepler.getReadableDatabase();
		Cursor c = db.query(PrivateLetter.tableName, 
				null, 
				null, 
				null, 
				null, 
				null, 
				PrivateLetter.time + " COLLATE LOCALIZED DESC");
		int count = c.getCount();
		c.close();
		db.close();
		return count;
		
	}
	
	public static void deletePrivateLetter( DatabaseHelper dbHepler,
			int _id ) {

		SQLiteDatabase db = dbHepler.getWritableDatabase();
		db.delete(  PrivateLetter.tableName, PrivateLetter._id + "=?",new String[] { _id + "" }  );
		db.close();

	}
	
	public static void deleteAllPrivateLetter( DatabaseHelper dbHepler ) {

		SQLiteDatabase db = dbHepler.getWritableDatabase();
		db.delete(  PrivateLetter.tableName, null, null  );
		db.close();
	} 

    public static HashMap<String, Object> getPrivateLetter( DatabaseHelper dbHepler, int _id ){
		
		SQLiteDatabase db = dbHepler.getReadableDatabase();
		
		HashMap<String, Object> privateLetterMap = new HashMap<String, Object>();
		Cursor cursor = db.query( PrivateLetter.tableName, null, PrivateLetter._id + " =? ", new String[]{ _id + "" }, null, null, null);
		cursor.moveToFirst();
		privateLetterMap.put(PrivateLetter.PrivateLetterID, cursor.getLong(cursor.getColumnIndex(PrivateLetter.PrivateLetterID)));
		privateLetterMap.put(PrivateLetter.PrivateLetterUID, cursor.getLong(cursor.getColumnIndex(PrivateLetter.PrivateLetterUID)));
		privateLetterMap.put(PrivateLetter.content, cursor.getString(cursor.getColumnIndex(PrivateLetter.content)));
		privateLetterMap.put(PrivateLetter.time, cursor.getInt(cursor.getColumnIndex(PrivateLetter.time)));
		privateLetterMap.put(PrivateLetter.name, cursor.getString(cursor.getColumnIndex(PrivateLetter.name)));
		privateLetterMap.put(PrivateLetter.photo, cursor.getString(cursor.getColumnIndex(PrivateLetter.photo)));
		privateLetterMap.put(PrivateLetter.isSend, cursor.getInt(cursor.getColumnIndex(PrivateLetter.photo)) == 0? false : true);

		return privateLetterMap;
		
	}
    
   public static ArrayList<HashMap<String, Object>> getListPrivateLetter( DatabaseHelper dbHepler ){
		
		SQLiteDatabase db = dbHepler.getReadableDatabase();
		
		ArrayList<HashMap<String, Object>> PrivateLetterList = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> privateLetterMap = null;
		Cursor cursor = db.query( PrivateLetter.tableName, null, null, null, null, null, PrivateLetter.time + " DESC ");
		
		for( cursor.moveToFirst(); cursor.isAfterLast(); cursor.moveToNext() ){
			privateLetterMap = new HashMap<String, Object>();
			privateLetterMap.put(PrivateLetter._id, cursor.getInt(cursor.getColumnIndex(PrivateLetter._id)));
			privateLetterMap.put(PrivateLetter.time, cursor.getInt(cursor.getColumnIndex(PrivateLetter.time)));
			privateLetterMap.put(PrivateLetter.name, cursor.getString(cursor.getColumnIndex(PrivateLetter.name)));
			privateLetterMap.put(PrivateLetter.photo, cursor.getString(cursor.getColumnIndex(PrivateLetter.photo)));
			privateLetterMap.put(PrivateLetter.isSend, cursor.getInt(cursor.getColumnIndex(PrivateLetter.photo)) == 0? false : true);
			privateLetterMap.put(PrivateLetter.PrivateLetterUID, cursor.getLong(cursor.getColumnIndex(PrivateLetter.PrivateLetterUID)));
			PrivateLetterList.add(privateLetterMap);
		}

		return PrivateLetterList;
		
	}

	

}
