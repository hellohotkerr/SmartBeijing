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
 * 页签页面对象
 * 
 * @author Administrator
 * 
 */
public class TabDetailPager extends BaseMenuDetailPager {
	private NewsTabData mTabData; // 单个页签的网络数据
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
		// // view.setText(mTabData.title); //空指针异常
		// view.setTextColor(Color.RED);
		// view.setTextSize(22);
		// view.setGravity(Gravity.CENTER);
		View view = View.inflate(mActivity, R.layout.pager_tab_detail, null);
		ViewUtils.inject(this, view);
		
		//给listView添加头布局
		View mHeadView = View.inflate(mActivity, R.layout.list_item_header, null);
		ViewUtils.inject(this, mHeadView);	//此处必须将头布局也注入
		
		lvList.addHeaderView(mHeadView);
		
		// 5. 前端界面设置回调
		lvList.setOnRefreshListener(new onRefreshListener() {
			
			public void Refresh() {
				//刷新数据
				getDataFromSeiver();
			}

			public void LoadMore() {
				// 判断是否有下一页数据
				if (mMoreUrl != null) {
					
					getMoreDataFromServer();
				}else {
					// 没有下一页
					Toast.makeText(mActivity, "没有更多数据了", Toast.LENGTH_SHORT)
							.show();
					// 没有数据时也要收起控件
					lvList.onRefreshComplete(true);
				}
			}
		});
		
		lvList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					int headerViewsCount = lvList.getHeaderViewsCount();// 获取头布局数量
					position = position -headerViewsCount;
					
					NewsData news = mNewsList.get(position);
					// read_ids: 1101,1102,1105,1203,
					String readIds = PrefUtils.getString(mActivity, "read_Id", "");
					
					if (!readIds.contains(news.id + "")) {// 只有不包含当前id,才追加,
						// 避免重复添加同一个id
						
						readIds = readIds + news.id + ",";// 1101,1102,
						PrefUtils.setString(mActivity, "read_Id", readIds);
					}
					
					// 要将被点击的item的文字颜色改为灰色, 局部刷新, view对象就是当前被点击的对象
					TextView tvTitle= (TextView) view.findViewById(R.id.tv_title);
					tvTitle.setTextColor(Color.GRAY);
					// mNewsAdapter.notifyDataSetChanged();//全局刷新, 浪费性能
					
					// 跳到新闻详情页面
					Intent intent = new Intent(mActivity,NewsDetailActivity.class);
					intent.putExtra("url", news.url);
					mActivity.startActivity(intent);
					
					
			}
		});
		
		return view;
	}

	/**
	 *加载下一页数据
	 */
	protected void getMoreDataFromServer() {
		HttpUtils utils = new HttpUtils();
		utils.send(HttpMethod.GET, mMoreUrl, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
				Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
				System.out.println("请求失败");

				// 收起下拉刷新控件
				lvList.onRefreshComplete(false);

			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				processData(result,true);

				// 收起下拉刷新控件
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
				System.out.println("请求失败");

				// 收起下拉刷新控件
				lvList.onRefreshComplete(false);

			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				processData(result,false);

				CacheUtils.setCache(mUrl, result, mActivity);
				
				// 收起下拉刷新控件
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

			// 头条新闻填充数据
			mTopNews = newsTabBean.data.topnews;

			if (mTopNews != null) {
				mViewPager.setAdapter(new TopNewsAdapter());
				mIndicator.setViewPager(mViewPager);
				mIndicator.setSnap(true); // 快照方式展示(小圆点)

				// 事件要设置给Indicator
				mIndicator.setOnPageChangeListener(new OnPageChangeListener() {

					public void onPageSelected(int position) {
						// 更新头条新闻标题
						TopNews topNews = mTopNews.get(position);
						tvTitle.setText(topNews.title);
					}

					public void onPageScrolled(int position, float positionOffset,
							int positionOffsetPixels) {

					}

					public void onPageScrollStateChanged(int state) {

					}
				});

				// 更新第一个头条新闻标题
				tvTitle.setText(mTopNews.get(0).title);
				mIndicator.onPageSelected(0);// 默认让第一个选中(解决页面销毁后重新初始化时,Indicator仍然保留上次圆点位置的bug)
			}
			
			// 列表新闻

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
							currentItem = currentItem % mTopNews.size() ; // 如果已经到了最后一个页面,跳到第一页
						}
						mViewPager.setCurrentItem(currentItem);
						mHandler.sendEmptyMessageDelayed(0, 3000);// 继续发送延时3秒的消息,形成内循环
					};
				};
				
				// 保证启动自动轮播逻辑只执行一次
				mHandler.sendEmptyMessageDelayed(0, 3000);// 发送延时3秒的消息
				
				mViewPager.setOnTouchListener(new OnTouchListener() {
					
					public boolean onTouch(View v, MotionEvent event) {
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:
							System.out.println("ACTION_DOWN");
							// 停止广告自动轮播
							// 删除handler的所有消息
							
							mHandler.removeCallbacksAndMessages(null);
							// mHandler.post(new Runnable() {
							//
							// @Override
							// public void run() {
							// //在主线程运行
							// }
							// });
							
							break;
						case MotionEvent.ACTION_UP:
							System.out.println("ACTION_UP");
							
							mHandler.sendEmptyMessageDelayed(0, 3000);
							
							break;
						case MotionEvent.ACTION_CANCEL:// 取消事件,
							System.out.println("ACTION_CANCEL");// 当按下viewpager后,直接滑动listview,
							//导致抬起事件无法响应,但会走此事件
							
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
			//加载更多数据
			ArrayList<NewsData> moreNews = newsTabBean.data.news;
			mNewsList.addAll(moreNews);	// 将数据追加在原来的集合中
			// 刷新listview
			mNewsAdapter.notifyDataSetChanged();
		}
		
	}

	class TopNewsAdapter extends PagerAdapter {

		private BitmapUtils mBitmapUtils;

		public TopNewsAdapter() {
			mBitmapUtils = new BitmapUtils(mActivity);
			// 设置加载中的默认图片
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
			view.setScaleType(ScaleType.FIT_XY); // 设置图片缩放方式，宽高填充父控件

			String imageUrl = mTopNews.get(position).topimage;

			// 下载图片，将图片设置给imageview,避免内存溢出，缓存
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
			
			
			// 根据本地记录来标记已读未读
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
