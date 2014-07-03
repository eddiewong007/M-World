package com.mworld.ui;

import net.tsz.afinal.FinalBitmap;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.mworld.utils.AccessTokenKeeper;
import com.mworld.weibo.api.UsersAPI;
import com.mworld.weibo.entities.AccessToken;
import com.mworld.weibo.entities.User;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class ProfileActivity extends Activity {
	private AccessToken mAccessToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		((TextView) findViewById(R.id.actionbar_title)).setText("个人信息");
		mAccessToken = AccessTokenKeeper.readAccessToken(this);
		UsersAPI usersAPI = new UsersAPI(mAccessToken);
		usersAPI.show(Long.parseLong(getIntent().getStringExtra("uid")), new RequestListener() {

			@Override
			public void onWeiboException(WeiboException e) {

			}

			@Override
			public void onComplete(String jsonString) {
				User user = User.parse(jsonString);
				FinalBitmap.create(ProfileActivity.this).display(
						(ImageView) findViewById(R.id.my_icon),
						user.avatar_large);
				((TextView) findViewById(R.id.my_name)).setText("昵称："
						+ user.screen_name);
				((TextView) findViewById(R.id.my_fans))
						.setText("关注：" + user.friends_count + "    粉丝："
								+ user.followers_count);
			}
		});

	}

}
