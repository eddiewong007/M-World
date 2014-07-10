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

public class AtFragment extends Fragment {

	private AccessToken mAccessToken;

	private StatusesAPI mStatusesAPI;

	private ArrayList<Status> mArrayList;

	private StatusesListAdapter mAdapter;

	private long since_id = 0;

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
		View view = inflater.inflate(R.layout.fragment_at, null, false);
		mList = (PullToRefreshListView) view.findViewById(R.id.at_timeline);

		mList.setAdapter(mAdapter);
		mList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				mStatusesAPI
						.mentions(since_id, 0, 10, 1,
								StatusesAPI.AUTHOR_FILTER_ALL,
								StatusesAPI.SRC_FILTER_ALL,
								StatusesAPI.TYPE_FILTER_ALL, false,
								new StatusHandler());
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
