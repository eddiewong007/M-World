package com.mworld.support.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import com.mworld.weibo.entities.Account;

public class Utility {

	private static final String TAG = Utility.class.getName();

	public static boolean isCertificateFingerprintCorrect(Context context) {
		try {
			PackageManager pm = context.getPackageManager();
			String packageName = context.getPackageName();
			int flags = PackageManager.GET_SIGNATURES;

			PackageInfo packageInfo = pm.getPackageInfo(packageName, flags);

			Signature[] signatures = packageInfo.signatures;

			byte[] cert = signatures[0].toByteArray();

			String strResult = "";

			MessageDigest md;

			md = MessageDigest.getInstance("MD5");
			md.update(cert);
			for (byte b : md.digest()) {
				strResult += Integer.toString(b & 0xff, 16);
			}
			strResult = strResult.toUpperCase(Locale.ENGLISH);
			// debug
			if ("59D4455C365EE4EF9B6FD52F9D397C69".toUpperCase(Locale.ENGLISH)
					.equals(strResult)) {
				return true;
			}
			if ("5E13C8E9C532AD9919681D3FF5E6723".toUpperCase(Locale.ENGLISH)
					.equals(strResult)) {
				return true;
			}
			// relaease
			if ("F641BB3EF2B1E56CA3788482D9E67A7".toUpperCase(Locale.ENGLISH)
					.equals(strResult)) {
				return true;
			}
			Log.e(TAG, strResult);
		} catch (NoSuchAlgorithmException ex) {
			ex.printStackTrace();
		} catch (PackageManager.NameNotFoundException ex) {
			ex.printStackTrace();
		}

		return false;
	}

	public static boolean isSinaWeiboSafe(Activity activity) {
		Intent mapCall = new Intent("com.sina.weibo.remotessoservice");
		PackageManager packageManager = activity.getPackageManager();
		List<ResolveInfo> services = packageManager.queryIntentServices(
				mapCall, 0);
		return services.size() > 0;
	}

	public static Bundle parseUrl(String url) {
		url = url.replace("weiboconnect", "http");
		try {
			URL u = new URL(url);
			Bundle b = decodeUrl(u.getQuery());
			b.putAll(decodeUrl(u.getRef()));
			return b;
		} catch (MalformedURLException e) {
			return new Bundle();
		}
	}

	public static Bundle decodeUrl(String s) {
		Bundle params = new Bundle();
		if (s != null) {
			String array[] = s.split("&");
			for (String parameter : array) {
				String v[] = parameter.split("=");
				try {
					params.putString(URLDecoder.decode(v[0], "UTF-8"),
							URLDecoder.decode(v[1], "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();

				}
			}
		}
		return params;
	}

	public static boolean isTokenValid(Account account) {
		return !TextUtils.isEmpty(account.getAccessToken())
				&& (account.getExpiresIn() == 0 || (System.currentTimeMillis()) < account
						.getExpiresIn());
	}

	public static int getScreenWidth() {
		Activity activity = GlobalContext.getInstance().getActivity();
		if (activity != null) {
			Display display = activity.getWindowManager().getDefaultDisplay();
			DisplayMetrics metrics = new DisplayMetrics();
			display.getMetrics(metrics);
			return metrics.widthPixels;
		}

		return 480;
	}

}
