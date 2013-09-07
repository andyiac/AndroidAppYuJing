package com.example.slidingmenu.yujing.client.database.table;

import java.util.ArrayList;
import java.util.HashMap;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.slidingmenu.yujing.client.database.DatabaseHelper;

public class Topic implements DatabaseHelper.TableCreateInterface {
	// 定义表名
	public static String tableName = "Topic";
	// 定义各字段名
	public static String _id = "_id"; // _id是SQLite中自动生成的主键，用语标识唯一的记录，为了方便使用，此处定义对应字段名
	public static String UID = "UID"; // 用户id
	public static String ID = "Topic_ID"; // 话题的id
	public static String content = "Topic_Content"; // 话题内容
	public static String time = "Topic_Time"; // 话题的时间
	public static String name = "Topic_Name"; // 话题的名字
	public static String photo = "Topic_Photo"; // 话题的图片
	
	// 返回表的实例进行创建与更新
	private static Topic topic = new Topic();

	public static Topic getInstance() {
		return Topic.topic;
	}
	
	//建立数据表
	@Override
	public void onCreate(SQLiteDatabase db){

		String sql = "CREATE TABLE "
				+ Topic.tableName
				+ " (  "
				+ "_id integer primary key autoincrement, "				
				+ Topic.UID + " LONG, "
				+ Topic.ID + " LONG, "
				+ Topic.content + " TEXT, "
				+ Topic.time + " INTEGER, "
				+ Topic.name + " TEXT, "
				+ Topic.photo + " TEXT "
				+ ");";
		db.execSQL( sql );
	}

	// 更新数据表
	@Override
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {

		if ( oldVersion < newVersion ) {
			String sql = "DROP TABLE IF EXISTS " + Topic.tableName;
			db.execSQL( sql );
			this.onCreate( db );
		}
	}
	
	// 插入话题
	public static void insertTopic( DatabaseHelper dbHelper, ContentValues userValues ) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.insert( Topic.tableName, null, userValues );
		db.close();
	}	

	// 删除一条话题
	public static void deleteTopic( DatabaseHelper dbHelper, int _id ) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.delete(  Topic.tableName, Topic._id + "=?",new String[] { _id + "" }  );
		db.close();

	}
	
	// 删除所有话题
	public static void deleteAllTopic( DatabaseHelper dbHelper ) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		db.delete(  Topic.tableName, null, null  );
		db.close();
	}
	
	// 修改话题（在项目中并未用到此方法）
	public static void updateTopic( DatabaseHelper dbHelper,  int _id, ContentValues infoValues ) {

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		db.update(Topic.tableName, infoValues, Topic._id + " =? ", new String[]{ _id + "" });
		db.close();
	}
	
	// 以HashMap<String, Object>键值对的形式获取一条话题的信息
    public static HashMap<String, Object> getTopic( DatabaseHelper dbHelper, int _id ){
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		HashMap<String, Object> topicMap = new HashMap<String, Object>();
		// 此处要求查询Topic._id为传入参数_id的对应记录，使游标指向此记录
		Cursor cursor = db.query( Topic.tableName, null, Topic._id + " =? ", new String[]{ _id + "" }, null, null, null);
		cursor.moveToFirst();
		topicMap.put(Topic.ID, cursor.getLong(cursor.getColumnIndex(Topic.ID)));
		topicMap.put(Topic.content, cursor.getString(cursor.getColumnIndex(Topic.content)));
		topicMap.put(Topic.time, cursor.getInt(cursor.getColumnIndex(Topic.time)));
		topicMap.put(Topic.name, cursor.getString(cursor.getColumnIndex(Topic.name)));
		topicMap.put(Topic.photo, cursor.getString(cursor.getColumnIndex(Topic.photo)));

		return topicMap;
		
	}
    
    // 获得查询指向话题表的游标，要求以时间倒序排列
    public static Cursor getAllTopics(DatabaseHelper dbHelper) {
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		// 此处Topic.time + " DESC "作用是要求游标以时间倒序进行查询
    	Cursor cursor = db.query(Topic.tableName, null, null, null, null, null, Topic.time + " DESC ");
    	cursor.moveToFirst();
		return cursor;
    }
    
    // 返回话题总数
    public static int getCount(DatabaseHelper dbHelper) {
    	SQLiteDatabase db = dbHelper.getReadableDatabase();
    	Cursor cursor = db.query(Topic.tableName, null, null, null, null, null, Topic.time + " DESC ");
    	int count = cursor.getCount();
    	cursor.close();
    	db.close();
		return count;
    }
    
    // 获得最大的话题的_id，以便与服务器进行查询最新话题
    public synchronized static int getMaxId(DatabaseHelper dbHelper) {
		int id = 0;
		try {
			SQLiteDatabase db = dbHelper.getReadableDatabase();
			Cursor cursor = db.query(Topic.tableName, null, null, null, null, null, null);
			cursor.moveToLast();
			if (cursor.getCount() > 0) {
				id = cursor.getInt(cursor.getColumnIndex(Topic._id));
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
    }
    
    // 以ArrayList<HashMap<String, Object>>队列的形式获得所有话题的信息
   public static ArrayList<HashMap<String, Object>> getListTopic( DatabaseHelper dbHelper ){
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		
		ArrayList<HashMap<String, Object>> topicList = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> topicMap = null;
		Cursor cursor = db.query( Topic.tableName, null, null, null, null, null, Topic.time + " DESC ");
		
		for( cursor.moveToFirst(); cursor.isAfterLast(); cursor.moveToNext() ){
			topicMap = new HashMap<String, Object>();
			topicMap.put(Topic._id, cursor.getInt(cursor.getColumnIndex(Topic._id)));
			topicMap.put(Topic.time, cursor.getInt(cursor.getColumnIndex(Topic.time)));
			topicMap.put(Topic.name, cursor.getString(cursor.getColumnIndex(Topic.name)));
			topicMap.put(Topic.photo, cursor.getString(cursor.getColumnIndex(Topic.photo)));
			
			topicList.add(topicMap);
		}

		return topicList;
		
	}
   
}
