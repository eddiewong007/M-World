package com.mworld.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleAdapter.ViewBinder;
import android.widget.TextView;

import com.mworld.utils.AccessTokenKeeper;
import com.mworld.weibo.api.StatusesAPI;
import com.mworld.weibo.api.UsersAPI;
import com.mworld.weibo.entities.AccessToken;
import com.mworld.weibo.entities.Status;
import com.mworld.weibo.entities.StatusList;
import com.mworld.weibo.entities.User;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class MworldActivity extends Activity {
	private AccessToken mAccessToken = null;
	private TextView mTvName;
	private ListView mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mworld);
		initComponents();
	}

	private void initComponents() {
		mAccessToken = AccessTokenKeeper.readAccessToken(MworldActivity.this);
		((TextView) findViewById(R.id.actionbar_title)).setText("M-World");
		mTvName = (TextView) findViewById(R.id.tv_name);
		mList = (ListView) findViewById(R.id.status_list);
	}

	public void name(View v) {

		UsersAPI usersAPI = new UsersAPI(mAccessToken);
		usersAPI.show(Long.parseLong(mAccessToken.uid), new RequestListener() {

			@Override
			public void onComplete(String jsonString) {
				final User user = User.parse(jsonString);
				mTvName.post(new Runnable() {

					@Override
					public void run() {
						mTvName.setText(user.screen_name);
					}

				});

			}

			@Override
			public void onWeiboException(WeiboException arg0) {

			}

		});

	}

	public void status(View v) {
		StatusesAPI statusesAPI = new StatusesAPI(mAccessToken);
		statusesAPI.publicTimeline(10, new RequestListener() {

			@Override
			public void onComplete(String jsonString) {
				Log.d("--------------", "回调");
				StatusList statusList = StatusList.parse(jsonString);
				final List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
				for (Status status : statusList.statusList) {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("Icon", status.user.profile_image_url);
					map.put("Name", status.user.screen_name);
					map.put("Date", status.created_at);
					map.put("Text", status.text);
					map.put("repost", "转发：" + status.reposts_count);
					map.put("comment", "评论" + status.comments_count);
					data.add(map);
				}
				mList.post(new Runnable() {

					@Override
					public void run() {
						SimpleAdapter adapter = new SimpleAdapter(
								MworldActivity.this,
								data,
								R.layout.list_item,
								new String[] { "Icon", "Name", "Date", "Text",
										"repost", "comment" },
								new int[] { R.id.icon, R.id.name, R.id.date,
										R.id.status, R.id.repost, R.id.comment });
						mList.setAdapter(adapter);
						adapter.setViewBinder(new ViewBinder() {

							@Override
							public boolean setViewValue(View view, Object data,
									String textRepresentation) {

								if (view instanceof ImageView) {
									ImageView iv = (ImageView) view;
									FinalBitmap.create(MworldActivity.this)
											.display(iv, (String) data);
									return true;
								} else
									return false;
							}

						});
					}

				});
			}

			@Override
			public void onWeiboException(WeiboException arg0) {
				Log.d("-------------------", "请求出错");
			}

		});
	}

	public void logout(View v) {
		AccessTokenKeeper.clear(this);
		startActivity(new Intent(MworldActivity.this, LoginActivity.class));
		finish();
	}
}
