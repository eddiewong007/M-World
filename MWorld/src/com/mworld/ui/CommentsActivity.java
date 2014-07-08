package com.mworld.ui;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mworld.adapter.CommentsListAdapter;
import com.mworld.utils.AccessTokenKeeper;
import com.mworld.weibo.api.CommentsAPI;
import com.mworld.weibo.api.StatusesAPI;
import com.mworld.weibo.entities.CommentsList;
import com.mworld.weibo.entities.Status;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

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
		StatusesAPI statusesAPI = new StatusesAPI(
				AccessTokenKeeper.readAccessToken(this));
		statusesAPI.show(getIntent().getLongExtra("id", 0),
				new RequestListener() {

					@Override
					public void onWeiboException(WeiboException arg0) {

					}

					@Override
					public void onComplete(String jsonString) {
						Log.i("-------------回调", jsonString);
						mStatus = Status.parse(jsonString);
						ImageView userAvatar = (ImageView) findViewById(R.id.user_avatar);
						FinalBitmap.create(CommentsActivity.this).display(
								userAvatar, mStatus.user.avatar_large);
						userAvatar.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								Log.i("adapter", "click");
								Intent intent = new Intent(
										CommentsActivity.this,
										DisplayActivity.class);
								intent.putExtra("type", 3);
								intent.putExtra("uid",
										String.valueOf(mStatus.user.id));
								startActivity(intent);
							}
						});
						((TextView) findViewById(R.id.user_name))
								.setText(mStatus.user.screen_name);
						((TextView) findViewById(R.id.date))
								.setText(mStatus.created_at);
						((TextView) findViewById(R.id.text_status))
								.setText(mStatus.text);
						if (null != mStatus.retweeted_status) {
							((TextView) findViewById(R.id.text_repost))
									.setText("@"
											+ mStatus.retweeted_status.user.screen_name
											+ ":"
											+ mStatus.retweeted_status.text);
							((TextView) findViewById(R.id.repost_count))
									.setText("转发 "
											+ mStatus.retweeted_status.reposts_count
											+ " 评论 "
											+ mStatus.retweeted_status.comments_count);
						}
						((TextView) findViewById(R.id.ret_count))
								.setText(String.valueOf(mStatus.reposts_count));
						((TextView) findViewById(R.id.com_count))
								.setText(String.valueOf(mStatus.comments_count));
					}
				});

	}

	private void loadComments() {
		CommentsAPI commentsAPI = new CommentsAPI(
				AccessTokenKeeper.readAccessToken(this));
		long id = getIntent().getLongExtra("id", 0);
		commentsAPI.show(id, 0, 0, 10, 1, 0, new RequestListener() {

			@Override
			public void onWeiboException(WeiboException arg0) {

			}

			@Override
			public void onComplete(String jsonString) {
				CommentsList commentsList = CommentsList.parse(jsonString);
				if (0 == commentsList.commentList.size())
					((TextView) findViewById(R.id.no_comment)).setText("没有评论");
				CommentsListAdapter adapter = new CommentsListAdapter(
						CommentsActivity.this, commentsList.commentList);

				((ListView) findViewById(R.id.comments_list))
						.setAdapter(adapter);
			}
		});
	}

}
