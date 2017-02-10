package com.asionbo.coolweather.activity;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

public class WeatherActivity extends AppCompatActivity implements OnClickListener,OnRefreshListener{

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
	private ImageButton drawerToggle;//侧边栏开关
	private ImageButton refresh;//刷新天气
	private DrawerLayout drawerLayout;
	private LinearLayout drawerLeft;//侧边栏布局
	private boolean drawerOpen;//侧边栏默认关闭状态false
	private LinearLayout llCity;//添加城市
	private String address;
	private String current_addressCode = "CH180901";//当前页面城市id，默认luoyang
	private String addressCode;//选中的城市id
	private long exitTime = 0;//退出时间
	private SwipeRefreshLayout mySwipe;
	private LinearLayout llContent;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_weather);
		initUi();
		initData();
	}
	

	private void initUi() {
		
		//标题控件
		drawerToggle = (ImageButton) findViewById(R.id.drawer_toggle);
		refresh = (ImageButton) findViewById(R.id.refresh);
		
		
		//内容控件
		llContent = (LinearLayout) findViewById(R.id.ll_content);
		mySwipe = (SwipeRefreshLayout) llContent.findViewById(R.id.swipe_container);//下拉刷新
		lvWeather = (ListView) findViewById(R.id.lv_weather);
		tvDate = (TextView) findViewById(R.id.tv_date);
		tvTime = (TextView) findViewById(R.id.tv_time);
		tvAddress = (TextView) findViewById(R.id.tv_address);
		tvTq = (TextView) findViewById(R.id.tv_tq);
		tvQw = (TextView) findViewById(R.id.tv_qw);
		
		
		//侧边栏
		drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
		drawerLeft = (LinearLayout) findViewById(R.id.drawer_left);
		llCity = (LinearLayout) findViewById(R.id.ll_city);
		
		
		mySwipe.setColorSchemeResources(android.R.color.holo_blue_dark);//设置颜色
		mySwipe.setDistanceToTriggerSync(40);
		mySwipe.setOnRefreshListener(this);
		
		drawerOpen = drawerLayout.isDrawerOpen(drawerLeft);//侧边栏状态
		
		//跳转添加城市activity
		llCity.setOnClickListener(this);
		//侧边栏开关
		drawerToggle.setOnClickListener(this);
		//刷新天气
		refresh.setOnClickListener(this);
		
		sp = this.getSharedPreferences("config", Context.MODE_PRIVATE);
		
		if(sp != null){
			//设置大的view显示数据
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
		
		if(adapter == null){
			adapter = new WeatherAdapter();
		}
	}
	
	private void initData() {
		
		Intent intent = getIntent();
		
		addressCode = intent.getStringExtra(getPackageName()+".code");
		if(addressCode != null){
			current_addressCode = addressCode;
//			address = "http://api.yytianqi.com/forecast7d?city="+addressCode+"&key=wnq294g9pchq2gbo";
			address = "http://10.0.3.2:8080/weather.json";
			System.out.println(addressCode);
		}else{
			address = "http://10.0.3.2:8080/weather_bad.json";
//			address = "http://api.yytianqi.com/forecast7d?city=CH180901&key=wnq294g9pchq2gbo";
		}
		
		System.out.println(address);
		//从服务器获取天气数据
		HttpUtils.sendHttpRequest(address, new HttpCallbackListener() {
			@Override
			public Weather onFinish(String response) {
				System.out.println("解析前："+response);
				weatherData = phraseData(WeatherActivity.this,response);
				System.out.println("解析后："+weatherData);
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						if(weatherData != null){
							weatherList = weatherData.data.list;
							lvWeather.setAdapter(adapter);
							initUi();
						}
					}
				});
				return null;
			}
			
			@Override
			public void onError(Exception e) {
				e.printStackTrace();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(WeatherActivity.this, "请检查网络设置", Toast.LENGTH_SHORT).show();
					}
				});
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


	//处理各控件的点击事件
	@Override
	public void onClick(View v) {
		if(v == llCity){//添加城市
			Intent intent = new Intent(this,ChooseActivity.class);
			startActivity(intent);
			Toast.makeText(WeatherActivity.this, "添加", Toast.LENGTH_SHORT).show();
		}else if(v == drawerToggle){
			if(drawerOpen){//打开状态
				//点击关闭
				drawerLayout.closeDrawer(drawerLeft);
			}else{
				//点击打开
				drawerLayout.openDrawer(drawerLeft);
			}
		}else if(v == refresh){//刷新天气
//			refreshData();
			mySwipe.setRefreshing(true);
			onRefresh();
//			Toast.makeText(WeatherActivity.this, "刷新", Toast.LENGTH_SHORT).show();
		}
	}


	
	//按两次退出程序
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			if((System.currentTimeMillis() - exitTime) > 2000){
				//间隔大于2s,提示再按一次
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
			}else{
				finish();
				System.exit(0);
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}


	/**
	 * 下拉小listview刷新
	 */
	@Override
	public void onRefresh() {
		MyAsyncTask myTask = new MyAsyncTask();
		if(current_addressCode != null){
//			String current_address = "http://api.yytianqi.com/forecast7d?city="+
//					current_addressCode+"&key=wnq294g9pchq2gbo";
			String current_address = "http://10.0.3.2:8080/luoyang.json";
			
			myTask.execute(current_address);
		}else{
			myTask.execute("");//关闭动画
			Toast.makeText(WeatherActivity.this, "请选择城市", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	
	class MyAsyncTask extends AsyncTask<String,String,Weather>{
		
		//执行一些耗时操作
		@Override
		protected Weather doInBackground(String...params) {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			if(params[0] == null){
				return null;
			}
			//请求网络数据
			HttpUtils.sendHttpRequest(params[0], new HttpCallbackListener() {
				@Override
				public Weather onFinish(String response) {
					System.out.println("刷新解析前："+response);
					weatherData = phraseData(WeatherActivity.this,response);
					System.out.println("刷新解析后："+weatherData);
					return weatherData;
				}
				@Override
				public void onError(Exception e) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(WeatherActivity.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
						}
					});
				}
			});
			return weatherData;
		}
		//刷新ui
		@Override
		protected void onPostExecute(Weather result) {
			if(result != null){
				weatherList = result.data.list;
				lvWeather.setAdapter(adapter);
				initUi();
				System.out.println("result:"+result);
			}else{
				System.out.println("空result");
			}
			mySwipe.setRefreshing(false);//关闭刷新动画
		}
	}
}
