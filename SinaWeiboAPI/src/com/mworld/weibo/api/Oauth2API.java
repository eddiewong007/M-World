package com.mworld.weibo.api;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.mworld.weibo.constants.OauthConstants;
import com.mworld.weibo.entities.AccessToken;
import com.mworld.weibo.net.AsyncOauthRunner;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.utils.Utility;

/**
 * 
 * @author MengMeng
 * 
 */
public class Oauth2API implements OauthConstants {

	private static final String TAG = Oauth2API.class.getName();

	/** 微博授权服务的地址 */
	private static final String API_SERVER = "https://api.weibo.com/oauth2";
	/** POST 请求方式 */
	protected static final String HTTPMETHOD_POST = "POST";
	/** GET 请求方式 */
	protected static final String HTTPMETHOD_GET = "GET";
	/** HTTP 参数 */
	protected static final String KEY_ACCESS_TOKEN = "access_token";

	/**
	 * API 类型
	 */
	private static final int AUTHORIZE = 0;
	private static final int ACCESS_TOKEN = 1;
	private static final int GET_TOKEN_INFO = 2;
	private static final int REVOKE_OAUTH2 = 3;
	private static final SparseArray<String> sAPIList = new SparseArray<String>();
	static {
		sAPIList.put(AUTHORIZE, API_SERVER + "/authorize");
		sAPIList.put(ACCESS_TOKEN, API_SERVER + "/access_token");
		sAPIList.put(GET_TOKEN_INFO, API_SERVER + "/get_token_info");
		sAPIList.put(REVOKE_OAUTH2, API_SERVER + "/revokeOauth2");
	}

	/**
	 * 获取用户验证时需要引导用户跳转的url,通过用户验证后授权Token
	 * 
	 * @return
	 */
	public static String fetchAuthorizeUrl() {
		return sAPIList.get(AUTHORIZE) + "?client_id=" + APP_KEY
				+ "&redirect_uri=" + REDIRECT_URL
				+ "&response_type=code&display=mobile";

	}

	/**
	 * 获取授权过的Access Token
	 * 
	 * @param code
	 * @param listener
	 *            异步请求回调接口，onComplete方法获取token
	 */
	public static void accessToken(String code, RequestListener listener) {
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair("client_id", APP_KEY));
		postParameters.add(new BasicNameValuePair("client_secret", APP_SECRET));
		postParameters.add(new BasicNameValuePair("grant_type", GRANT_TYPE));
		postParameters.add(new BasicNameValuePair("code", code));
		postParameters
				.add(new BasicNameValuePair("redirect_uri", REDIRECT_URL));
		AsyncOauthRunner.requestAsync(sAPIList.get(ACCESS_TOKEN),
				postParameters, listener);

	}

	/**
	 * 查询用户access_token的授权相关信息
	 * 
	 * @param token
	 * @param listener
	 *            异步请求回调接口，onComplete方法获取token信息
	 */
	public static void getTokenInfo(AccessToken token, RequestListener listener) {
		if (null == token) {
			return;
		}
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair(KEY_ACCESS_TOKEN,
				token.access_token));
		AsyncOauthRunner.requestAsync(sAPIList.get(GET_TOKEN_INFO),
				postParameters, listener);
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
	 * @param listener
	 *            异步请求回调接口，onComplete方法获取取消授权的结果,格式为：{"result":"true" }
	 */
	public static void revokeOauth2(AccessToken token, RequestListener listener) {
		if (null == token) {
			return;
		}
		List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		postParameters.add(new BasicNameValuePair(KEY_ACCESS_TOKEN,
				token.access_token));
		AsyncOauthRunner.requestAsync(sAPIList.get(REVOKE_OAUTH2),
				postParameters, listener);
	}

	/**
	 * 验证页面所需要的WebViewClient 请用RequestListener回调接口来构造对象，以获得AccessToken
	 * 
	 * @author MengMeng
	 * 
	 */
	public static class WeiboWebViewClient extends WebViewClient {

		RequestListener mListener;

		public WeiboWebViewClient(RequestListener listener) {
			mListener = listener;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
			Log.e(TAG, "Argument error!");
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			if (url.startsWith(REDIRECT_URL)) {
				Bundle values = Utility.parseUrl(url);
				String code = values.getString("code");
				if (!(null == code || TextUtils.isEmpty(code))) {
					accessToken(code, mListener);
				} else {
					Context context = view.getContext();
					if (context instanceof Activity) {
						Activity activity = (Activity) context;
						Toast.makeText(context, "授权失败", Toast.LENGTH_SHORT)
								.show();
						activity.finish();
					} else {
						view.stopLoading();
						view.loadUrl("about:blank");
					}
				}

			}
			super.onPageStarted(view, url, favicon);
		}
	}

}
