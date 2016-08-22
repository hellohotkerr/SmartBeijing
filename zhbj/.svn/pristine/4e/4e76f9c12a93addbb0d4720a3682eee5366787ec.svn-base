package com.itandroid.zhbj.utils;

import android.content.Context;

/**
 * 网络缓存工具类
 * @author Administrator
 *
 */
public class CacheUtils {
	
	/**
	 * 以url为key,以json为value保存在本地
	 * @param url
	 * @param json
	 */
	public static void setCache(String url,String json,Context ctx){
		PrefUtils.setString(ctx, url, json);
	}

	/**获取缓存
	 * @param url
	 * @param ctx
	 * @return
	 */
	public static String getCache(String url,Context ctx){
		
		return PrefUtils.getString(ctx, url, null);
	}
}
