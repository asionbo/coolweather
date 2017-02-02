package com.asionbo.coolweather.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.asionbo.coolweather.R;
import com.asionbo.coolweather.db.SaveAddress;
import com.asionbo.coolweather.domain.City;
import com.asionbo.coolweather.domain.County;
import com.asionbo.coolweather.domain.Province;
import com.asionbo.coolweather.utils.HttpCallbackListener;
import com.asionbo.coolweather.utils.HttpUtils;
import com.asionbo.coolweather.utils.MyDBUtils;

public class ChooseActivity extends Activity{

	private ListView listView;
	private TextView tvTitle;
	private List<String> dataList = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private MyDBUtils dbUtils;
	private List<Province> provinceList;
	
	private int LEVEL_PROVINCE = 0;
	private int LEVEL_CITY = 1;
	private int LEVEL_COUNTY = 2;
	private int currentLevel;
	
	/**选中的省份*/
	protected Province selectedProvince;
	/**选中的城市*/
	protected City selectedCity;
	/**选中的县*/
	protected County selectedCounty;
	private List<City> cityList;
	private List<County> countyList;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_choose);
		
		listView = (ListView) findViewById(R.id.list_view);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				dataList);
		
		listView.setAdapter(adapter);
		dbUtils = MyDBUtils.getInstance(this);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(currentLevel == LEVEL_PROVINCE){
					selectedProvince = provinceList.get(position);
					queryCities();
				}else if(currentLevel == LEVEL_CITY){
					selectedCity = cityList.get(position);
					queryCounties();
				}
			}
			
		});
		queryProvinces();//加载省级数据
	}

	

	/**
	 * 查询省级数据，优先从数据库查询，如果没有，再从服务器查询
	 */
	private void queryProvinces() {
		provinceList = dbUtils.loadProvinces();
		if(provinceList.size() > 0){
			//说明数据库有数据
			dataList.clear();//清空这个要显示的列表
			for (Province province : provinceList) {//遍历省信息
				dataList.add(province.getProvinceName());
			}
			adapter.notifyDataSetChanged();//刷新界面
			listView.setSelection(0);
			tvTitle.setText("中国");
			
			currentLevel = LEVEL_PROVINCE;//设置当前级别为省级
		}else{
			//从网络取数据
			queryFromServer(null,"province");
		}
	}
	
	/**
	 * 查询选中省内所有的市，优先从数据库查询，如果没有，再从服务器查询
	 */
	private void queryCities(){
		cityList = dbUtils.loadCities(selectedProvince.getId());
		if(cityList.size() > 0){
			dataList.clear();
			for (City city : cityList) {
				dataList.add(city.getCityName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			tvTitle.setText(selectedProvince.getProvinceName());
			currentLevel = LEVEL_CITY;
		}else{
			queryFromServer(selectedProvince.getProvinceCode(), "city");
		}
	}

	/**
	 * 查询选中市内所有的县，优先从数据库查询，如果没有，再从服务器查询
	 */
	private void queryCounties() {
		countyList = dbUtils.loadCounties(selectedCity.getId());
		if(countyList.size() > 0){
			dataList.clear();
			for (County county : countyList) {
				dataList.add(county.getCountyName());
			}
			adapter.notifyDataSetChanged();
			listView.setSelection(0);
			tvTitle.setText(selectedCity.getCityName());
			currentLevel = LEVEL_COUNTY ;
		}else{
			queryFromServer(selectedCity.getCityCode(), "county");
		}
	}
	
	/**
	 * 根据传入的代号和类型从服务器上查询省市县数据
	 */
	private void queryFromServer(String code, String type) {
		String address = "http://api.yytianqi.com/citylist/id/2";
		System.out.println("选中的地区："+code+"-------------"+type);
		HttpUtils.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				SaveAddress.getJsonData(dbUtils, response);
				queryProvinces();
			}
			
			@Override
			public void onError(Exception e) {
				System.out.println("刷新试试");
			}
		});
	}
	
	/**
	 * 重写back方法，根据当前等级判断退出方式
	 */
	public void onBackPressed(){
		if(currentLevel == LEVEL_COUNTY){
			queryCities();
		}else if(currentLevel == LEVEL_CITY){
			queryProvinces();
		}else{
			finish();
		}
	}
}