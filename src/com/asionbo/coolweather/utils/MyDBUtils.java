package com.asionbo.coolweather.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.asionbo.coolweather.db.MyOpenHelper;
import com.asionbo.coolweather.domain.City;
import com.asionbo.coolweather.domain.County;
import com.asionbo.coolweather.domain.MyCity;
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
	 * @param provinceList
	 */
	public void saveProvince(List<Province> provinceList){
		if(provinceList != null){
			db.beginTransaction();
			try {
				for (Province province : provinceList) {
					ContentValues values = new ContentValues();
					values.put("province_name", province.getProvinceName());
					values.put("province_code", province.getProvinceCode());
					db.insert("Province", null, values);
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				db.endTransaction();
			}
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
			do{
				Province province = new Province();
				province.setId(cursor.getInt(cursor.getColumnIndex("id_")));
				province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				
				list.add(province);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}

	/**
	 * 将City实例存储到数据库
	 * @param cityList
	 */
	public void saveCity(List<City> cityList){
		if(cityList != null){
			db.beginTransaction();
			try {
				for (City city : cityList) {
					ContentValues values = new ContentValues();
					values.put("city_name", city.getCityName());
					values.put("city_code", city.getCityCode());
					values.put("province_id",city.getProvinceId());
					db.insert("City", null, values);
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			} finally{
				db.endTransaction();
			}
			
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
			do{
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id_")));
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(provinceId);
				
				list.add(city);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
	
	/**
	 * 批量将County实例化到数据库
	 * @param countyList
	 */
	public void saveCounty(List<County> countyList){
		
		if(countyList != null){
			db.beginTransaction();//开启一个事务
			try {
				for(County county : countyList){
					ContentValues values = new ContentValues();
					values.put("county_name", county.getCountyName());
					values.put("county_code", county.getCountyCode());
					values.put("city_id", county.getCityId());
					db.insert("County", null, values);
				}
				db.setTransactionSuccessful();
			} catch (Exception e) {
				
				e.printStackTrace();
			} finally{
				db.endTransaction();
			}
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
			do{
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id_")));
				county.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				county.setCityId(cityId);
				
				list.add(county);
			}while(cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		return list;
	}
	
	/**
	 * 保存选中的地区到数据库里
	 * @param myCityName	地区名称
	 * @param myCityCode	地区代码
	 */
	public void saveMyCity(String myCityName,String myCityCode){
		ContentValues values = new ContentValues();
		values.put("mycity_name", myCityName);
		values.put("mycity_code", myCityCode);
		
		db.insert("MyCity", null, values);
	}
	
	
	/**
	 * 从数据库查询，用户保存的地区集合
	 * @return	List<MyCity>
	 */
	public List<MyCity> loadMyCity(){
		List<MyCity> myCityList = new ArrayList<MyCity>();
		Cursor cursor = db.query("MyCity", null, null, null, null, null, null);
		if(cursor.moveToFirst()){
			do {
				MyCity myCity = new MyCity();
				myCity.setId(cursor.getInt(cursor.getColumnIndex("id_")));
				myCity.setName(cursor.getString(cursor.getColumnIndex("mycity_name")));
				myCity.setName(cursor.getString(cursor.getColumnIndex("mycity_code")));
				
				myCityList.add(myCity);
			} while (cursor.moveToNext());
		}
		if(cursor != null){
			cursor.close();
		}
		
		return myCityList;
	}
	
	/**
	 * 删除用户保存的城市
	 * @param code
	 */
	public void deleteMyCity(String code){
		String[] whereArgs = {code};
		db.delete("MyCity", "mycity_code=?", whereArgs);
	}
	
	/**
	 * 删除表
	 */
	public void dropTable(String tableName){
		String sql = "drop table if exists"+tableName;
		db.execSQL(sql);
	}
}
