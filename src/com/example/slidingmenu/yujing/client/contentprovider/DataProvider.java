package com.example.slidingmenu.yujing.client.contentprovider;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.example.slidingmenu.yujing.client.database.DatabaseHelper;
import com.example.slidingmenu.yujing.client.database.table.Friend;
import com.example.slidingmenu.yujing.client.database.table.PrivateLetter;
import com.example.slidingmenu.yujing.client.database.table.Topic;

public class DataProvider extends ContentProvider{
	
	private DatabaseHelper dbHelper;
	// 定义UriMatcher类
    private static final UriMatcher uriMatcher;
    private static final int topic = 1;
    private static final int privateLetter = 2;
    private static final int friend = 3;
    
    public static String AUTHORITY = "com.androidbook.client.contentprovider.DataProvider";
	
	 //标识URI，定义不同的Uri来区别对不同表的操作
	public static final Uri Topic_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/Topic");
	public static final Uri PrivateLetter_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/PrivateLetter");
	public static final Uri Friend_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/Friend");

    static {
    	
		// 当没有匹配成功是时，返回NO_MATCH的值
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		
		// 匹配Topic表的Uri，匹配成功后返回topic整数值
		uriMatcher.addURI(AUTHORITY, "Topic", topic);
		
		// 匹配PrivateLetter表的Uri，匹配成功后返回privateLetter整数值
		uriMatcher.addURI(AUTHORITY, "PrivateLetter", privateLetter);
		
		// 匹配Friend表的Uri，匹配成功后返回privateLetter整数值
		uriMatcher.addURI(AUTHORITY, "Friend", friend);
    }   
    
    
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        int row = 0;
        Uri newuri = ContentUris.withAppendedId(uri, 3);
        long id = ContentUris.parseId(newuri);
		switch (uriMatcher.match(uri)) {
		case topic:
			row = db.delete(Topic.tableName, selection, selectionArgs);
			break;
		case privateLetter:
			row = db.delete(PrivateLetter.tableName, selection, selectionArgs);
			break;
		case friend:
			row = db.delete(Friend.tableName, selection, selectionArgs);
			break;
        }        
        return row;
    }
    
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
    	switch (uriMatcher.match(uri)) {
		case topic:
			db.insert(Topic.tableName, null, values);
			break;
		case privateLetter:
			db.insert(PrivateLetter.tableName, null, values);
			break;
	    case friend:
			db.insert(Friend.tableName, null, values);
			break;
	    } 
        return null;
    }

    @Override
    public boolean onCreate() {
    	this.dbHelper = new DatabaseHelper(this.getContext(), "client.db", null, 1);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
            String[] selectionArgs, String sortOrder) {
    	Cursor cursor = null;
		SQLiteDatabase db = this.dbHelper.getReadableDatabase();
		switch (uriMatcher.match(uri)) {
		case topic:
	    	cursor = db.query( Topic.tableName, projection, selection, selectionArgs, null, null, sortOrder);
	    	cursor.moveToFirst();
			break;
		case privateLetter:
	    	cursor = db.query( PrivateLetter.tableName, projection, selection, selectionArgs, null, null, sortOrder);
			cursor.moveToFirst();
			break;
		case friend:
	    	cursor = db.query( Friend.tableName, projection, selection, selectionArgs, null, null, sortOrder);
			cursor.moveToFirst();
			System.out.print("cursor----->");
			System.out.println(cursor==null);
			break;
        }
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
            String[] selectionArgs) {
    	SQLiteDatabase db = this.dbHelper.getWritableDatabase();
    	int row = 0;
    	switch (uriMatcher.match(uri)) {
		case topic:
			row = db.update(Topic.tableName, values, selection, selectionArgs);
			break;
		case privateLetter:
			row = db.update(PrivateLetter.tableName, values, selection, selectionArgs);
			break;
		case friend:
			row = db.update(Friend.tableName, values, selection, selectionArgs);
			break;
        }      
        return row;
    }

	
	

}
