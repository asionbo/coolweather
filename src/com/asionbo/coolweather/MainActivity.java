package com.asionbo.coolweather;

import android.app.Activity;
import android.os.Bundle;

import com.asionbo.coolweather.db.SaveAddress;
import com.asionbo.coolweather.utils.HttpCallbackListener;
import com.asionbo.coolweather.utils.HttpUtils;
import com.asionbo.coolweather.utils.MyDBUtils;


public class MainActivity extends Activity {

    private MyDBUtils dbUtils;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        String address = "http://api.yytianqi.com/citylist/id/2";
        
        dbUtils = MyDBUtils.getInstance(getApplicationContext());
        HttpUtils.sendHttpRequest(address, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				System.out.println("解析前："+response);
				SaveAddress.getJsonData(dbUtils, response);
			}
			
			@Override
			public void onError(Exception e) {
				System.out.println("失败");
			}
		});
    }
    
    

}
