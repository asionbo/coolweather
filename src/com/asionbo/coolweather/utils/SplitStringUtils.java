package com.asionbo.coolweather.utils;
/**
 * ========================================================================
 * 
 * author: asionbo
 * 
 * time: 2017-2-3 下午8:16:16
 * 
 * 拆分字符串工具类
 * ========================================================================
 */
public class SplitStringUtils {

	/**
	 * 根据一个元素拆分字符串为两段
	 * @param one 拆分元素
	 * @param str 目标字符串
	 */
	public static String[] SplitStrByOne(String one,String str){
		String[] split = str.split(one);
		return split;
	}
}
