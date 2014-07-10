package com.mworld.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
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
import com.mworld.handler.StatusLoadHandler;
import com.mworld.handler.StatusRefreshHandler;
import com.mworld.ui.R;

public class HomeFragment extends BaseFragment {

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home, null);
		mList = (PullToRefreshListView) view.findViewById(R.id.home_timeline);
		mStatusesAPI.friendsTimeline(since_id, 0, 20, 1, false, 0, false,
				new StatusRefreshHandler(HomeFragment.this));
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		mList.setAdapter(mAdapter);
		mList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				Log.i("Home", "refresh");
				mStatusesAPI.friendsTimeline(since_id, 0, 20, 1, false, 0,
						false, new StatusRefreshHandler(HomeFragment.this));
			}
		});
		mList.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(getActivity(), "正在加载微博", Toast.LENGTH_SHORT)
						.show();
				mStatusesAPI.friendsTimeline(0, init_id, 20, page++, false, 0,
						false, new StatusLoadHandler(HomeFragment.this));
			}
		});

	}

}
