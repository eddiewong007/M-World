package com.mworld.ui.main;

import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.mworld.R;
import com.mworld.support.utils.GlobalContext;
import com.mworld.ui.adapter.StatusCmtListAdapter;
import com.mworld.ui.holder.StatusHolder;
import com.mworld.weibo.api.CommentAPI;
import com.mworld.weibo.entities.CommentList;
import com.mworld.weibo.entities.Status;

public class CommentActivity extends Activity {

	private Status mStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.commentactivity_layout);
		loadStatus();
		loadComments();
	}

	private void loadStatus() {
		mStatus = (Status) getIntent().getParcelableExtra("status");
		StatusHolder holder = new StatusHolder(this, findViewById(R.id.status));
		holder.inflate(mStatus);
	}

	private void loadComments() {
		CommentAPI commentsAPI = new CommentAPI(GlobalContext.getInstance()
				.getAccount().getAccessToken());
		long id = getIntent().getLongExtra("id", 0);
		commentsAPI.show(id, 0, 0, 10, 1, 0, new AjaxCallBack<String>() {

			@Override
			public void onSuccess(String jsonString) {
				super.onSuccess(jsonString);
				CommentList commentsList = CommentList.parse(jsonString);
				if (0 == commentsList.comments.size())
					((TextView) findViewById(R.id.no_comment)).setText("没有评论");
				StatusCmtListAdapter adapter = new StatusCmtListAdapter(
						CommentActivity.this, commentsList.comments);

				((ListView) findViewById(R.id.comments_list))
						.setAdapter(adapter);
			}

		});
	}

}
