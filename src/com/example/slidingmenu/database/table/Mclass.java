package com.example.slidingmenu.database.table;

import com.example.slidingmenu.database.AttendanceHelper;
import com.example.slidingmenu.database.AttendanceHelper.TableCreateInterface;
import com.example.slidingmenu.yujing.client.database.DatabaseHelper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


public class Mclass implements TableCreateInterface {
	
	private static String tableName = "class";
	private static String _id ="_id";
	private static String cname = "cname";
	
	private static Mclass myClass = new Mclass();
	
	public static Mclass getInstance() {
		
		return Mclass.myClass;
	}
	
	//建立数据表
		@Override
		public void onCreate(SQLiteDatabase db){

			String sql = "CREATE TABLE "
					+ Mclass.tableName
					+ " (  "
					+ Mclass._id + " integer primary key,"				
					+ Mclass.cname + " TEXT)";
			db.execSQL( sql );
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			if ( oldVersion < newVersion ) {
				String sql = "DROP TABLE IF EXISTS " + Mclass.tableName;
				db.execSQL( sql );
				this.onCreate( db );
			}
		}
		/**
		 * 插入班级
		 * @param attendhelper
		 * @param name  班级
		 */
		public static void insertClass(AttendanceHelper attendhelper ,String name) {
			SQLiteDatabase db = attendhelper.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put(cname, name);
			db.insert(tableName, null, contentValues);
			db.close();
		}
		/**
		 * 删除班级
		 * @param attendhelper
		 * @param id
		 */
		public static void deleteClass(AttendanceHelper attendhelper ,int id) {
			SQLiteDatabase db = attendhelper.getWritableDatabase();
			db.delete(tableName, Mclass._id + "=?",new String[] { id + "" }  );
			db.close();
		}
		
		
		/**
		 * 更新班级
		 * @param attendhelper
		 * @param name
		 * @param id
		 */
		public static void updateClass(AttendanceHelper attendhelper, String name,int id) {
			SQLiteDatabase db = attendhelper.getWritableDatabase();
			ContentValues contentValues = new ContentValues();
			contentValues.put(Mclass.cname, name );
			db.update(tableName, contentValues,"_id = ?", new String[]{id+""});
		}
		/**
		 * 获得全部班级
		 * @param attendhelper
		 * @return
		 */
		public static Cursor getAllClassName(AttendanceHelper attendhelper) {
			SQLiteDatabase db = attendhelper.getReadableDatabase();
			return db.query(tableName, new String[] {_id,cname }, null, null,
					null, null, null);
		}
		
		/**
		 * 获得相应的班级
		 * @param attendhelper
		 * @param id
		 * @return
		 */
		public static String getClassName(AttendanceHelper attendhelper,int id) {
			SQLiteDatabase db = attendhelper.getReadableDatabase();
			Cursor cursor = db.query(tableName, new String[] {_id,cname }, "_id = ?", new String[]{id+""},
					null, null, null);
			cursor.moveToFirst();
			String name =cursor.getString(cursor.getColumnIndex(Mclass.cname));
			return name;
		}

}
