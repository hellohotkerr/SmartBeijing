package fragment;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.itandroid.zhbj.MainActivity;
import com.itandroid.zhbj.R;
import com.itandroid.zhbj.base.BasePager;
import com.itandroid.zhbj.base.impl.GovAffairsPager;
import com.itandroid.zhbj.base.impl.HomePager;
import com.itandroid.zhbj.base.impl.NewsCenterPager;
import com.itandroid.zhbj.base.impl.SettingPager;
import com.itandroid.zhbj.base.impl.SmartServicePager;
import com.itandroid.zhbj.view.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class ContentFragment extends BaseFragment {

	private NoScrollViewPager mViewPager;
	private ArrayList<BasePager> mPagers;
	private RadioGroup mRadioGroup;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_content, null);
		mViewPager = (NoScrollViewPager) view.findViewById(R.id.vp_content);
		mRadioGroup = (RadioGroup) view.findViewById(R.id.rg_group);
		return view;
	}

	@Override
	public void initData() {
		mPagers = new ArrayList<BasePager>();

		mPagers.add(new HomePager(mActivity));
		mPagers.add(new NewsCenterPager(mActivity));
		mPagers.add(new SmartServicePager(mActivity));
		mPagers.add(new GovAffairsPager(mActivity));
		mPagers.add(new SettingPager(mActivity));

		mViewPager.setAdapter(new ContentAdapter());
		mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home:
					// mViewPager.setCurrentItem(0);
					mViewPager.setCurrentItem(0, false); // ����2��ʾ�Ƿ����ƽ������
					break;
				case R.id.rb_news:
					mViewPager.setCurrentItem(1, false);
					break;
				case R.id.rb_smart:
					mViewPager.setCurrentItem(2, false);
					break;

				case R.id.rb_gov:
					mViewPager.setCurrentItem(3, false);
					break;

				case R.id.rb_setting:
					mViewPager.setCurrentItem(4, false);
					break;

				default:
					break;
				}
			}
		});
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			public void onPageSelected(int position) {
				BasePager pager = mPagers.get(position);
				pager.initData();

				if (position == 0 || position == mPagers.size() - 1) {
					// ��ҳ������ҳҪ���ò����
					setSlidingMenuEnable(false);
				} else {
					// ����ҳ�濪�������
					setSlidingMenuEnable(true);
				}
			}

			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub

			}
		});

		// ��ҳ���ò����
		setSlidingMenuEnable(false);

	}

	/**
	 * ��������ò����
	 * 
	 * @param enable
	 */
	protected void setSlidingMenuEnable(boolean enable) {
		// ��ȡ���������
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		if (enable) {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		} else {
			slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		}

	}

	class ContentAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mPagers.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			// TODO Auto-generated method stub
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			BasePager pager = mPagers.get(position);
			View view = pager.mRootView;
			// pager.initData();// ��ʼ������, viewpager��Ĭ�ϼ�����һ��ҳ��,
			// Ϊ�˽�ʡ����������,��Ҫ�ڴ˴����ó�ʼ�����ݵķ���

			container.addView(view); // ���ز���
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

	}
	
	// ��ȡ��������ҳ��
		public NewsCenterPager getNewsCenterPager() {
			NewsCenterPager pager = (NewsCenterPager) mPagers.get(1);
			return pager;
		}

}
