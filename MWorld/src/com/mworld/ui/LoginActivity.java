package com.mworld.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.mworld.utils.AccessTokenKeeper;
import com.mworld.weibo.api.Oauth2API;
import com.mworld.weibo.entities.AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class LoginActivity extends Activity {

	public static LoginActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		judgeIsLoggedIn();
	}

	@SuppressLint("SetJavaScriptEnabled")
	public void gologin(View v) {
		String username = ((EditText) findViewById(R.id.editText1))
				.getEditableText().toString();
		String password = ((EditText) findViewById(R.id.editText2))
				.getEditableText().toString();
		if (TextUtils.isEmpty(username)||TextUtils.isEmpty(password)){
			Toast.makeText(this, "输入用户名密码", Toast.LENGTH_SHORT).show();
		}
		Oauth2API.authorize(this, username, password, new TokenHandler());

	}

	public class TokenHandler implements RequestListener {

		@Override
		public void onComplete(String jsonString) {

			Log.i("------------------", "回调");
			AccessToken accessToken = AccessToken.parse(jsonString);
			if (null == accessToken) {
				Toast.makeText(LoginActivity.this, "授权失败，请重新授权！",
						Toast.LENGTH_SHORT).show();
				String username = ((EditText) findViewById(R.id.editText1))
						.getEditableText().toString();
				String password = ((EditText) findViewById(R.id.editText2))
						.getEditableText().toString();
				Intent intent = new Intent(LoginActivity.this,
						OauthActivity.class);
				intent.putExtra("username", username);
				intent.putExtra("password", password);
				startActivity(intent);
				finish();
			} else {
				AccessTokenKeeper.keepAccessToken(LoginActivity.this,
						accessToken);
				startActivity(new Intent(LoginActivity.this,
						MworldActivity.class));
				finish();
			}

		}

		@Override
		public void onWeiboException(WeiboException e) {
			Log.e("----------------------", e.getMessage());
		}

	}

	public void login(View v) {
		String username = ((EditText) findViewById(R.id.editText1))
				.getEditableText().toString();
		String password = ((EditText) findViewById(R.id.editText2))
				.getEditableText().toString();
		Intent intent = new Intent(LoginActivity.this, OauthActivity.class);
		intent.putExtra("username", username);
		intent.putExtra("password", password);
		startActivity(intent);
		finish();

	}

	private void judgeIsLoggedIn() {

		if (null == AccessTokenKeeper.readAccessToken(LoginActivity.this)) {
			instance = this;
		} else {
			Intent intent = new Intent(LoginActivity.this, MworldActivity.class);
			startActivity(intent);
		}
	}

}
