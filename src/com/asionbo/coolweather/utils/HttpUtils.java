package com.asionbo.coolweather.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * ========================================================================
 * 
 * author: asionbo
 * 
 * time: 2017-1-21 下午2:33:34
 * 
 * 获取服务器数据工具类
 * ========================================================================
 */
public class HttpUtils {

	/**
	 * 连接服务器
	 */
	public static void sendHttpRequest(final String address,final HttpCallbackListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				HttpURLConnection connection = null;
				try {
					URL url = new URL(address);
					connection = (HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					connection.setReadTimeout(5000);

					InputStream in = connection.getInputStream();
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(in));
					
					StringBuilder response = new StringBuilder();
					String line;
					while((line = reader.readLine()) != null){
						response.append(line);
					}
					if(listener != null){
						System.out.println("成功获取服务器数据");
						listener.onFinish(response.toString());//回调onFinish()方法,返回服务器数据
					}
				} catch (Exception e) {
					System.out.println("回调错误信息");
					e.printStackTrace();
					if(listener != null){
						listener.onError(e);//回调onError()方法
					}
				} finally{
					if(connection != null){
						connection.disconnect();
					}
				}
			}
		}).start();
	}
}
