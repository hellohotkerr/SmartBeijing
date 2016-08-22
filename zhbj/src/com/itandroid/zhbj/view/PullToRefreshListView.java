package com.itandroid.zhbj.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.itandroid.zhbj.R;

/**
 * 下拉刷新的ListView
 * 
 * @author Administrator
 * 
 */
public class PullToRefreshListView extends ListView implements OnScrollListener {
	public static final int STATE_PULL_TO_REFRESH = 1;
	public static final int STATE_RELEASE_REFRESH = 2;
	public static final int STATE_REFRESHING = 3;

	private int mCurrentState = STATE_PULL_TO_REFRESH;

	private View mHeadView;
	private TextView tvTitle;
	private TextView tvTime;
	private ImageView ivArrow;
	private ProgressBar pbProgress;
	private int mHeadViewHeight;
	private int startY = -1;
	private RotateAnimation mRotateDown;
	private RotateAnimation mRotateUp;
	private View mFooterView;

	public PullToRefreshListView(Context context) {
		super(context);
		initHeadView();
		initFootView();
	}

	public PullToRefreshListView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initHeadView();
		initFootView();
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initHeadView();
		initFootView();
	}

	/**
	 * 初始化头布局
	 */
	private void initHeadView() {
		mHeadView = View.inflate(getContext(), R.layout.pull_to_refresh_header, null);

		addHeaderView(mHeadView);

		tvTitle = (TextView) mHeadView.findViewById(R.id.tv_title);
		tvTime = (TextView) mHeadView.findViewById(R.id.tv_time);
		ivArrow = (ImageView) mHeadView.findViewById(R.id.iv_arrow);
		pbProgress = (ProgressBar) mHeadView.findViewById(R.id.pb_loading);

		// 隐藏头布局
		mHeadView.measure(0, 0);
		mHeadViewHeight = mHeadView.getMeasuredHeight();
		mHeadView.setPadding(0, -mHeadViewHeight, 0, 0);

		initAnimation();
		setCurrentTime();

	}
	/**
	 * 初始化脚布局
	 */
	private void initFootView(){
		mFooterView = View.inflate(getContext(), R.layout.pull_to_refresh_footer, null);

		addFooterView(mFooterView);

		// 隐藏脚布局
		mFooterView.measure(0, 0);
		mFooterHeight = mFooterView.getMeasuredHeight();
		mFooterView.setPadding(0, -mFooterHeight, 0, 0);
		this.setOnScrollListener(this);
	}

	/**
	 * 设置刷新时间
	 */
	private void setCurrentTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String time = format.format(new Date());
		
		tvTime.setText(time);
	}

	private void initAnimation() {
		mRotateDown = new RotateAnimation(-180, 0, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateDown.setDuration(200);
		mRotateDown.setFillAfter(true);

		mRotateUp = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF,
				0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateUp.setDuration(200);
		mRotateUp.setFillAfter(true);

	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		switch (ev.getAction()) {

		case MotionEvent.ACTION_DOWN:
			startY = (int) ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY == -1) {// 当用户按住头条新闻的viewpager进行下拉时,ACTION_DOWN会被viewpager消费掉,
				// 导致startY没有赋值,此处需要重新获取一下
				startY = (int) ev.getY();
			}
			
			if (mCurrentState == STATE_REFRESHING) {
				// 如果是正在刷新, 跳出循环
				break;
			}
			
			

			int endY = (int) ev.getY();
			int dy = endY - startY;
			int firstVisiblePosition = getFirstVisiblePosition();

			if (dy > 0 && firstVisiblePosition == 0) {
				int padding = dy - mHeadViewHeight;
				mHeadView.setPadding(0, padding, 0, 0);

				if (padding > 0 && mCurrentState != STATE_RELEASE_REFRESH) {
					mCurrentState = STATE_RELEASE_REFRESH;
					refreshData();

				} else if (padding < 0
						&& mCurrentState != STATE_PULL_TO_REFRESH) {
					mCurrentState = STATE_PULL_TO_REFRESH;
					refreshData();

				}
				return true;
			}
		case MotionEvent.ACTION_UP:
			startY = -1;
			if (mCurrentState == STATE_RELEASE_REFRESH) {
				mCurrentState = STATE_REFRESHING;
				refreshData();

				// 完整展示头布局
				mHeadView.setPadding(0, 0, 0, 0);
				// 4. 进行回调
				if (mListener != null) {
					mListener.Refresh();
				}

			} else if (mCurrentState == STATE_PULL_TO_REFRESH) {
				// 隐藏头布局
				mHeadView.setPadding(0, -mHeadViewHeight, 0, 0);
			}

			break;

		default:
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 根据当前状态刷新界面
	 */
	private void refreshData() {
		switch (mCurrentState) {
		case STATE_PULL_TO_REFRESH:
			tvTitle.setText("下拉刷新");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			ivArrow.startAnimation(mRotateDown);
			break;

		case STATE_RELEASE_REFRESH:
			tvTitle.setText("释放刷新");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			ivArrow.startAnimation(mRotateUp);
			break;
		case STATE_REFRESHING:
			tvTitle.setText("正在刷新");

			ivArrow.clearAnimation(); // 清除箭头动画,否则无法隐

			pbProgress.setVisibility(View.VISIBLE);
			ivArrow.setVisibility(View.INVISIBLE);
			break;

		default:
			break;
		}
	}
	/**刷新结束，收起控件
	 * @param success
	 */
	public void onRefreshComplete(boolean success){
		if (!isLoadMore) {
			mHeadView.setPadding(0, -mHeadViewHeight, 0, 0);
			mCurrentState = STATE_PULL_TO_REFRESH;
			tvTitle.setText("下拉刷新");
			pbProgress.setVisibility(View.INVISIBLE);
			ivArrow.setVisibility(View.VISIBLE);
			if (success) {// 只有刷新成功之后才更新时间
				setCurrentTime();
			}
		}else {
			//加载更多
			mFooterView.setPadding(0, -mFooterHeight, 0, 0);//隐藏布局
			isLoadMore = false;
		}
		
	}
	/**1. 下拉刷新的回调接口
	 * @author Administrator
	 *
	 */
	public interface onRefreshListener{
		public void Refresh();
		
		public void LoadMore();
	}
	/**2. 暴露接口,设置监听
	 * @param listener
	 */
	public void setOnRefreshListener(onRefreshListener listener){
		mListener = listener;
	}
	//3. 定义成员变量,接收监听对象
	private onRefreshListener mListener;
	private int mFooterHeight;
	
	private boolean isLoadMore;	// 标记是否正在加载更多

	// 滑动状态发生变化
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE ) {
			int lastVisiblePosition = getLastVisiblePosition();
			if (lastVisiblePosition == getCount() -1 && !isLoadMore) {
				//当前显示的是最后一个item并且没有正在加载更多
				// 到底了
				System.out.println("加载更多...");
				isLoadMore =true;
				mFooterView.setPadding(0, 0, 0, 0);
				
				setSelection(getCount() -1); // 将listview显示在最后一个item上,
				// 从而加载更多会直接展示出来, 无需手动滑动

				//通知主界面加载下一页数据
				if (mListener != null) {
					mListener.LoadMore();
				}
				
			}
			
		}
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}
	

}
