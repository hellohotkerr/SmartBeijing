package com.itandroid.zhbj.base.impl.menu;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.itandroid.zhbj.NewsDetailActivity;
import com.itandroid.zhbj.R;
import com.itandroid.zhbj.base.BaseMenuDetailPager;
import com.itandroid.zhbj.domain.NewsMenu.NewsTabData;
import com.itandroid.zhbj.domain.NewsTabBean;
import com.itandroid.zhbj.domain.NewsTabBean.NewsData;
import com.itandroid.zhbj.domain.NewsTabBean.TopNews;
import com.itandroid.zhbj.global.GlobalConstants;
import com.itandroid.zhbj.utils.CacheUtils;
import com.itandroid.zhbj.utils.PrefUtils;
import com.itandroid.zhbj.view.PullToRefreshListView;
import com.itandroid.zhbj.view.PullToRefreshListView.onRefreshListener;
import com.itandroid.zhbj.view.TopNewsViewPager;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * ҳǩҳ�����
 * 
 * @author Administrator
 * 
 */
public class TabDetailPager extends BaseMenuDetailPager {
	private NewsTabData mTabData; // ����ҳǩ����������
	private TextView view;
	@ViewInject(R.id.vp_top_news)
	private TopNewsViewPager mViewPager;
	private String mUrl;
	private ArrayList<TopNews> mTopNews;
	@ViewInject(R.id.tv_title)
	private TextView tvTitle;
	@ViewInject(R.id.circle_page_indicator)
	private CirclePageIndicator mIndicator;
	@ViewInject(R.id.lv_list_tab_detail)
	private PullToRefreshListView lvList;
	private ArrayList<NewsData> mNewsList;
	private NewsAdapter mNewsAdapter;
	private String mMoreUrl;
	
	private Handler mHandler;

	public TabDetailPager(Activity activity, NewsTabData newsTabData) {
		super(activity);
		mTabData = newsTabData;

		mUrl = GlobalConstants.SERVER_URL + mTabData.url;
	}

