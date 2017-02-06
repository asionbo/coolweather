package com.asionbo.coolweather.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.asionbo.coolweather.R;
import com.asionbo.coolweather.domain.Weather;
import com.asionbo.coolweather.domain.Weather.WeatherInfo.WeatherDetail;
import com.asionbo.coolweather.utils.HttpCallbackListener;
import com.asionbo.coolweather.utils.HttpUtils;
import com.asionbo.coolweather.utils.MySharePreUtils;
import com.asionbo.coolweather.utils.SplitStringUtils;
import com.google.gson.Gson;

public class WeatherActivity extends AppCompatActivity {

	private ListView lvWeather;//显示天气
	private Weather weatherData = null;
	private TextView tvDate;//日期
	private TextView tvTime;//刷新时间
	private TextView tvAddress;//地点
	private WeatherAdapter adapter;
	
	private ArrayList<WeatherDetail> weatherList;
	private SharedPreferences sp;
	private TextView tvTq;
	private TextView tvQw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_weather);
		initUi();
		initData();
	}
	

	private void initUi() {
		
		lvWeather = (ListView) findViewById(R.id.lv_weather);
		tvDate = (TextView) findViewById(R.id.tv_date);
		tvTime = (TextView) findViewById(R.id.tv_time);
		tvAddress = (TextView) findViewById(R.id.tv_address);
		tvTq = (TextView) findViewById(R.id.tv_tq);
		tvQw = (TextView) findViewById(R.id.tv_qw);
		
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer);
		
		sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);
		
		if(sp != null){
			tvAddress.setText(sp.getString("city_name", ""));
			String[] str = SplitStringUtils.SplitStrByOne(" ",
					sp.getString("sj", ""));
			System.out.println(sp.getString("sj", ""));
			if(str.length > 1){
				tvDate.setText(str[0]);
				tvTime.setText(str[1]+"发布");//设置刷新时间
			}
			if(sp.getString("tq_1", "").equals(sp.getString("tq_2", ""))){
				tvTq.setText(sp.getString("tq_1", ""));
			}else{
				tvTq.setText(sp.getString("tq_1", "")+"转"+sp.getString("tq_2", ""));
			}
			tvQw.setText(sp.getString("qw_2", "")+"゜~  "+sp.getString("qw_1", "")+"゜");
		}
		
		adapter = new WeatherAdapter();
		
	}
	
	private void initData() {
		String address = "http://10.0.3.2:8080/weather.json";
		String cityCode = "CH180901";
//		String address = "http://api.yytianqi.com/forecast7d?city="+cityCode+"&key=wnq294g9pchq2gbo";
		
		//从服务器获取天气数据
		HttpUtils.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				weatherData = phraseData(WeatherActivity.this,response);
				System.out.println("解析后："+weatherData);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if(weatherData != null){
//							System.out.println(weatherData.data.cityName);
//							tvAddress.setText(weatherData.data.cityName);//设置地区名
//							
//							String[] str = SplitStringUtils.SplitStrByOne(" ",
//									weatherData.data.sj);
//							if(str.length > 0){
//								tvDate.setText(str[0]);
//								tvTime.setText(str[1]+"刷新");//设置刷新时间
//							}
							
							weatherList = weatherData.data.list;
							lvWeather.setAdapter(adapter);
						}
					}
				});
			}
			
			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}
		});
	}
	
	class WeatherAdapter extends BaseAdapter{

		private TextView tv_date;//小条目日期
		private TextView tv_info;//详情
		private TextView tv_temp;//温度

		public WeatherAdapter() {
			super();
		}

		@Override
		public int getCount() {
			return weatherList.size();
		}

		@Override
		public WeatherDetail getItem(int position) {
			return weatherList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = View.inflate(WeatherActivity.this, R.layout.weather_list_item, null);
				tv_date = (TextView) convertView.findViewById(R.id.tv_date);
				tv_info = (TextView) convertView.findViewById(R.id.tv_info);
				tv_temp = (TextView) convertView.findViewById(R.id.tv_temp);
			}
			WeatherDetail item = getItem(position);
			
			tv_date.setText(item.date);
			if(item.tq1.equals(item.tq2)){
				tv_info.setText(item.tq1);
			}else{
				tv_info.setText(item.tq1+"转"+item.tq2);
			}
			tv_temp.setText("最低气温："+item.qw2+"゜、最高气温："+item.qw1+"゜");
			
//			if(sp != null){
//				if(sp.getString("tq_1", "").equals(sp.getString("tq_2", ""))){
//					tv_info.setText(sp.getString("tq_1", ""));
//				}else{
//					tv_info.setText(sp.getString("tq_1", "")+"转"+sp.getString("tq_2", ""));
//				}
//				tv_temp.setText(sp.getString("qw_2", "")+"゜~"+sp.getString("qw_1", "")+"゜");
//			}
			return convertView;
		}
		
	}

	/**
	 * 处理json数据
	 * @param response
	 * @return 
	 */
	protected Weather phraseData(Context context,String response) {
		Gson gson = new Gson();
		Weather fromJson = gson.fromJson(response, Weather.class);
		
		String cityName = fromJson.data.cityName;
		String cityId = fromJson.data.cityId;
		String sj = fromJson.data.sj;
//		String date = fromJson.data.list.get(0).date;
		String fx1 = fromJson.data.list.get(0).fx1;
		String fx2 = fromJson.data.list.get(0).fx2;
		String qw1 = fromJson.data.list.get(0).qw1;
		String qw2 = fromJson.data.list.get(0).qw2;
		String tq1 = fromJson.data.list.get(0).tq1;
		String tq2 = fromJson.data.list.get(0).tq2;
		
		System.out.println("test-----"+sj+fx1+tq2+cityName);
		
		MySharePreUtils.saveSharePre(context, sj, cityId, cityName,
				tq1, tq2, qw1, qw2, fx1, fx2);
		
		return fromJson;
	}
}
