package com.asionbo.coolweather.utils;

import com.asionbo.coolweather.domain.Weather;

public interface HttpCallbackListener {

	Weather onFinish(String response);
	
	void onError(Exception e);
	
}
