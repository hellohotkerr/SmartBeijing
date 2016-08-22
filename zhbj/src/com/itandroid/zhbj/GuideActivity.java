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
			R.drawable.guide_3};  //����ҳͼƬid����
	//ImageView����
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
		mViewPager.setAdapter(new GuideAdapter());  //��������
		ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
		btnStart = (Button) findViewById(R.id.btn_start);
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			// ĳ��ҳ�汻ѡ��
			public void onPageSelected(int position) {
				// ĳ��ҳ�汻ѡ��
				if (position == mImageViews.size() -1) {
					btnStart.setVisibility(View.VISIBLE);
				}else {
					btnStart.setVisibility(View.INVISIBLE);
					
				}
				
			}
			
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// ��ҳ�滬�������еĻص�
				//����С������
				int leftMargin = (int) (mPointDis *positionOffset)
						+ position * mPointDis;//����С��㵱ǰ��߾�
				RelativeLayout.LayoutParams params =  
						(android.widget.RelativeLayout.LayoutParams) ivRedPoint.getLayoutParams();
				params.leftMargin = leftMargin;
				
				//�������ò��ֲ���
				ivRedPoint.setLayoutParams(params);
			}
			
			public void onPageScrollStateChanged(int state) {
				// ҳ��״̬�����仯�Ļص�
				
			}
		});
		
		// ��������Բ��֮��ľ���
		// �ƶ�����=�ڶ���Բ��leftֵ - ��һ��Բ��leftֵ
		// measure->layout(ȷ��λ��)->draw(activity��onCreate����ִ�н���֮��Ż��ߴ�����)
		// mPointDis = ll_container.getChildAt(1).getLeft()
		// - ll_container.getChildAt(0).getLeft();
		
		// ����layout�����������¼�,λ��ȷ����֮���ٻ�ȡԲ����
		// ��ͼ��
		ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					public void onGlobalLayout() {
						// �Ƴ�����,�����ظ��ص�
						ivRedPoint.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						// ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
						// layout����ִ�н����Ļص�
						mPointDis = ll_container.getChildAt(1).getLeft()
								- ll_container.getChildAt(0).getLeft();
					}
				});
		btnStart.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				// ����sp,���ǵ�һ�ν���
				PrefUtils.setBoolean(getApplicationContext(), "isFirstEnter", false);
				//��ת����ҳ��
				startActivity(new Intent(getApplicationContext(),MainActivity.class));
				
			}
		});
		
}
	private void initData() {
		mImageViews = new ArrayList<ImageView>();
		for (int i = 0; i < mImageIds.length; i++) {
			ImageView imageView = new ImageView(this);
			imageView.setBackgroundResource(mImageIds[i]); 	//ͨ�����ñ����������ÿ�����䲼��
			mImageViews.add(imageView);
			
			
			ll_container = (LinearLayout) findViewById(R.id.ll_container);
			ImageView pointImageView = new ImageView(this);
			
			pointImageView.setImageResource(R.drawable.shape_point_gray);
			//��ʼ�����ֲ��������߰������ݣ�
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
		
		// item�ĸ���
		@Override
		public int getCount() {
			return mImageViews.size();   
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		// ��ʼ��item����
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = mImageViews.get(position);
			container.addView(view);
			return view;
		}
		
		// ����item
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
		
	}
}