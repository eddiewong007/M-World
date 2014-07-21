package com.mworld.ui.main;

import java.util.ArrayList;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import com.mworld.R;
import com.mworld.support.utils.GlobalContext;
import com.mworld.ui.adapter.StatusCmtListAdapter;
import com.mworld.weibo.api.CommentAPI;
import com.mworld.weibo.entities.CommentList;
import com.mworld.weibo.entities.Status;

public class CommentActivity extends Activity {

	private Status mStatus;

	@ViewInject(id = R.id.comments_list)
	private ListView mListView;

	@SuppressWarnings("rawtypes")
	private ArrayList mArrayList = new ArrayList();

	private StatusCmtListAdapter mAdapter;

	private CommentAPI mCommentsAPI;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commentactivity_layout);
		FinalActivity.initInjectedView(this);

		mStatus = (Status) getIntent().getParcelableExtra("status");
		mArrayList.add(mStatus);
		// mArrayList.add(GlobalContext.getInstance().getAccount().getUserInfo());
		mAdapter = new StatusCmtListAdapter(this, mArrayList);
		mListView.setAdapter(mAdapter);
		mCommentsAPI = new CommentAPI(GlobalContext.getInstance().getAccount()
				.getAccessToken());
		loadComments();
	}

	private void loadComments() {

		mCommentsAPI.show(mStatus.id, 0, 0, 10, 1, 0,
				new AjaxCallBack<String>() {

					@SuppressWarnings("unchecked")
					@Override
					public void onSuccess(String jsonString) {
						super.onSuccess(jsonString);
						CommentList commentList = CommentList.parse(jsonString);
						mArrayList.addAll(commentList.comments);
						mAdapter.notifyDataSetChanged();
					}

				});
	}

}
