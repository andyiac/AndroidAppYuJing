package com.example.slidingmenu.yujing.client.database.table;

import java.util.ArrayList;
import java.util.HashMap;



import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.slidingmenu.yujing.client.database.DatabaseHelper;

public class Friend implements DatabaseHelper.TableCreateInterface {
	public static String tableName = "Friend";
	public static String _id = "_id";
	public static String UID = "UID";
	public static String ID = "Friend_ID";
	public static String name = "Friend_Name";
	public static String mobile = "Friend_Mobile";
	public static String photo = "Friend_Photo";
	public static String sex = "Friend_Sex";
	public static String address = "Friend_Address";
	public static String state = "Friend_State";
	
	public static final int ON_LINE = 0;
	public static final int OFF_LINE = -1;

	
	private static Friend friend = new Friend();

	public static Friend getInstance() {
		return Friend.friend;
	}

	@Override
	public void onCreate( SQLiteDatabase db ) {

		String sql = "CREATE TABLE "
				+ Friend.tableName
				+ " (  "
				+ "_id integer primary key autoincrement, "				
				+ Friend.UID + " LONG, "
				+ Friend.ID + " LONG, "
				+ Friend.name + " TEXT, "
				+ Friend.mobile + " TEXT, "
				+ Friend.photo + " TEXT, "
				+ Friend.address + " TEXT, "
				+ Friend.sex + " TEXT, "
				+ Friend.state + " INTEGER "
				+ ");";
		db.execSQL( sql );
	}

	@Override
	public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {

		if ( oldVersion < newVersion ) {
			String sql = "DROP TABLE IF EXISTS " + Friend.tableName;
			db.execSQL( sql );
			this.onCreate( db );
		}
	}
	
	public static void insertFriend( DatabaseHelper dbHepler, ContentValues userValues ) {
		String whereArgs = String .valueOf(userValues.getAsLong(Friend.ID));
		if(!IsFriend(dbHepler, whereArgs)) {
			SQLiteDatabase db = dbHepler.getWritableDatabase();
			db.insert(Friend.tableName, null, userValues);
			db.close();
		}
	}	

	public static void deleteFriend( DatabaseHelper dbHepler,
			int _id ) {

		SQLiteDatabase db = dbHepler.getWritableDatabase();
		db.delete(  Friend.tableName, Friend._id + "=?",new String[] { _id + "" }  );
		db.close();

	}
	
	public static void deleteAllFriend( DatabaseHelper dbHepler ) {

		SQLiteDatabase db = dbHepler.getWritableDatabase();
		db.delete(  Friend.tableName, null, null  );
		db.close();
	} 

	public static void updataFriendState( DatabaseHelper dbHepler, ContentValues values, long id ) {

		SQLiteDatabase db = dbHepler.getWritableDatabase();
		db.update(Friend.tableName, values, Friend.ID + " = " + id, null);
		db.close();
	} 
	
	public static Cursor getAllFriends(DatabaseHelper dbHepler, String user) {
		Cursor c = null;
		try {
			SQLiteDatabase db = dbHepler.getReadableDatabase();
			c = db.query(Friend.tableName, null, Friend.UID
					+ "="
					+ user, null, null, null,
					Friend.name + " COLLATE LOCALIZED ASC ");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return c;
	}
	
    public static HashMap<String, Object> getFriend(DatabaseHelper dbHepler, int _id ){
		
		SQLiteDatabase db = dbHepler.getReadableDatabase();
		
		HashMap<String, Object> friendMap = new HashMap<String, Object>();
		Cursor cursor = db.query( Friend.tableName, null, Friend._id + " =? ", new String[]{ _id + "" }, null, null, null);
		if(cursor == null || cursor.getCount() == 0) {
			return null;
		}
		friendMap.put(Friend.ID, cursor.getLong(cursor.getColumnIndex(Friend.ID)));
		friendMap.put(Friend.name, cursor.getString(cursor.getColumnIndex(Friend.name)));
		friendMap.put(Friend.photo, cursor.getString(cursor.getColumnIndex(Friend.photo)));
		friendMap.put(Friend.sex, cursor.getString(cursor.getColumnIndex(Friend.sex)));
		friendMap.put(Friend.mobile, cursor.getString(cursor.getColumnIndex(Friend.mobile)));
		friendMap.put(Friend.address, cursor.getString(cursor.getColumnIndex(Friend.address)));
		friendMap.put(Friend.state, cursor.getInt(cursor.getColumnIndex(Friend.state)));

		return friendMap;
		
	}
    
    public static boolean IsFriend(DatabaseHelper dbHepler, String uid) {
    	
    	boolean retVal = true;
    	SQLiteDatabase db = dbHepler.getWritableDatabase();
		Cursor c = db.query(Friend.tableName, null, Friend.ID + "=?",
				new String[] {uid}, null, null, null);
		if(c == null || c.getCount() == 0) {
			retVal = false;
		}
		c.close();
		db.close();
    	
		return retVal;
    	
    }
    
    public static ArrayList<HashMap<String, Object>> getListFriend( DatabaseHelper dbHepler ){
		
		SQLiteDatabase db = dbHepler.getReadableDatabase();
		
		ArrayList<HashMap<String, Object>> FriendList = new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> friendMap = null;
		Cursor cursor = db.query( Friend.tableName, null, null, null, null, null, Friend.name + " ASC ");
		
		for( cursor.moveToFirst(); cursor.isAfterLast(); cursor.moveToNext() ){
			friendMap = new HashMap<String, Object>();
			friendMap.put(Friend._id, cursor.getLong(cursor.getColumnIndex(Friend._id)));
			friendMap.put(Friend.name, cursor.getString(cursor.getColumnIndex(Friend.name)));
			friendMap.put(Friend.photo, cursor.getString(cursor.getColumnIndex(Friend.photo)));
			friendMap.put(Friend.mobile, cursor.getString(cursor.getColumnIndex(Friend.mobile)));
			friendMap.put(Friend.state, cursor.getInt(cursor.getColumnIndex(Friend.state)));

			FriendList.add(friendMap);
		}

		return FriendList;
		
	}

}
