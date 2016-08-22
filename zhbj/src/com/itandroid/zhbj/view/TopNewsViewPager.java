package com.itandroid.zhbj.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * ͷ�������Զ���viewpager
 * 
 * @author Administrator
 * 
 */
public class TopNewsViewPager extends ViewPager {

	private int startX;
	private int startY;

	public TopNewsViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public TopNewsViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 1. ���»�����Ҫ���� 
	 * 2. ���һ������ҵ�ǰ�ǵ�һ��ҳ��,��Ҫ���� 
	 * 3. ���󻬶����ҵ�ǰ�����һ��ҳ��,��Ҫ����
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		getParent().requestDisallowInterceptTouchEvent(true);

		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = (int) ev.getX();
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:

			int endX = (int) ev.getX();
			int endY = (int) ev.getY();

			int dx = endX - startX;
			int dy = endY - startY;

			if (Math.abs(dy) < Math.abs(dx)) {
				int currentItem = getCurrentItem();
				// ���һ���
				if (dx > 0) {
					// ���һ�
					if (currentItem == 0) {
						// ��һ��ҳ��,��Ҫ����
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				} else {
					// ����
					int count = getAdapter().getCount();// item����
					if (currentItem == count - 1) {
						// ���һ��ҳ��,��Ҫ����
						getParent().requestDisallowInterceptTouchEvent(false);
					}
				}

			} else {
				// ���»���,��Ҫ����
				getParent().requestDisallowInterceptTouchEvent(false);
			}

			break;

		default:
			break;
		}

		return super.dispatchTouchEvent(ev);
	}

}
