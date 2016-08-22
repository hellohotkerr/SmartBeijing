package com.itandroid.zhbj.base;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.itandroid.zhbj.MainActivity;
import com.itandroid.zhbj.R;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * �����ǩҳ�Ļ���
 * 
 * @author Kevin
 * @date 2015-10-18
 */
public class BasePager {
	public TextView tvTitle;
	public ImageButton btnMenu;
	public FrameLayout flContent;// �յ�֡���ֶ���, Ҫ��̬���Ӳ���
	public View mRootView;// ��ǰҳ��Ĳ��ֶ���
	public Activity mActivity;
	public ImageButton btnPhoto;

	public BasePager(Activity activity) {
		mActivity = activity;
		mRootView = initView();
	}

	// ��ʼ������
	public View initView() {
		View view = View.inflate(mActivity, R.layout.base_pager, null);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		btnMenu = (ImageButton) view.findViewById(R.id.btn_menu);
		flContent = (FrameLayout) view.findViewById(R.id.fl_content);
		btnPhoto = (ImageButton) view.findViewById(R.id.btn_photo);
		
		btnMenu.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				toggle();
			}
		});
		return view;
	}
	
	/**
	 * �򿪻��߹رղ����
	 */
	protected void toggle() {
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		slidingMenu.toggle();// �����ǰ״̬�ǿ�, ���ú�͹�; ��֮��Ȼ
	}

	// ��ʼ������
	public void initData() {

	}
}