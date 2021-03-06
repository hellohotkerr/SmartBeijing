package com.itandroid.zhbj;
import java.util.ArrayList;

import com.itandroid.zhbj.utils.PrefUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class GuideActivity extends Activity {
	private ViewPager mViewPager;
	private int[] mImageIds = new int[]{R.drawable.guide_1,R.drawable.guide_2,
			R.drawable.guide_3};  //引导页图片id数组
	//ImageView集合
	private ArrayList<ImageView> mImageViews;
	private LinearLayout ll_container;
	private int mPointDis;
	private ImageView ivRedPoint;
	private Button btnStart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guide);
		mViewPager = (ViewPager) findViewById(R.id.vp_guide);
		initData();
		mViewPager.setAdapter(new GuideAdapter());  //设置数据
		ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
		btnStart = (Button) findViewById(R.id.btn_start);
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			// 某个页面被选中
			public void onPageSelected(int position) {
				// 某个页面被选中
				if (position == mImageViews.size() -1) {
					btnStart.setVisibility(View.VISIBLE);
				}else {
					btnStart.setVisibility(View.INVISIBLE);
					
				}
				
			}
			
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// 当页面滑动过程中的回调
				//更新小红点距离
				int leftMargin = (int) (mPointDis *positionOffset)
						+ position * mPointDis;//计算小红点当前左边距
				RelativeLayout.LayoutParams params =  
						(android.widget.RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
				params.leftMargin = leftMargin;
				
				//重新设置布局参数
				ivRedPoint.setLayoutParams(params);
			}
			
			public void onPageScrollStateChanged(int state) {
				// 页面状态发生变化的回调
				
			}
		});
		
		// 计算两个圆点之间的距离
		// 移动距离=第二个圆点left值 - 第一个圆点left值
		// measure->layout(确定位置)->draw(activity的onCreate方法执行结束之后才会走此流程)
		// mPointDis = ll_container.getChildAt(1).getLeft()
		// - ll_container.getChildAt(0).getLeft();
		
		// 监听layout方法结束的事件,位置确定好之后再获取圆点间距
		// 视图树
		ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					public void onGlobalLayout() {
						// 移除监听,避免重复回调
						ivRedPoint.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						// ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
						// layout方法执行结束的回调
						mPointDis = ll_container.getChildAt(1).getLeft()
								- ll_container.getChildAt(0).getLeft();
					}
				});
		btnStart.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// 更新sp,不是第一次进入
				PrefUtils.setBoolean(getApplicationContext(), "isFirstEnter", false);
				//跳转到主页面
				startActivity(new Intent(getApplicationContext(),MainActivity.class));
				
			}
		});
		
}
	private void initData() {
		mImageViews = new ArrayList<ImageView>();
		for (int i = 0; i < mImageIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(mImageIds[i]); 	//通过设置背景，可以让宽高填充布局
			mImageViews.add(imageView);
			
			
			ll_container = (LinearLayout) findViewById(R.id.ll_container);
			ImageView pointImageView = new ImageView(this);
			
			pointImageView.setImageResource(R.drawable.shape_point_gray);
			//初始化布局参数，宽高包裹内容，
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);
			if (i > 0) {
				params.leftMargin =10;
			}
			pointImageView.setLayoutParams(params);
			ll_container.addView(pointImageView);
			
		
		}
	}
	class GuideAdapter extends PagerAdapter{
		
		// item的个数
		@Override
		public int getCount() {
			return mImageViews.size();   
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		// 初始化item布局
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = mImageViews.get(position);
			container.addView(view);
			return view;
		}
		
		// 销毁item
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
	}
}
