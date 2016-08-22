package com.itandroid.zhbj.base;

import android.app.Activity;
import android.view.View;

/**
 * �˵�����ҳ����
 * 
 * 
 * 
 */
public abstract class BaseMenuDetailPager {

	public Activity mActivity;
	public View mRootView;// �˵�����ҳ������

	public BaseMenuDetailPager(Activity activity) {
		mActivity = activity;
		mRootView = initView();
	}

	// ��ʼ������,��������ʵ��
	public abstract View initView();

	// ��ʼ������
	public void initData() {

	}

}