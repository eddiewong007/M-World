package com.mworld.ui;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.mworld.adapter.StatusCmtListAdapter;
import com.mworld.holder.StatusHolder;
import com.mworld.utils.PrefUtils;
import com.weibo.api.CommentsAPI;
import com.weibo.entities.CommentsList;
import com.weibo.entities.Status;

public class CommentsActivity extends Activity {

	private Status mStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comments);
		loadStatus();
		loadComments();
	}

	private void loadStatus() {
		mStatus = (Status) getIntent().getSerializableExtra("status");
		StatusHolder holder = new StatusHolder(this, findViewById(R.id.status));
		holder.inflate(mStatus);
	}

	private void loadComments() {
		CommentsAPI commentsAPI = new CommentsAPI(
				PrefUtils.readAccessToken(this));
		long id = getIntent().getLongExtra("id", 0);
		commentsAPI.show(id, 0, 0, 10, 1, 0, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String jsonString) {
				super.onSuccess(jsonString);
				CommentsList commentsList = CommentsList.parse(jsonString);
				if (0 == commentsList.commentsList.size())
					((TextView) findViewById(R.id.no_comment)).setText("没有评论");
				StatusCmtListAdapter adapter = new StatusCmtListAdapter(
						CommentsActivity.this, commentsList.commentsList);

				((ListView) findViewById(R.id.comments_list))
						.setAdapter(adapter);
			}

		});
	}

}
