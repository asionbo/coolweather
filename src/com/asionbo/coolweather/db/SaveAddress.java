package com.asionbo.coolweather.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.asionbo.coolweather.domain.AddressInfo;
import com.asionbo.coolweather.domain.AddressInfo.CityInfo;
import com.asionbo.coolweather.domain.AddressInfo.CountyInfo;
import com.asionbo.coolweather.domain.AddressInfo.ProvinceInfo;
import com.asionbo.coolweather.domain.City;
import com.asionbo.coolweather.domain.County;
import com.asionbo.coolweather.domain.Province;
import com.asionbo.coolweather.utils.MyDBUtils;
import com.google.gson.Gson;

/**
 * ========================================================================
 * 
 * author: asionbo
 * 
 * time: 2017-1-21 下午3:18:03
 * 
 * 解析Json数据保存地址到数据库
 * ========================================================================
 */
public class SaveAddress {

	private static int PROVINCE_ID = 0;
	private static int CITY_ID = 0;
	private static Iterator<ProvinceInfo> itorProvince;
	private static Iterator<CityInfo> itorCity;
	private static Iterator<CountyInfo> itorCounty;

	/** 解析Json数据 ,并保存到数据库 */
	public static void getJsonData(MyDBUtils dbUtils, String json) {
		Gson gson = new Gson();
		AddressInfo fromJson = gson.fromJson(json, AddressInfo.class);
		System.out.println("解析后：" + fromJson);

		itorProvince = fromJson.getList().iterator();

		List<Province> provinceList = new ArrayList<Province>();
		while (itorProvince.hasNext()) {
			ProvinceInfo provinceInfo = itorProvince.next();
			Province province = new Province();
			province.setProvinceName(provinceInfo.getName());
			System.out.println("province" + provinceInfo.getName());
			province.setProvinceCode(provinceInfo.getCity_id());
			province.setId(PROVINCE_ID);
			PROVINCE_ID++;
			
			provinceList.add(province);

			itorCity = provinceInfo.getList().iterator();
			List<City> cityList = new ArrayList<City>();
			while (itorCity.hasNext()) {
				CityInfo cityInfo = itorCity.next();
				City city = new City();
				city.setCityName(cityInfo.getName());
				System.out.println("city:" + cityInfo.getName());
				city.setCityCode(cityInfo.getCity_id());
				city.setProvinceId(PROVINCE_ID);
				city.setId(CITY_ID);
				CITY_ID++;

				cityList.add(city);
				

				if (cityInfo.getList() != null) {
					itorCounty = cityInfo.getList().iterator();
					List<County> countyList = new ArrayList<County>(); //定义一个County的集合
					while (itorCounty.hasNext()) {
						CountyInfo countyInfo = itorCounty.next();
						County county = new County();
						county.setCountyName(countyInfo.getName());
						county.setCountyCode(countyInfo.getCity_id());
						county.setCityId(CITY_ID);
						System.out.println("county:" + countyInfo.getName());
						countyList.add(county);

					}
					dbUtils.saveCounty(countyList);//将county数据插入数据库
				}
			}
			dbUtils.saveCity(cityList);//将city数据插入数据库
		}
		dbUtils.saveProvince(provinceList);//将province数据插入数据库
	}
}
