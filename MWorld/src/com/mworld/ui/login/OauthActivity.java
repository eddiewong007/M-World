package com.mworld.ui.login;

import java.util.List;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.mworld.R;
import com.mworld.support.utils.GlobalContext;
import com.mworld.support.utils.Utility;
import com.mworld.weibo.api.UserAPI;
import com.mworld.weibo.entities.Account;
import com.mworld.weibo.oauth.Oauth2API;

@SuppressLint("ValidFragment")
public class OauthActivity extends SwipeBackActivity {
	private static final String TAG = OauthActivity.class.getName();

	private static boolean oauthing = false;

	@ViewInject(id = R.id.webView)
	private WebView webView;

	private MenuItem refreshItem;

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.oauthactivity_layout);
		FinalActivity.initInjectedView(this);

		ActionBar actionBar = getActionBar();
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		actionBar.setTitle(getString(R.string.oauth));

		webView.setWebViewClient(new WeiboWebViewClient());

		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setSaveFormData(false);
		settings.setSavePassword(false);
		settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		settings.setRenderPriority(WebSettings.RenderPriority.HIGH);

		CookieSyncManager.createInstance(this);
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		webView.clearCache(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.actionbar_menu_oauthactivity, menu);
		refreshItem = menu.findItem(R.id.menu_refresh);
		refresh();
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.menu_refresh:
			refresh();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public void refresh() {
		webView.clearView();
		webView.loadUrl("about:blank");
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		ImageView iv = (ImageView) inflater.inflate(
				R.layout.refresh_action_view, null);

		Animation rotation = AnimationUtils.loadAnimation(this, R.anim.refresh);
		iv.startAnimation(rotation);

		refreshItem.setActionView(iv);
		webView.loadUrl(Oauth2API.fetchAuthorizeUrl());
	}

	private void completeRefresh() {
		if (refreshItem.getActionView() != null) {
			refreshItem.getActionView().clearAnimation();
			refreshItem.setActionView(null);
		}
	}

	private class WeiboWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			if (url.startsWith(Oauth2API.REDIRECT_URL0)) {
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
			if (!url.equals("about:blank")) {
				completeRefresh();
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
			if (!oauthing) {
				handleOauthFailure();
				return;
			}
			super.onSuccess(jsonString);
			Intent intent = new Intent();
			Account account = Account.parse(jsonString);
			if (account == null || TextUtils.isEmpty(account.getUid())) {
				handleOauthFailure();
				return;
			}
			setResult(RESULT_OK, intent);
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
				fd.update(mAccount, "uid=\'" + mAccount.getUid() + "\'");
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
		Toast.makeText(OauthActivity.this, getString(R.string.oauth_failed),
				Toast.LENGTH_SHORT).show();
		finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		if (webView.canGoBack()) {
			webView.goBack();
		} else {
			Toast.makeText(OauthActivity.this,
					getString(R.string.you_cancel_login), Toast.LENGTH_SHORT)
					.show();
			finish();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (isFinishing()) {
			webView.stopLoading();
		}
	}

	@SuppressLint("ValidFragment")
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
			OauthActivity.this.finish();
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
