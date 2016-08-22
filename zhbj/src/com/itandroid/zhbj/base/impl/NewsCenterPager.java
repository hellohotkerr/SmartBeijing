package com.itandroid.zhbj.base.impl;

import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itandroid.zhbj.MainActivity;
import com.itandroid.zhbj.base.BaseMenuDetailPager;
import com.itandroid.zhbj.base.BasePager;
import com.itandroid.zhbj.base.impl.menu.InteractMenuDetailPager;
import com.itandroid.zhbj.base.impl.menu.NewsMenuDetailPager;
import com.itandroid.zhbj.base.impl.menu.PhotosMenuDetailPager;
import com.itandroid.zhbj.base.impl.menu.TopicMenuDetailPager;
import com.itandroid.zhbj.domain.NewsMenu;
import com.itandroid.zhbj.global.GlobalConstants;
import com.itandroid.zhbj.utils.CacheUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import fragment.LeftMenuFragment;

public class NewsCenterPager extends BasePager {
	private ArrayList<BaseMenuDetailPager> mMenuDetailPagers;// �˵�����ҳ����

	private NewsMenu mNewsData;// ������Ϣ��������

	public NewsCenterPager(Activity activity) {
		super(activity);
	}

	@Override
	public void initData() {
		System.out.println("�������ĳ�ʼ����...");

		// Ҫ��֡������䲼�ֶ���
		TextView view = new TextView(mActivity);
		view.setText("��������");
		view.setTextColor(Color.RED);
		view.setTextSize(22);
		view.setGravity(Gravity.CENTER);

		flContent.addView(view);

		// �޸ı���
		tvTitle.setText("����");

		// ��ʾ�˵���ť
		btnMenu.setVisibility(View.VISIBLE);

		// ���ж���û�л���
		String cache = CacheUtils.getCache(GlobalConstants.CATEGORY_URL,
				mActivity);
		if (!TextUtils.isEmpty(cache)) {
			processData(cache);
		}
		// �������������ȡ����
		// ��Դ���:xUtils
		getDataFromServer();

	}

	/**
	 * �ӷ�������ȡ����
	 */
	private void getDataFromServer() {
		HttpUtils utils = new HttpUtils();

		utils.send(HttpMethod.GET, GlobalConstants.CATEGORY_URL,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String result = responseInfo.result; // ��ȡ���������ؽ��
						System.out.println("���������ؽ�� :" + result);

						// JsonObject,Gson
						processData(result);

						// д����
						CacheUtils.setCache(GlobalConstants.CATEGORY_URL,
								result, mActivity);

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						error.printStackTrace();
						Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT)
								.show();
						System.out.println("����ʧ��");
					}
				});

	}

	/**
	 * ��������
	 */
	private void processData(String json) {
		// Gson
		Gson gson = new Gson();
		mNewsData = gson.fromJson(json, NewsMenu.class);
		System.out.println("���������:" + mNewsData);

		// ��ȡ���������
		MainActivity mainUI = (MainActivity) mActivity;
		LeftMenuFragment leftMenuFragment = mainUI.getLeftMenuFragment();

		// ���������������
		leftMenuFragment.setMenuData(mNewsData.data);

		// ��ʼ��4���˵�����ҳ
		mMenuDetailPagers = new ArrayList<BaseMenuDetailPager>();
		mMenuDetailPagers.add(new NewsMenuDetailPager(mActivity,mNewsData.data.get(0).children));
		mMenuDetailPagers.add(new TopicMenuDetailPager(mActivity));
		mMenuDetailPagers.add(new PhotosMenuDetailPager(mActivity,btnPhoto));
		mMenuDetailPagers.add(new InteractMenuDetailPager(mActivity));

		// �����Ų˵�����ҳ����ΪĬ��ҳ��
		setCurrentDetailPager(0);

	}

	// ���ò˵�����ҳ
	public void setCurrentDetailPager(int position) {
		// ���¸�frameLayout��������
		BaseMenuDetailPager pager = mMenuDetailPagers.get(position);// ��ȡ��ǰӦ����ʾ��ҳ��
		View view = pager.mRootView;// ��ǰҳ��Ĳ���

		// ���֮ǰ�ɵĲ���
		flContent.removeAllViews();

		flContent.addView(view);// ��֡�������Ӳ���

		// ��ʼ��ҳ������
		pager.initData();

		// ���±���
		tvTitle.setText(mNewsData.data.get(position).title);
		
		// �������ͼҳ��, ��Ҫ��ʾ�л���ť
		
		if (pager instanceof PhotosMenuDetailPager) {
			
			btnPhoto.setVisibility(View.VISIBLE);
			
		}else {
			// �����л���ť
			btnPhoto.setVisibility(View.INVISIBLE);
		}
	}

}