package com.asionbo.coolweather.utils;

import android.content.Context;

/**
 * ========================================================================
 * 
 * author: asionbo
 * 
 * time: 2017-2-5 下午4:24:46
 * 
 * dp-px互换
 * ========================================================================
 */
public class DensityUtils {

	/**
	 * px2dp
	 * @param ctx
	 * @param px
	 * @return 返回float类型
	 */
	public static float px2dp(Context ctx,int px){
		float density = ctx.getResources().getDisplayMetrics().density;//密度
		float dp = px/density;
		return dp;
	}
	
	/**
	 * dp2px
	 * @param ctx
	 * @param dp
	 * @return 返回int类型
	 */
	public static int dp2px(Context ctx,float dp){
		float density = ctx.getResources().getDisplayMetrics().density;
		int px = (int) (dp*density + 0.5f);//四舍五入
		return px;
	}
}
