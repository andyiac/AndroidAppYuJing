package com.example.slidingmenu.yujing.client.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.slidingmenu.yujing.client.database.table.Friend;
import com.example.slidingmenu.yujing.client.database.table.PrivateLetter;
import com.example.slidingmenu.yujing.client.database.table.Topic;

/**
 * SQLiteOpenHelper是SQLite的数据库辅助类，SQliteOpenHelper是一个抽象类，用来管理数据库的创建和版本的管理
 *
 */
public class DatabaseHelper extends SQLiteOpenHelper{

	/**
	 * 构造方法，创建数据库
	 * @param context 
	 * @param name 数据库名
	 * @param factory 游标类
	 * @param version 数据库版本
	 */
	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
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
		Topic.getInstance().onCreate(db);
		PrivateLetter.getInstance().onCreate(db);
		Friend.getInstance().onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Topic.getInstance().onUpgrade(db, oldVersion, newVersion);
		PrivateLetter.getInstance().onUpgrade(db, oldVersion, newVersion);
		Friend.getInstance().onUpgrade(db, oldVersion, newVersion);
	}

}
