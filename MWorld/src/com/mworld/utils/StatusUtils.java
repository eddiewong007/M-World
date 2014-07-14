package com.mworld.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class StatusUtils {

	public static final Pattern WEB_URL = Pattern
			.compile("http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]");

	public static final Pattern TOPIC_URL = Pattern
			.compile("#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#");

	public static final Pattern MENTION_URL = Pattern
			.compile("@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}");

	public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");

	public static final String WEB_SCHEME = "http://";

	public static final String TOPIC_SCHEME = "com.mworld.topic://";

	public static final String MENTION_SCHEME = "com.mworld.mention://";

	public static SpannableString convertToSpannableString(String txt) {
		// hack to fix android imagespan bug,see
		// http://stackoverflow.com/questions/3253148/imagespan-is-cut-off-incorrectly-aligned
		// if string only contains emotion tags,add a empty char to the end
		String hackTxt;
		if (txt.startsWith("[") && txt.endsWith("]")) {
			hackTxt = txt + " ";
		} else {
			hackTxt = txt;
		}
		SpannableString value = SpannableString.valueOf(hackTxt);
		Linkify.addLinks(value, MENTION_URL, MENTION_SCHEME);
		Linkify.addLinks(value, WEB_URL, WEB_SCHEME);
		Linkify.addLinks(value, TOPIC_URL, TOPIC_SCHEME);

		URLSpan[] urlSpans = value.getSpans(0, value.length(), URLSpan.class);
		MyURLSpan weiboSpan = null;
		for (URLSpan urlSpan : urlSpans) {
			weiboSpan = new MyURLSpan(urlSpan.getURL());
			int start = value.getSpanStart(urlSpan);
			int end = value.getSpanEnd(urlSpan);
			value.removeSpan(urlSpan);
			value.setSpan(weiboSpan, start, end,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		addEmotions(value);
		return value;
	}

	private static void addEmotions(SpannableString value) {
		Matcher localMatcher = EMOTION_URL.matcher(value);
		while (localMatcher.find()) {
			String str2 = localMatcher.group(0);
			Log.i("Emotions", str2);
			int k = localMatcher.start();
			int m = localMatcher.end();
			if (m - k < 8) {
				// Bitmap bitmap =
				// GlobalContext.getInstance().getEmotionsPics().get(str2);
				// if (bitmap == null) {
				// bitmap =
				// GlobalContext.getInstance().getHuahuaPics().get(str2);
				// }
				// if (bitmap != null) {
				// ImageSpan localImageSpan = new ImageSpan(
				// GlobalContext.getInstance().getActivity(), bitmap,
				// ImageSpan.ALIGN_BASELINE);
				// value.setSpan(localImageSpan, k, m,
				// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				// }

			}
		}
	}

	public static class MyURLSpan extends ClickableSpan {

		private final String mURL;

		public MyURLSpan(String url) {
			mURL = url;
		}

		public int getSpanTypeId() {
			return 11;
		}

		public int describeContents() {
			return 0;
		}

		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(mURL);
		}

		public String getURL() {
			return mURL;
		}

		public void onClick(View widget) {
			Uri uri = Uri.parse(getURL());
			Context context = widget.getContext();
			if (uri.getScheme().startsWith("http")) {

			} else {
				Log.i("Emotions", getURL());
				Toast.makeText(context, getURL(), Toast.LENGTH_LONG).show();
			}
		}

		public void onLongClick(View widget) {

		}

		@Override
		public void updateDrawState(TextPaint tp) {
			tp.setUnderlineText(false);
		}
	}
}
