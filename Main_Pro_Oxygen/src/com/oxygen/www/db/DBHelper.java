package com.oxygen.www.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	/**
	 * 数据库名称
	 */
	private static final String DATABASE_NAME = "guodong.db";
	/**
	 * 数据库版本号
	 */
	private static final int DATABASE_VERSION = 3;
	public static final String TABLE_ACTIVITIES = "activities";

	public DBHelper(Context context) {
		// CursorFactory设置为null,使用默认值
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// 数据库第一次被创建时onCreate会被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		CreateTable(db);
	}

	// 如果DATABASE_VERSION值被改为2,系统发现现有数据库版本不同,即会调用onUpgrade
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		DelallTable(db);
		CreateTable(db);
	}

	/**
	 * 创建表
	 */
	private void CreateTable(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_ACTIVITIES
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
				"type VARCHAR," +
				"sport VARCHAR," +
				"distance DOUBLE," +
				"duration INT," +
				"latitude DOUBLE," +
				"longitude DOUBLE,"+
				"address VARCHAR," +
				"start_time DATETIME,"+
				"end_time DATETIME,"+
				"route TEXT,"+
				"created_at DATETIME,"+
				"created_by INT,"+
				"title VARCHAR,"+
				"status VARCHAR,"+
				"event_id INT,"+
				"intro VARCHAR,"+
				"pace_min VARCHAR,"+
				"pace_max VARCHAR"+
 				")");
	}

	/**
	 * 删除所有表
	 */
	private void DelallTable(SQLiteDatabase db) {
		db.execSQL(" DROP TABLE "+TABLE_ACTIVITIES);
	}
}
