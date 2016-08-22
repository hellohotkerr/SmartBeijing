package com.itandroid.zhbj.base.impl;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.itandroid.zhbj.base.BasePager;

public class GovAffairsPager extends BasePager {

	public GovAffairsPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		System.out.println("�����ʼ����...");

		// Ҫ��֡������䲼�ֶ���
		TextView view = new TextView(mActivity);
		view.setText("����");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);

		flContent.addView(view);

		// �޸ı���
		tvTitle.setText("�˿ڹ���");

		// ��ʾ�˵���ť
		btnMenu.setVisibility(View.VISIBLE);

	}

}
