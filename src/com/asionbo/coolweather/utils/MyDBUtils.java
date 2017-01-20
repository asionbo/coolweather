package com.asionbo.coolweather.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asionbo.coolweather.db.MyOpenHelper;
import com.asionbo.coolweather.domain.City;
import com.asionbo.coolweather.domain.County;
import com.asionbo.coolweather.domain.Province;

/**
 * ========================================================================
 * 
 * author: asionbo
 * 
 * time: 2017-1-20 下午4:12:48
 * 
 * 操作数据库工具类
 * ========================================================================
 */
public class MyDBUtils {
	
	/**数据库名*/
	public static final String DB_NAME = "cool_weather";
	/**数据库版本*/
	public static final int VERSION = 1;
	private SQLiteDatabase db;
	private static MyDBUtils dbUtils;

	/**
	 * 私有化的构造方法
	 */
	private MyDBUtils(Context context) {
		MyOpenHelper myOpenHelper = new MyOpenHelper(context, DB_NAME, null, VERSION);
		db = myOpenHelper.getWritableDatabase();
	}
	
	/**
	 * 获取MyDBUtils的实例，单例设计模式
	 */
	public synchronized static MyDBUtils getInstance(Context context){
		if(dbUtils == null){
			dbUtils = new MyDBUtils(context);
		}
		return dbUtils;
	}
	
	/**
	 * 将Province实例存到数据库
	 * @param province
	 */
	public void saveProvince(Province province){
		if(province != null){
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	
	/**
	 * 从数据库读取省份信息
	 * @return
	 */
	public List<Province> loadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		
		if(cursor.moveToFirst()){
			while(cursor.moveToNext()){
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id_")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				
				list.add(province);
			}
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}

	/**
	 * 将City实例存储到数据库
	 * @param city
	 */
	public void saveCity(City city){
		if(city != null){
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id",city.getProvinceId());
			db.insert("City", null, values);
		}
	}
	
	/**
	 * 从数据库读取某省下所有城市信息
	 * @return
	 */
	public List<City> loadCities(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id=?", 
				new String[]{String.valueOf(provinceId)}, null, null, null);
		
		if(cursor.moveToFirst()){
			while(cursor.moveToNext()){
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id_")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				
				list.add(city);
			}
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
	
	/**
	 * 将County实例化到数据库
	 * @param county
	 */
	public void saveCounty(County county){
		if(county != null){
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
		}
	}
	
	
	/**
	 * 从数据库读取某城市下所有县区信息
	 * @return
	 */
	public List<County> loadCounties(int cityId){
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id=?", 
				new String[]{String.valueOf(cityId)}, null, null, null);
		
		if(cursor.moveToFirst()){
			while(cursor.moveToNext()){
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id_")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cityId);
				
				list.add(county);
			}
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
	
}
