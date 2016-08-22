package com.itandroid.zhbj.utils;

import com.itandroid.zhbj.R;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class MyBitmaoUtils {
	private NetCacheUtils mNetCacheUtils;
	private LocalCacheUtils mLocalCacheUtils;
	private MemoryCacheUtils mMemoryCacheUtils;

	public MyBitmaoUtils() {
		mLocalCacheUtils = new LocalCacheUtils();
		mNetCacheUtils = new NetCacheUtils(mLocalCacheUtils, mMemoryCacheUtils);
	}

	public void dispaly(ImageView imageView, String url) {
		// ����Ĭ��ͼƬ
		imageView.setImageResource(R.drawable.pic_item_list_default);
		// ���ȴ��ڴ��м���ͼƬ, �ٶ����, ���˷�����
		Bitmap bitmap = mMemoryCacheUtils.getMemoryCache(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			System.out.println("���ڴ����ͼƬ��");
			return;
		}
		// ��δӱ���(sdcard)����ͼƬ, �ٶȿ�, ���˷�����
		bitmap = mLocalCacheUtils.getLocalCache(url);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			System.out.println("�ӱ��ؼ���ͼƬ��");

			// д�ڴ滺��
			mMemoryCacheUtils.setMemoryCache(url, bitmap);
			return;
		}
		// ������������ͼƬ, �ٶ���, �˷�����
		mNetCacheUtils.getBitmapFromNet(imageView, url);

	}

}
