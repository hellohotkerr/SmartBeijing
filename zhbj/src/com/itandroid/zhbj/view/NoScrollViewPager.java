package com.itandroid.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager{

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public NoScrollViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	//事件拦截
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return false;	//不拦截此事件
	}
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		//实现禁用滑动
		return true;
	}

}
