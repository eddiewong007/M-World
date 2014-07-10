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
import com.weibo.api.StatusesAPI;

public class AtFragment extends BaseFragment {

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_at, null, false);
		mList = (PullToRefreshListView) view.findViewById(R.id.at_timeline);
		mStatusesAPI.mentions(since_id, 0, 20, page++,
				StatusesAPI.AUTHOR_FILTER_ALL, StatusesAPI.SRC_FILTER_ALL,
				StatusesAPI.TYPE_FILTER_ALL, false, new StatusRefreshHandler(
						AtFragment.this));
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		mList.setAdapter(mAdapter);
		mList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				Log.i("At", "refresh");
				mStatusesAPI.mentions(since_id, 0, 20, 1,
						StatusesAPI.AUTHOR_FILTER_ALL,
						StatusesAPI.SRC_FILTER_ALL,
						StatusesAPI.TYPE_FILTER_ALL, false,
						new StatusRefreshHandler(AtFragment.this));
			}
		});
		mList.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(getActivity(), "正在加载微博", Toast.LENGTH_SHORT)
						.show();
				mStatusesAPI.mentions(0, init_id, 20, page++,
						StatusesAPI.AUTHOR_FILTER_ALL,
						StatusesAPI.SRC_FILTER_ALL,
						StatusesAPI.TYPE_FILTER_ALL, false,
						new StatusLoadHandler(AtFragment.this));
			}
		});
	}

}
