package com.mworld.fragment;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.markupartist.android.widget.PullToRefreshListView;
import com.markupartist.android.widget.PullToRefreshListView.OnRefreshListener;
import com.mworld.adapter.StatusesListAdapter;
import com.mworld.ui.R;
import com.mworld.utils.PreUtils;
import com.mworld.weibo.api.StatusesAPI;
import com.mworld.weibo.entities.AccessToken;
import com.mworld.weibo.entities.Status;
import com.mworld.weibo.entities.StatusesList;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class HomeFragment extends Fragment {

	private AccessToken mAccessToken;

	private StatusesAPI mStatusesAPI;

	private ArrayList<Status> mArrayList;

	private StatusesListAdapter mAdapter;

	private long since_id = 0;

	private PullToRefreshListView mList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, null);
		mList = (PullToRefreshListView) view.findViewById(R.id.home_timeline);
		mAccessToken = PreUtils.readAccessToken(getActivity());
		mStatusesAPI = new StatusesAPI(mAccessToken);
		mArrayList = new ArrayList<Status>();
		mAdapter = new StatusesListAdapter(getActivity(), mArrayList);
		mList.setAdapter(mAdapter);
		mList.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				mStatusesAPI.friendsTimeline(since_id, 0, 10, 1, false, 0,
						false, new StatusHandler());
			}
		});
		return view;
	}

	public class StatusHandler implements RequestListener {

		@Override
		public void onComplete(String jsonString) {
			Log.i("--------------------------", "回调");

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
			since_id = statusList.statusesList.get(0).id;
			mArrayList.addAll(0, statusList.statusesList);
			mAdapter.notifyDataSetChanged();
			mList.onRefreshComplete();
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Log.e("-------------------", e.getMessage());
		}

	}
}
