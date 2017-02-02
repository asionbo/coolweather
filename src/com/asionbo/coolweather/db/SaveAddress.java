package com.asionbo.coolweather.db;

import java.util.Iterator;

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

	/** 解析Json数据 ,并保存到数据库 */
	public static void getJsonData(MyDBUtils dbUtils, String json) {
		Gson gson = new Gson();
		AddressInfo fromJson = gson.fromJson(json, AddressInfo.class);
		System.out.println("解析后：" + fromJson);

		itorProvince = fromJson.getList().iterator();

		for (ProvinceInfo provinceInfo : fromJson.getList()) {
			Province province = new Province();
			province.setProvinceName(provinceInfo.getName());
			System.out.println("province" + provinceInfo.getName());
			province.setProvinceCode(provinceInfo.getCity_id());
			province.setId(PROVINCE_ID);
			PROVINCE_ID++;
			dbUtils.saveProvince(province);
			// }

//			itorCity = itorProvince.next().getList().iterator();
			for (CityInfo cityInfo : itorProvince.next().getList()) {
				City city = new City();
				city.setCityName(cityInfo.getName());
				System.out.println("city:" + cityInfo.getName());
				city.setCityCode(cityInfo.getCity_id());
				city.setProvinceId(PROVINCE_ID);
				city.setId(CITY_ID);
				CITY_ID++;

				
				dbUtils.saveCity(city);
				// }
				// }

//				if (itorCity.next().getList() != null && itorCity.next().getList().iterator().hasNext()) {
//					for (CountyInfo countyInfo : itorCity.next().getList()) {
//						County county = new County();
//						county.setCountyName(countyInfo.getName());
//						county.setCountyCode(countyInfo.getCity_id());
//						county.setCityId(CITY_ID);
//						System.out.println("county:" + countyInfo.getName());
//
//						dbUtils.saveCounty(county);
//					}
//				}
			}
		}
	}
}
