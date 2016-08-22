package fragment;

import java.util.ArrayList;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itandroid.zhbj.MainActivity;
import com.itandroid.zhbj.R;
import com.itandroid.zhbj.base.impl.NewsCenterPager;
import com.itandroid.zhbj.domain.NewsMenu.NewsMenuData;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class LeftMenuFragment extends BaseFragment{
	@ViewInject(R.id.lv_list)
	private ListView lvList;
	
	private int mCurrentPos;// ��ǰ��ѡ�е�item��λ��
	
	private ArrayList<NewsMenuData> mNewsMenuDatas;	//������������ݶ���

	private LeftMenuAdapter mAdapter;
	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_left_menu, null);
//		lvList = (ListView) view.findViewById(R.id.lv_list);
		ViewUtils.inject(this,view);
		return view;
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
	}
	/**
	 * ���������������
	 * @param data 
	 */
	public void setMenuData(ArrayList<NewsMenuData> data){
		mCurrentPos = 0;//��ǰѡ�е�λ�ù���
		
		//����ҳ��
		mNewsMenuDatas = data;
		mAdapter = new LeftMenuAdapter();
		lvList.setAdapter(mAdapter);
		lvList.setOnItemClickListener(new OnItemClickListener() {

			
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mCurrentPos = position;// ���µ�ǰ��ѡ�е�λ��
				mAdapter.notifyDataSetChanged();// ˢ��listview

				// ��������
				toggle();

				// ��������֮��, Ҫ�޸��������ĵ�FrameLayout�е�����
				setCurrentDetailPager(position);
			}
		});
		
	}
	/**
	 * ���õ�ǰ�˵�����ҳ
	 * 
	 * @param position
	 */
	protected void setCurrentDetailPager(int position) {
		// ��ȡ�������ĵĶ���
		MainActivity mainUI = (MainActivity) mActivity;
		// ��ȡContentFragment
		ContentFragment fragment = mainUI.getContentFragment();
		// ��ȡNewsCenterPager
		NewsCenterPager newsCenterPager = fragment.getNewsCenterPager();
		// �޸��������ĵ�FrameLayout�Ĳ���
		newsCenterPager.setCurrentDetailPager(position);
	}
	
	/**
	 * �򿪻��߹رղ����
	 */
	protected void toggle() {
		MainActivity mainUI = (MainActivity) mActivity;
		SlidingMenu slidingMenu = mainUI.getSlidingMenu();
		slidingMenu.toggle();// �����ǰ״̬�ǿ�, ���ú�͹�; ��֮��Ȼ
	}
	
	class LeftMenuAdapter extends BaseAdapter{

		public int getCount() {
			return mNewsMenuDatas.size();
		}

		public NewsMenuData getItem(int position) {
			return mNewsMenuDatas.get(position);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mActivity, R.layout.list_item_left_menu,null);
			TextView tvMenu = (TextView) view.findViewById(R.id.tv_menu);
			
			NewsMenuData item = getItem(position);
			tvMenu.setText(item.title);
			tvMenu.setTextSize(25);
			

			if (position == mCurrentPos) {
				// ��ѡ��
				tvMenu.setEnabled(true);// ���ֱ�Ϊ��ɫ
			} else {
				// δѡ��
				tvMenu.setEnabled(false);// ���ֱ�Ϊ��ɫ
			}
			
			return view;
		}

	
		
	}

}