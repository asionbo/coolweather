package com.asionbo.coolweather.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MySharePreUtils {

	/**
	 * 将天气信息储存到sp
	 * @param context
	 * @param weather
	 */
	public static void saveSharePre(Context context,String sj,String cityId,String cityName,
			String tq1,String tq2,String qw1,String qw2,String fx1,String fx2){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		Editor edit = context.getSharedPreferences("config", Context.MODE_PRIVATE).edit();
		edit.putString("sj",sj);
		edit.putString("city_id",cityId);
		edit.putString("city_name",cityName);
		edit.putString("tq_1",tq1);//白天天气
		edit.putString("tq_2",tq2);
		edit.putString("qw_1",qw1);//白天气温
		edit.putString("qw_2",qw2);
		edit.putString("fx_1",fx1);//白天风向
		edit.putString("fx_2",fx2);
		edit.putString("current_date", sdf.format(new Date()));
		edit.commit();
	}
	
}
