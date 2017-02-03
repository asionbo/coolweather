package com.asionbo.coolweather.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.asionbo.coolweather.R;
import com.asionbo.coolweather.domain.Weather;
import com.asionbo.coolweather.domain.Weather.WeatherInfo.WeatherDetail;
import com.asionbo.coolweather.utils.HttpCallbackListener;
import com.asionbo.coolweather.utils.HttpUtils;
import com.asionbo.coolweather.utils.SplitStringUtils;
import com.google.gson.Gson;

public class WeatherActivity extends Activity {

	private ListView lvWeather;//显示天气
	private Weather weatherData = null;
	private TextView tvDate;//日期
	private TextView tvTime;//刷新时间
	private TextView tvAddress;//地点
	private WeatherAdapter adapter;
	
	private ArrayList<WeatherDetail> weatherList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUi();
		initData();
	}

	private void initUi() {
		setContentView(R.layout.activity_weather);
		lvWeather = (ListView) findViewById(R.id.lv_weather);
		tvDate = (TextView) findViewById(R.id.tv_date);
		tvTime = (TextView) findViewById(R.id.tv_time);
		tvAddress = (TextView) findViewById(R.id.tv_address);
//		TextView tv = findViewById(R.id.)
		
		adapter = new WeatherAdapter();
	}
	
	private void initData() {
//		String address = "http://10.0.2.2:8080/weather.json";
		String cityCode = "CH180901";
		String address = "http://api.yytianqi.com/forecast7d?city="+cityCode+"&key=wnq294g9pchq2gbo";
		
		//从服务器获取天气数据
		HttpUtils.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				weatherData = phraseData(response);
				System.out.println("解析后："+weatherData);
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if(weatherData != null){
							System.out.println(weatherData.data.cityName);
							tvAddress.setText(weatherData.data.cityName);//设置地区名
							
							String[] str = SplitStringUtils.SplitStrByOne(" ",
									weatherData.data.sj);
							if(str.length > 0){
								tvDate.setText(str[0]);
								tvTime.setText(str[1]+"刷新");//设置刷新时间
							}
							
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
				convertView  = View.inflate(WeatherActivity.this, R.layout.weather_list_item, null);
				tv_date = (TextView) convertView.findViewById(R.id.tv_date);
				tv_info = (TextView) convertView.findViewById(R.id.tv_info);
				tv_temp = (TextView) convertView.findViewById(R.id.tv_temp);
			}
			WeatherDetail item = getItem(position);
			
			tv_date.setText("日期："+item.date);
			if(item.tq1.equals(item.tq2)){
				tv_info.setText("天气详情："+item.tq1);
			}else{
				tv_info.setText("天气详情："+item.tq1+"转"+item.tq2);
			}
			tv_temp.setText("最低气温："+item.qw2+"゜、最高气温："+item.qw1+"゜");
			
			return convertView;
		}
		
	}

	/**
	 * 处理json数据
	 * @param response
	 */
	protected Weather phraseData(String response) {
		Gson gson = new Gson();
		Weather fromJson = gson.fromJson(response, Weather.class);
		return fromJson;
	}
}
