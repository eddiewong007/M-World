package com.mworld.weibo.oauth;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * 此类封装了授权借口。
 * 
 * @author MengMeng
 * 
 */
public class Oauth2API implements OauthConstants {

	/** 微博授权服务的地址 */
	private static final String API_SERVER = "https://api.weibo.com/oauth2";
	/** POST 请求方式 */
	protected static final String HTTPMETHOD_POST = "POST";
	/** GET 请求方式 */
	protected static final String HTTPMETHOD_GET = "GET";
	/** HTTP 参数 */
	protected static final String KEY_ACCESS_TOKEN = "access_token";

	/**
	 * 获取用户验证时需要引导用户跳转的url,通过用户验证后加载回调页
	 * 
	 * @return url
	 */
	public static String fetchAuthorizeUrl() {
		return API_SERVER + "/authorize" + "?client_id=" + APP_KEY0
				+ "&redirect_uri=" + REDIRECT_URL0 + "&scope=" + SCOPE
				+ "&response_type=code&display=mobile";
	}

	/**
	 * 获取授权过的Access Token
	 * 
	 * @param code
	 *            授权完成之后回调页返回的参数，用来获取access token
	 * @param callBack
	 *            异步请求回调接口
	 */
	public static void accessToken(String code, AjaxCallBack<String> callBack) {
		AjaxParams params = new AjaxParams();
		params.put("client_id", APP_KEY0);
		params.put("client_secret", APP_SECRET0);
		params.put("grant_type", GRANT_TYPE);
		params.put("code", code);
		params.put("redirect_uri", REDIRECT_URL0);
		new FinalHttp().post(API_SERVER + "/access_token", params, callBack);
	}

	/**
	 * 查询用户access_token的授权相关信息
	 * 
	 * @param token
	 *            访问令牌
	 * @param callBack
	 *            异步请求回调接口
	 */
	public static void getTokenInfo(String sccessToken,
			AjaxCallBack<String> callBack) {
		AjaxParams params = new AjaxParams();
		params.put(KEY_ACCESS_TOKEN, sccessToken);
		new FinalHttp().post(API_SERVER + "/get_token_info", params, callBack);
	}

	/**
	 * 授权回收接口，帮助开发者主动取消用户的授权
	 * <p>
	 * 应用场景
	 * </p>
	 * <p>
	 * 1.应用下线时，清空所有用户的授权
	 * </p>
	 * <p>
	 * 2.应用新上线了功能，需要取得用户scope权限，可以回收后重新引导用户授权
	 * </p>
	 * <p>
	 * 3.开发者调试应用，需要反复调试授权功能
	 * </p>
	 * <p>
	 * 4.应用内实现类似登出微博帐号的功能
	 * </p>
	 * 
	 * @param token
	 *            访问令牌
	 * @param callBack
	 *            异步请求回调接口，格式为：{"result":"true" }
	 */
	public static void revokeOauth2(String accessToken,
			AjaxCallBack<String> callBack) {
		AjaxParams params = new AjaxParams();
		params.put(KEY_ACCESS_TOKEN, accessToken);
		new FinalHttp().post(API_SERVER + "/revokeOauth2", params, callBack);
	}

	/**
	 * 通过用户名密码验证
	 * 
	 * @param context
	 *            上下文对象
	 * @param username
	 *            用户名
	 * @param password
	 *            密码
	 * @param callBack
	 *            异步回调接口
	 */
	@SuppressLint("SetJavaScriptEnabled")
	public static void authorize(final Context context, final String username,
			final String password, final AjaxCallBack<String> callBack) {
		WebView webView = new WebView(context);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setSupportZoom(true);
		webSettings.setBuiltInZoomControls(true);
		webView.setWebViewClient(new WeiboWebViewClient(username, password,
				callBack));
		webView.setWebChromeClient(new WeiboWebChromeClient(context));
		webView.loadUrl(Oauth2API.fetchAuthorizeUrl());
	}

	private static class WeiboWebChromeClient extends WebChromeClient {

		private Context mContext;

		public WeiboWebChromeClient(Context context) {
			mContext = context;
		}

		@Override
		public boolean onJsAlert(WebView view, String url, String message,
				JsResult result) {
			if (message.equals("oauth_failed")) {
				Toast.makeText(mContext, "用户名/密码错误", Toast.LENGTH_SHORT).show();
			}
			result.confirm();
			return true;
		}
	}

	private static class WeiboWebViewClient extends WebViewClient {

		private boolean oauthFlag = false;
		private boolean reload = false;
		private String username, password;
		private AjaxCallBack<String> mCallBack;

		public WeiboWebViewClient(String username, String password,
				AjaxCallBack<String> callBack) {
			super();
			this.username = username;
			this.password = password;
			mCallBack = callBack;
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
			if (reUrl.equals(API_SERVER + "/authorize") && !reload) {
				view.loadUrl(String.format(JS_FUNCTION, SECOND_SUBMIT));
				reload = true;
				return;
			}
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			processUrl(view, url);
		}

		private void processUrl(WebView view, String url) {
			if (url.startsWith(REDIRECT_URL)) {
				String code = "code=";
				int codeStart = url.indexOf(code) + code.length();
				code = url.substring(codeStart);
				if (null == code || TextUtils.isEmpty(code)) {
					Context context = view.getContext();
					Toast.makeText(context, "未能获取code", Toast.LENGTH_SHORT)
							.show();
				} else {
					accessToken(code, mCallBack);
				}
			}
		}
	}

}
