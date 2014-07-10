package com.mworld.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.mworld.adapter.StatusesListAdapter;
import com.mworld.ui.R;
import com.mworld.utils.PreUtils;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.weibo.api.StatusesAPI;
import com.weibo.entities.AccessToken;
import com.weibo.entities.Status;
import com.weibo.entities.StatusesList;

public class HomeFragment extends Fragment {

	private AccessToken mAccessToken;

	public StatusesAPI mStatusesAPI;

	public ArrayList<Status> mArrayList;

	private StatusesListAdapter mAdapter;

	public long since_id = 0;

	public long init_id = 0;

	private int page = 1;

	private PullToRefreshListView mList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAccessToken = PreUtils.readAccessToken(getActivity());
		mStatusesAPI = new StatusesAPI(mAccessToken);
		mArrayList = new ArrayList<Status>();
		mAdapter = new StatusesListAdapter(getActivity(), mArrayList);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, null);
		mList = (PullToRefreshListView) view.findViewById(R.id.home_timeline);
		mList.setAdapter(mAdapter);
		mList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				mStatusesAPI.friendsTimeline(since_id, 0, 20, 1, false, 0,
						false, new RefreshHandler());
			}
		});
		mStatusesAPI.friendsTimeline(since_id, 0, 20, 1, false, 0, false,
				new RefreshHandler());
		mList.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(getActivity(), "正在加载微博", Toast.LENGTH_SHORT)
						.show();
				mStatusesAPI.friendsTimeline(0, init_id, 20, page++, false, 0,
						false, new LoadHandler());
			}
		});
		return view;
	}

	public class RefreshHandler implements RequestListener {

		@Override
		public void onComplete(String jsonString) {

			StatusesList statusList = new StatusesList();
			try {
				statusList = StatusesList.parse(jsonString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (statusList.statusesList == null
					|| statusList.statusesList.isEmpty()) {
				Toast.makeText(getActivity(), "没有更新的微博", Toast.LENGTH_SHORT)
						.show();
			} else {
				if (0 == init_id)
					init_id = statusList.statusesList.get(0).id;
				since_id = statusList.statusesList.get(0).id;
				mArrayList.addAll(0, statusList.statusesList);
				mAdapter.notifyDataSetChanged();
			}

			mList.onRefreshComplete();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Log.e("-------------------", e.getMessage());
		}

	}

	public class LoadHandler implements RequestListener {

		@Override
		public void onComplete(String jsonString) {

			StatusesList statusList = new StatusesList();
			try {
				statusList = StatusesList.parse(jsonString);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (statusList.statusesList == null
					|| statusList.statusesList.isEmpty()) {
				Toast.makeText(getActivity(), "没有更新的微博", Toast.LENGTH_SHORT)
						.show();
				mList.onRefreshComplete();
				return;
			}
			if (0 == init_id)
				init_id = statusList.statusesList.get(0).id;
			since_id = statusList.statusesList.get(0).id;
			mArrayList.addAll(statusList.statusesList);
			mAdapter.notifyDataSetChanged();
			mList.onRefreshComplete();
			Toast.makeText(getActivity(), "加载了20条微博", Toast.LENGTH_SHORT)
					.show();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Log.e("-------------------", e.getMessage());
		}

	}
}
