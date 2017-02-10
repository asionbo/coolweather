package com.asionbo.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelper extends SQLiteOpenHelper {

	/** Province建表语句 */
	public static final String CREATE_PROVINCE = "create table Province ("
			+ "id_ integer primary key autoincrement," + "province_name text,"
			+ "province_code text)";
	/** City建表语句 */
	public static final String CREATE_CITY = "create table City ("
			+ "id_ integer primary key autoincrement," + "city_name text,"
			+ "city_code text," + "province_id integer)";
	/** County建表语句 */
	public static final String CREATE_COUNTY = "create table County ("
			+ "id_ integer primary key autoincrement," + "county_name text,"
			+ "county_code text," + "city_id integer)";

	/** MyCity建表语句*/
	public static final String CREATE_MY_CITY = "create table MyCity (" +
			"id_ integer primary key autoincrement," +
			"mycity_name text," +
			"mycity_code text)";

	public MyOpenHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PROVINCE);// 创建Province表
		db.execSQL(CREATE_CITY);// 创建City表
		db.execSQL(CREATE_COUNTY);// 创建County表
		db.execSQL(CREATE_MY_CITY);//创建用户保存城市的表
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
