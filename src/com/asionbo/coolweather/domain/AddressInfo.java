package com.asionbo.coolweather.domain;

import java.util.ArrayList;

public class AddressInfo {

	/** 国代码 */
	public String city_id;
	/** 国名称 */
	public String name;

	/** 各省列表 */
	public ArrayList<ProvinceInfo> list;
	

	@Override
	public String toString() {
		return "AddressInfo [name=" + name + ", list=" + list + "]";
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		this.city_id = city_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<ProvinceInfo> getList() {
		return list;
	}

	public void setList(ArrayList<ProvinceInfo> list) {
		this.list = list;
	}

	public static class ProvinceInfo {
		/** 省代码 */
		public String city_id;
		/** 省名称 */
		public String name;

		/** 各市列表 */
		public ArrayList<CityInfo> list;
		
		

		@Override
		public String toString() {
			return "ProvinceInfo [name=" + name + ", list=" + list + "]";
		}

		public String getCity_id() {
			return city_id;
		}

		public void setCity_id(String city_id) {
			this.city_id = city_id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public ArrayList<CityInfo> getList() {
			return list;
		}

		public void setList(ArrayList<CityInfo> list) {
			this.list = list;
		}
		
		
	}

	public static class CityInfo {
		/** 市代码 */
		public String city_id;
		/** 市名称 */
		public String name;
		/** 各县信息 */
		public ArrayList<CountyInfo> list;
		
		
		@Override
		public String toString() {
			return "CityInfo [name=" + name + ", list=" + list + "]";
		}
		public String getCity_id() {
			return city_id;
		}
		public void setCity_id(String city_id) {
			this.city_id = city_id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public ArrayList<CountyInfo> getList() {
			return list;
		}
		public void setList(ArrayList<CountyInfo> list) {
			this.list = list;
		}
		
		
	}

	public static class CountyInfo {
		/** 县代码 */
		public String city_id;
		/** 县名称 */
		public String name;
		
		
		@Override
		public String toString() {
			return "CountyInfo [city_id=" + city_id + ", name=" + name + "]";
		}
		public String getCity_id() {
			return city_id;
		}
		public void setCity_id(String city_id) {
			this.city_id = city_id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		
		
	}
}
