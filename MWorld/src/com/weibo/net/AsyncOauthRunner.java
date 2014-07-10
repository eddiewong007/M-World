package com.weibo.net;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.AsyncWeiboRunner;
import com.sina.weibo.sdk.net.RequestListener;

public class AsyncOauthRunner extends AsyncWeiboRunner {

	public static void requestAsync(final String url,
			final List<NameValuePair> postParameters,
			final RequestListener listener) {
		new Thread() {
			@Override
			public void run() {
				try {
					String response = openUrl(url, postParameters, listener);
					listener.onComplete(response);
				} catch (WeiboException e) {
					listener.onWeiboException(e);
				}
			}
		}.start();
	}

	public static String openUrl(String url,
			List<NameValuePair> postParameters, RequestListener listener) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(url);

			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
					postParameters);
			request.setEntity(formEntity);
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == 200) {
				String jsonString = EntityUtils.toString(response.getEntity());
				return jsonString;
			}
		} catch (ClientProtocolException e) {
			listener.onWeiboException(new WeiboException(e));
		} catch (ParseException e) {
			listener.onWeiboException(new WeiboException(e));
		} catch (IOException e) {
			listener.onWeiboException(new WeiboException(e));
		}
		return null;
	}
}
