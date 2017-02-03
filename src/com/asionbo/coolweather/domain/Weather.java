package com.asionbo.coolweather.domain;

import java.util.ArrayList;

public class Weather {
	public int counts;
	public WeatherInfo data;
	

	@Override
	public String toString() {
		return "Weather [counts=" + counts + ", data=" + data + "]";
	}

	public static class WeatherInfo{
		public String cityId;
		public String cityName;
		public String sj;//数据更新时间
		
		public ArrayList<WeatherDetail> list;
		
		@Override
		public String toString() {
			return "WeatherInfo [cityName=" + cityName + ", sj=" + sj
					+ ", list=" + list + "]";
		}

		public static class WeatherDetail{
			/**日期*/
			public String date;
			/**白天风力*/
			public String fl1;
			/**夜晚风力*/
			public String fl2;
			/**白天风向*/
			public String fx1;
			/**夜晚风向*/
			public String fx2;
			/**白天风力代码*/
			public String numfl1;
			/**夜晚风力代码*/
			public String numfl2;
			/**白天风向代码*/
			public String numfx1;
			/**夜晚风向代码*/
			public String numfx2;
			/**白天天气代码*/
			public String numtq1;
			/**夜间天气代码*/
			public String numtq2;
			/**白天气温*/
			public String qw1;
			/**夜间气温*/
			public String qw2;
			/**白天天气*/
			public String tq1;
			/**夜间天气*/
			public String tq2;
			@Override
			public String toString() {
				return "WeatherDetail [date=" + date + ", fl1=" + fl1
						+ ", fl2=" + fl2 + ", fx1=" + fx1 + ", fx2=" + fx2
						+ ", qw1=" + qw1 + ", qw2=" + qw2 + ", tq1=" + tq1
						+ ", tq2=" + tq2 + "]";
			}
			
			
		}
		
	}
}
