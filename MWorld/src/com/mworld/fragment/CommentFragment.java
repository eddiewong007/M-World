package com.mworld.fragment;

import java.util.ArrayList;

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
import com.mworld.adapter.MyComListAdapter;
import com.mworld.handler.MyComLoadHandler;
import com.mworld.handler.MyComRefHandler;
import com.mworld.ui.R;
import com.weibo.api.CommentsAPI;
import com.weibo.entities.Comment;

public class CommentFragment extends BaseFragment {

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAPI = new CommentsAPI(mAccessToken);
		mArrayList = new ArrayList<Comment>();
		mAdapter = new MyComListAdapter(getActivity(), mArrayList);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_comment, null);
		mList = (PullToRefreshListView) view
				.findViewById(R.id.comment_timeline);
		mList.setAdapter(mAdapter);
		((CommentsAPI) mAPI).timeline(since_id, 0, 20, 1, false,
				new MyComRefHandler(this));
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();

		mList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				Log.i("At", "refresh");
				((CommentsAPI) mAPI).timeline(since_id, 0, 20, 1, false,
						new MyComRefHandler(CommentFragment.this));
			}
		});
		mList.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(getActivity(), "正在加载微博", Toast.LENGTH_SHORT)
						.show();
				((CommentsAPI) mAPI).timeline(0, init_id, 20, page++, false,
						new MyComLoadHandler(CommentFragment.this));
			}
		});
	}
}
