package com.mworld.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.mworld.utils.AccessTokenKeeper;
import com.mworld.weibo.api.UsersAPI;
import com.mworld.weibo.entities.AccessToken;
import com.mworld.weibo.entities.User;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class MworldActivity extends Activity {
	AccessToken mAccessToken = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mworld);
		initComponents();
	}

	private void initComponents() {
		mAccessToken = AccessTokenKeeper.readAccessToken(this);
	}

	public void name(View v) {

		UsersAPI usersAPI = new UsersAPI(mAccessToken);
		usersAPI.show(Long.parseLong(mAccessToken.uid), new RequestListener() {

			@Override
			public void onComplete(String jsonString) {
				User user = User.parse(jsonString);
				((TextView) findViewById(R.id.tv_name))
						.setText(user.screen_name);
			}

			@Override
			public void onWeiboException(WeiboException arg0) {

			}

		});
	}
}
