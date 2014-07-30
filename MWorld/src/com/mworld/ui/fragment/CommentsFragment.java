package com.mworld.ui.fragment;

import java.util.ArrayList;

import org.w3c.dom.Comment;

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
import com.mworld.R;
import com.mworld.ui.adapter.MyCmtListAdapter;
import com.mworld.ui.handler.MyCmtLoadHandler;
import com.mworld.ui.handler.MyCmtRefHandler;
import com.mworld.weibo.api.CommentAPI;
import com.mworld.weibo.entities.Account;
import com.mworld.weibo.entities.User;

public class CommentsFragment extends BaseFragment {

	@SuppressWarnings("unused")
	private Account mAccount;

	@SuppressWarnings("unused")
	private User mUser;

	private String mToken;

	public static CommentsFragment newInstance(Account account, User user,
			String token) {
		CommentsFragment fragment = new CommentsFragment(account, user, token);
		fragment.setArguments(new Bundle());
		return fragment;
	}

	public CommentsFragment(Account account, User user, String token) {
		mAccount = account;
		mUser = user;
		mToken = token;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mAPI = new CommentAPI(mToken);
		mArrayList = new ArrayList<Comment>();
		mAdapter = new MyCmtListAdapter(getActivity(), mArrayList);
	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_comment, null);
		mList = (PullToRefreshListView) view
				.findViewById(R.id.comment_timeline);
		mList.setAdapter(mAdapter);
		((CommentAPI) mAPI).timeline(since_id, 0, 20, 1, false,
				new MyCmtRefHandler(this));
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();

		mList.setOnRefreshListener(new OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				Log.i("At", "refresh");
				((CommentAPI) mAPI).timeline(since_id, 0, 20, 1, false,
						new MyCmtRefHandler(CommentsFragment.this));
			}
		});
		mList.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

			@Override
			public void onLastItemVisible() {
				Toast.makeText(getActivity(), "正在加载微博", Toast.LENGTH_SHORT)
						.show();
				((CommentAPI) mAPI).timeline(0, init_id, 20, page++, false,
						new MyCmtLoadHandler(CommentsFragment.this));
			}
		});
	}
}
