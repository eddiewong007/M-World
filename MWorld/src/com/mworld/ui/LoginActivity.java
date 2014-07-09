package com.mworld.ui;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mworld.utils.PreUtils;
import com.mworld.weibo.api.Oauth2API;
import com.mworld.weibo.entities.AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

public class LoginActivity extends Activity {

	@ViewInject(id = R.id.username_edit)
	EditText mUsername;
	@ViewInject(id = R.id.password_edit)
	EditText mPassword;
	@ViewInject(id = R.id.signin_button, click = "login")
	Button mButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		FinalActivity.initInjectedView(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (null != PreUtils.readAccessToken(this)) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

	public void login(View v) {
		String username = mUsername.getEditableText().toString();
		String password = mPassword.getEditableText().toString();
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			Toast.makeText(this, "输入用户名密码", Toast.LENGTH_SHORT).show();
			return;
		}
		Oauth2API.authorize(this, username, password, new TokenHandler());

	}

	public class TokenHandler implements RequestListener {

		@Override
		public void onComplete(String jsonString) {

			AccessToken accessToken = AccessToken.parse(jsonString);
			if (null == accessToken) {
				Toast.makeText(LoginActivity.this, "授权失败，请重新授权！",
						Toast.LENGTH_SHORT).show();
			} else {
				PreUtils.keepAccessToken(LoginActivity.this, accessToken);
				startActivity(new Intent(LoginActivity.this, MainActivity.class));
				finish();
			}
		}

		@Override
		public void onWeiboException(WeiboException e) {
			Toast.makeText(LoginActivity.this, "授权失败，请重新授权！",
					Toast.LENGTH_SHORT).show();
		}

	}

}
