package com.itandroid.zhbj;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;
import android.view.WindowManager;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

import fragment.ContentFragment;
import fragment.LeftMenuFragment;

public class MainActivity extends SlidingFragmentActivity {

	private static final String TAG_LEFT_MENU = "TAG_LEFT_MENU";
	private static final String TAG_CONTENT = "TAG_CONTENT";

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题,
		// 必须在setContentView之前调用
		setContentView(R.layout.activity_main);

		// Utils.doSomthing();
		// R.drawable.p_10
		setBehindContentView(R.layout.left_menu);
		SlidingMenu slidingMenu = getSlidingMenu();
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 全屏触摸
//		slidingMenu.setBehindOffset(400);// 屏幕预留400像素宽度
		
		//200/320 * 屏幕宽度
		WindowManager wm = getWindowManager();
		int width = wm.getDefaultDisplay().getWidth();
		slidingMenu.setBehindOffset(width*200/320);

		initFragment();
	}

	/**
	 * 初始化Fragment
	 */
	private void initFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.replace(R.id.fl_left_menu, new LeftMenuFragment(),
				TAG_LEFT_MENU);
		// .addToBackStack(null);

		transaction.replace(R.id.fl_main, new ContentFragment(), TAG_CONTENT);
		// .addToBackStack(null);
		transaction.commit();

	}

	public LeftMenuFragment getLeftMenuFragment() {
		FragmentManager fragmentManager = getSupportFragmentManager();
		LeftMenuFragment fragment = (LeftMenuFragment) fragmentManager
				.findFragmentByTag(TAG_LEFT_MENU);
		return fragment;
	}
	
	// 获取主页fragment对象
	public ContentFragment getContentFragment() {
		FragmentManager fm = getSupportFragmentManager();
		ContentFragment fragment = (ContentFragment) fm
				.findFragmentByTag(TAG_CONTENT);// 根据标记找到对应的fragment
		return fragment;
	}
}