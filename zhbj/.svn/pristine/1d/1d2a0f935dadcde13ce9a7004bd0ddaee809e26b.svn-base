package com.itandroid.zhbj;

import com.itandroid.zhbj.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

public class SplashActivity extends Activity {

	private RelativeLayout rl_root;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		rl_root = (RelativeLayout) findViewById(R.id.rl_root);
		
		RotateAnimation animRote = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animRote.setDuration(1000);
		animRote.setFillAfter(true);	//保持动画结束状态
		
		ScaleAnimation animScaleAnimation = new ScaleAnimation(0, 1, 0, 1, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		animScaleAnimation.setDuration(1000);
		animScaleAnimation.setFillAfter(true);	//保持动画结束状态
		//渐变动画
		AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
		alphaAnimation.setDuration(2000);
		alphaAnimation.setFillAfter(true);
		
		//动画集合
		AnimationSet set = new AnimationSet(true);
		set.addAnimation(animRote);
		set.addAnimation(animScaleAnimation);
		set.addAnimation(alphaAnimation);
		
		//启动动画
		rl_root.startAnimation(set);
		
		set.setAnimationListener(new AnimationListener() {
			
			public void onAnimationStart(Animation arg0) {
				
			}
			
			public void onAnimationRepeat(Animation arg0) {
				
			}
			
			public void onAnimationEnd(Animation arg0) {
				// 动画结束，跳转界面
//				//第一次，进入引导页，否则进入主页面
//				SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
//				boolean isFirstEnter = sp.getBoolean("is_first_enter", true);
				boolean isFirstEnter = PrefUtils.getBoolean(SplashActivity.this, "isFirstEnter", true);
				Intent intent;
				if (isFirstEnter) {
					intent = new Intent(getApplicationContext(),GuideActivity.class);
				}else {
					intent = new Intent(getApplicationContext(),MainActivity.class);
				}
				startActivity(intent);
				
				finish();//结束当前界面
				
				
			}
		});
		
		

	}


}
