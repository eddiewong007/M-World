package com.mworld.ui.login;

import java.util.List;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mworld.R;
import com.mworld.support.utils.GlobalContext;
import com.mworld.support.utils.Utility;
import com.mworld.weibo.api.UserAPI;
import com.mworld.weibo.entities.Account;
import com.mworld.weibo.oauth.Oauth2API;
import com.mworld.weibo.oauth.OauthConstants;

public class LoginActivity extends FragmentActivity implements OauthConstants {

	private static final String TAG = LoginActivity.class.getName();

	private static boolean oauthing = false;

	private WebView mWebView;

	@ViewInject(id = R.id.username)
	private EditText username;
	@ViewInject(id = R.id.passworld)
	private EditText password;
	@ViewInject(id = R.id.login, click = "login")
	private Button loginBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginactivity_layout);
		FinalActivity.initInjectedView(this);

	}

	@SuppressLint("SetJavaScriptEnabled")
	public void login(View view) {
		String name = username.getEditableText().toString();
		String pass = password.getEditableText().toString();
		if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pass)) {
			Toast.makeText(LoginActivity.this, "请输入用户名/密码", Toast.LENGTH_SHORT)
					.show();
		}
		mWebView = new WebView(this);
		mWebView.setWebViewClient(new WeiboWebViewClient(name, pass));
		mWebView.setWebChromeClient(new WeiboWebChromeClient());
		mWebView.loadUrl(Oauth2API.fetchAuthorizeUrl());
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);

		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}

	private class WeiboWebChromeClient extends WebChromeClient {

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			if (message.equals("oauth_failed")) {
				Toast.makeText(LoginActivity.this, "用户名/密码错误",
						Toast.LENGTH_SHORT).show();
			}
			result.confirm();
			return true;
		}
	}

	private class WeiboWebViewClient extends WebViewClient {

		private boolean oauthFlag = false;
		private boolean reload = false;
		private String username, password;

		public WeiboWebViewClient(String name, String pass) {
			username = name;
			password = pass;
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			if (url.startsWith(Oauth2API.REDIRECT_URL)) {
				handleRedirectUrl(view, url);
				view.stopLoading();
			}
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			new SinaWeiboErrorDialog().show(getSupportFragmentManager(), "");
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			String reUrl = view.getUrl();
			if (!oauthFlag) {
				if (!TextUtils.isEmpty(username)
						&& !TextUtils.isEmpty(password)) {
					String firstSubmit = String.format(FIRST_SUBMIT, username,
							password);
					view.loadUrl(String.format(JS_FUNCTION, firstSubmit));
				}
				oauthFlag = true;
			}
			if (reUrl.equals("https://api.weibo.com/oauth2/authorize")
					&& !reload) {
				view.loadUrl(String.format(JS_FUNCTION, SECOND_SUBMIT));
				reload = true;
				return;
			}
		}
	}

	private void handleRedirectUrl(WebView view, String url) {
		Bundle values = Utility.parseUrl(url);
		String code = values.getString("code");

		if (null == code || TextUtils.isEmpty(code)) {
			handleOauthFailure();
			return;
		}
		Oauth2API.accessToken(code, new TokenHandler());
		new ProgressFragment().show(getSupportFragmentManager(), "");
		oauthing = true;
	}

	private class TokenHandler extends AjaxCallBack<String> {

		@Override
		public void onSuccess(String jsonString) {
			super.onSuccess(jsonString);
			if (!oauthing) {
				handleOauthFailure();
				return;
			}
			Account account = Account.parse(jsonString);
			if (account == null || TextUtils.isEmpty(account.getUid())) {
				handleOauthFailure();
				return;
			}
			setResult(RESULT_OK, new Intent());
			new UserAPI(account.getAccessToken()).show(Long.parseLong(account
					.getUid()), new AccountHandler(account));

		}

		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			handleOauthFailure();
			Log.e(TAG, strMsg);
		}

	}

	private class AccountHandler extends AjaxCallBack<String> {

		private Account mAccount;

		AccountHandler(Account account) {
			mAccount = account;
		}

		@Override
		public void onSuccess(String jsonString) {
			super.onSuccess(jsonString);
			if (!oauthing) {
				handleOauthFailure();
				return;
			}
			FinalDb fd = FinalDb.create(GlobalContext.getInstance(), true);
			List<Account> accounts = fd.findAllByWhere(Account.class, "uid=\'"
					+ mAccount.getUid() + "\'");
			mAccount.setJsonUserInfo(jsonString);
			if (accounts.isEmpty()) {
				fd.save(mAccount);
			} else {
				fd.update(mAccount, "uid=" + mAccount.getUid());
			}
			finish();
		}

		@Override
		public void onFailure(Throwable t, int errorNo, String strMsg) {
			super.onFailure(t, errorNo, strMsg);
			handleOauthFailure();
			Log.e(TAG, strMsg);
		}

	}

	private void handleOauthFailure() {
		Toast.makeText(LoginActivity.this, getString(R.string.oauth_failed),
				Toast.LENGTH_SHORT).show();
		finish();
	}

	private class ProgressFragment extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {

			ProgressDialog dialog = new ProgressDialog(getActivity());
			dialog.setMessage(getString(R.string.oauthing));
			dialog.setIndeterminate(false);
			dialog.setCancelable(true);

			return dialog;
		}

		@Override
		public void onCancel(DialogInterface dialog) {
			oauthing = false;
			LoginActivity.this.finish();
			super.onCancel(dialog);
		}

	};

	public class SinaWeiboErrorDialog extends DialogFragment {

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setMessage(R.string.sina_server_error).setPositiveButton(
					R.string.ok, new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {

						}
					});
			return builder.create();
		}
	}

}