	@Override
	public View initView() {
		// view = new TextView(mActivity);
		// // view.setText(mTabData.title); //��ָ���쳣
		// view.setTextColor(Color.RED);
		// view.setTextSize(22);
		// view.setGravity(Gravity.CENTER);
		View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
		ViewUtils.inject(this, view);
		
		//��listView���ͷ����
		View mHeadView = View.inflate(mActivity, R.layout.list_item_header, null);
		ViewUtils.inject(this, mHeadView);	//�˴����뽫ͷ����Ҳע��
		
		lvList.addHeaderView(mHeadView);
		
		// 5. ǰ�˽������ûص�
		lvList.setOnRefreshListener(new onRefreshListener() {
			
			public void Refresh() {
				//ˢ������
				getDataFromSeiver();
			}

			public void LoadMore() {
				// �ж��Ƿ�����һҳ����
				if (mMoreUrl != null) {
					
					getMoreDataFromServer();
				}else {
					// û����һҳ
					Toast.makeText(mActivity, "û�и���������", Toast.LENGTH_SHORT)
							.show();
					// û������ʱҲҪ����ؼ�
					lvList.onRefreshComplete(true);
				}
			}
		});
		
		lvList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					int headerViewsCount = lvList.getHeaderViewsCount();// ��ȡͷ��������
					position = position -headerViewsCount;
					
					NewsData news = mNewsList.get(position);
					// read_ids: 1101,1102,1105,1203,
					String readIds = PrefUtils.getString(mActivity, "read_Id", "");
					
					if (!readIds.contains(news.id + "")) {// ֻ�в�������ǰid,��׷��,
						// �����ظ����ͬһ��id
						
						readIds = readIds + news.id + ",";// 1101,1102,
						PrefUtils.setString(mActivity, "read_Id", readIds);
					}
					
					// Ҫ���������item��������ɫ��Ϊ��ɫ, �ֲ�ˢ��, view������ǵ�ǰ������Ķ���
					TextView tvTitle= (TextView) view.findViewById(R.id.tv_title);
					tvTitle.setTextColor(Color.GRAY);
					// mNewsAdapter.notifyDataSetChanged();//ȫ��ˢ��, �˷�����
					
					// ������������ҳ��
					Intent intent = new Intent(mActivity,NewsDetailActivity.class);
					intent.putExtra("url", news.url);
					mActivity.startActivity(intent);
					
					
			}
		});
		
		return view;
	}

	/**
	 *������һҳ����
	 */
	protected void getMoreDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				System.out.println("����ʧ��");

				// ��������ˢ�¿ؼ�
				lvList.onRefreshComplete(false);

			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				processData(result,true);

				// ��������ˢ�¿ؼ�
				lvList.onRefreshComplete(true);
			}
		});
	}

	@Override
	public void initData() {
		super.initData();
		// view.setText(mTabData.title);
		String cache = CacheUtils.getCache(mUrl, mActivity);
		if (!TextUtils.isEmpty(cache)) {
			processData(cache,false);
		}

		getDataFromSeiver();
	}

	private void getDataFromSeiver() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mUrl, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				System.out.println("����ʧ��");

				// ��������ˢ�¿ؼ�
				lvList.onRefreshComplete(false);

			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				processData(result,false);

				CacheUtils.setCache(mUrl, result, mActivity);
				
				// ��������ˢ�¿ؼ�
				lvList.onRefreshComplete(true);
			}
		});
	}

	protected void processData(String result,boolean isMore) {
		Gson gson = new Gson();
		NewsTabBean newsTabBean = gson.fromJson(result, NewsTabBean.class);
		
		String moreUrl = newsTabBean.data.more;
		
		if (!TextUtils.isEmpty(moreUrl)) {
			mMoreUrl = GlobalConstants.SERVER_URL + moreUrl;
			
		}else {
			mMoreUrl = null;
		}
		if (!isMore) {

			// ͷ�������������
			mTopNews = newsTabBean.data.topnews;

			if (mTopNews != null) {
				mViewPager.setAdapter(new TopNewsAdapter());
				mIndicator.setViewPager(mViewPager);
				mIndicator.setSnap(true); // ���շ�ʽչʾ(СԲ��)

				// �¼�Ҫ���ø�Indicator
				mIndicator.setOnPageChangeListener(new OnPageChangeListener() {

					public void onPageSelected(int position) {
						// ����ͷ�����ű���
						TopNews topNews = mTopNews.get(position);
						tvTitle.setText(topNews.title);
					}

					public void onPageScrolled(int position, float positionOffset,
							int positionOffsetPixels) {

					}

					public void onPageScrollStateChanged(int state) {

					}
				});

				// ���µ�һ��ͷ�����ű���
				tvTitle.setText(mTopNews.get(0).title);
				mIndicator.onPageSelected(0);// Ĭ���õ�һ��ѡ��(���ҳ�����ٺ����³�ʼ��ʱ,Indicator��Ȼ�����ϴ�Բ��λ�õ�bug)
			}
			
			// �б�����

			mNewsList = newsTabBean.data.news;
			if (mNewsList != null) {
				mNewsAdapter = new NewsAdapter();
				lvList.setAdapter(mNewsAdapter);
			}
			
			if (mHandler == null) {
				mHandler = new Handler(){
					public void handleMessage(android.os.Message msg) {
//						super.handleMessage(msg);
						int currentItem = mViewPager.getCurrentItem();
						currentItem++;
						
						if (currentItem > mTopNews.size() -1) {
							currentItem = currentItem % mTopNews.size() ; // ����Ѿ��������һ��ҳ��,������һҳ
						}
						mViewPager.setCurrentItem(currentItem);
						mHandler.sendEmptyMessageDelayed(0, 3000);// ����������ʱ3�����Ϣ,�γ���ѭ��
					};
				};
				
				// ��֤�����Զ��ֲ��߼�ִֻ��һ��
				mHandler.sendEmptyMessageDelayed(0, 3000);// ������ʱ3�����Ϣ
				
				mViewPager.setOnTouchListener(new OnTouchListener() {
					
					public boolean onTouch(View v, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							System.out.println("ACTION_DOWN");
							// ֹͣ����Զ��ֲ�
							// ɾ��handler��������Ϣ
							
							mHandler.removeCallbacksAndMessages(null);
							// mHandler.post(new Runnable() {
							//
							// @Override
							// public void run() {
							// //�����߳�����
							// }
							// });
							
							break;
						case MotionEvent.ACTION_UP:
							System.out.println("ACTION_UP");
							
							mHandler.sendEmptyMessageDelayed(0, 3000);
							
							break;
						case MotionEvent.ACTION_CANCEL:// ȡ���¼�,
							System.out.println("ACTION_CANCEL");// ������viewpager��,ֱ�ӻ���listview,
							//����̧���¼��޷���Ӧ,�����ߴ��¼�
							
							mHandler.sendEmptyMessageDelayed(0, 3000);
							
							break;

						default:
							break;
						}
						return false;
					}
				});
			
			}
			
			
			
		}else {
			//���ظ�������
			ArrayList<NewsData> moreNews = newsTabBean.data.news;
			mNewsList.addAll(moreNews);	// ������׷����ԭ���ļ�����
			// ˢ��listview
			mNewsAdapter.notifyDataSetChanged();
		}
		
	}

	class TopNewsAdapter extends PagerAdapter {

		private BitmapUtils mBitmapUtils;

		public TopNewsAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			// ���ü����е�Ĭ��ͼƬ
			mBitmapUtils
					.configDefaultLoadingImage(R.drawable.topnews_item_default);

		}

		@Override
		public int getCount() {
			return mTopNews.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			ImageView view = new ImageView(mActivity);
			// view.setImageResource(R.drawable.topnews_item_default);
			view.setScaleType(ScaleType.FIT_XY); // ����ͼƬ���ŷ�ʽ�������丸�ؼ�

			String imageUrl = mTopNews.get(position).topimage;

			// ����ͼƬ����ͼƬ���ø�imageview,�����ڴ����������
			// BitmapUtils - xUtils

			mBitmapUtils.display(view, imageUrl);

			container.addView(view);

			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}
	}

	class NewsAdapter extends BaseAdapter {
		private BitmapUtils mBitmapUtils;

		public NewsAdapter(){
			mBitmapUtils = new BitmapUtils(mActivity);
			mBitmapUtils.configDefaultLoadingImage(R.drawable.news_pic_default);
			
		}

		public int getCount() {
			return mNewsList.size();
		}

		public NewsData getItem(int position) {
			return mNewsList.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mActivity, R.layout.list_item_news,
						null);
				holder = new ViewHolder();
				holder.ivIcon = (ImageView) convertView.findViewById(R.id.iv_icon);
				holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
				holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			NewsData news = getItem(position);
			holder.tvTitle.setText(news.title);
			holder.tvDate.setText(news.pubdate);
			
			
			// ���ݱ��ؼ�¼������Ѷ�δ��
			String readIds = PrefUtils.getString(mActivity, "read_Id", "");
			if (readIds.contains(news.id + "")) {
				holder.tvTitle.setTextColor(Color.GRAY);
			}else {
				holder.tvTitle.setTextColor(Color.BLACK);
				
			}
			
			mBitmapUtils.display(holder.ivIcon, news.listimage);
			
			
			return convertView;
		}
		

	}
	static class ViewHolder{
		public ImageView ivIcon;
		public TextView tvTitle;
		public TextView tvDate;
		
	}

}
