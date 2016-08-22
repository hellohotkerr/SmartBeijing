package com.itandroid.zhbj.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**SharePreference·â×°
 * @author Administrator
 *
 */
public class PrefUtils {
	public static boolean getBoolean(Context ctx,String key,boolean defaultVlaue){
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getBoolean(key, defaultVlaue);
	} 
	
	public static void setBoolean(Context ctx,String key,boolean value){
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
	
	public static void setString(Context ctx,String key,String value){
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putString(key, value).commit();
	}
	
	public static String getString(Context ctx,String key,String defaultVlaue){
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getString(key, defaultVlaue);
	} 
	
	public static void setInt(Context ctx,String key,int value){
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		sp.edit().putInt(key, value).commit();
	}
	
	public static int getInt(Context ctx,String key,int defaultVlaue){
		SharedPreferences sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
		return sp.getInt(key, defaultVlaue);
	} 
}
