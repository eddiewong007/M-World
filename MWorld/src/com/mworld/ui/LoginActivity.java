package com.mworld.ui;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mworld.utils.PreUtils;
import com.weibo.api.Oauth2API;
import com.weibo.entities.AccessToken;

public class LoginActivity extends Activity {

	@ViewInject(id = R.id.username_edit)
	EditText mUsername;
	@ViewInject(id = R.id.password_edit)
	EditText mPassword;
	@ViewInject(id = R.id.signin_button, click = "login")
	Button mButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (null != PreUtils.readAccessToken(this)) {
			Intent intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			finish();
		}

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		FinalActivity.initInjectedView(this);
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

	public class TokenHandler extends AjaxCallBack<String> {

		@Override
		public void onSuccess(String jsonString) {
			super.onSuccess(jsonString);
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
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			Toast.makeText(LoginActivity.this, strMsg, Toast.LENGTH_SHORT)
					.show();
		}
	}

}
