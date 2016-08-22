package com.itandroid.zhbj.domain;

import java.util.ArrayList;

import android.R.integer;

/**
 * 分类信息对象封装
 * @author Administrator
 *
 */
public class NewsMenu {
	
	public int retcode;
	public ArrayList<Integer> extend;
	
	@Override
	public String toString() {
		return "NewsMenu [data=" + data + "]";
	}
	public ArrayList<NewsMenuData> data;
	
	//侧边栏菜单对象
	public class NewsMenuData{
		public int id;
		public String title;
		public int type;
		
		@Override
		public String toString() {
			return "NewsMenuData [title=" + title + ", children=" + children
					+ "]";
		}

		public ArrayList<NewsTabData> children;
	}
	//页签的对象
	public class NewsTabData{
		public int id;
		@Override
		public String toString() {
			return "NewsTabData [title=" + title + "]";
		}
		public String title;
		public int type;
		public String url;
	}
	
}
