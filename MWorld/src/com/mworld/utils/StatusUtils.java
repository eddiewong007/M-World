package com.mworld.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

public class StatusUtils {

	public static final Pattern NAME_PATTERN = Pattern.compile(
			"@([\\u4e00-\\u9fa5\\w\\-\\—]{2,30})", Pattern.CASE_INSENSITIVE);
	public static final Pattern TOPIC_PATTERN = Pattern
			.compile("#([^\\#|^\\@|.]+)#");
	public static final Pattern EMOTION_URL = Pattern.compile("\\[(\\S+?)\\]");

	public static SpannableStringBuilder matchNameTopic(String text) {
		SpannableStringBuilder style = new SpannableStringBuilder(text);
		Matcher matcher = NAME_PATTERN.matcher(text);
		while (matcher.find()) {
			style.setSpan(new ForegroundColorSpan(Color.rgb(0x00, 0xbf, 0xff)),
					matcher.start(), matcher.end(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		matcher = TOPIC_PATTERN.matcher(text);
		while (matcher.find()) {
			style.setSpan(new ForegroundColorSpan(Color.rgb(0x00, 0xbf, 0xff)),
					matcher.start(), matcher.end(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return style;
	}

	public static SpannableStringBuilder addEmotions(Context context,
			SpannableStringBuilder style) {
		Matcher localMatcher = EMOTION_URL.matcher(style);
		while (localMatcher.find()) {
			String key = localMatcher.group(0);
			int start = localMatcher.start();
			int end = localMatcher.end();
			if (end - start < 8) {
				Bitmap bitmap = EmotionUtils.getEmotion(context, key);
				if (bitmap != null) {
					ImageSpan localImageSpan = new ImageSpan(context, bitmap,
							ImageSpan.ALIGN_BASELINE);
					style.setSpan(localImageSpan, start, end,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}

			}
		}
		return style;
	}

	// public static final Pattern WEB_URL = Pattern
	// .compile("http://[a-zA-Z0-9+&@#/%?=~_\\-|!:,\\.;]*[a-zA-Z0-9+&@#/%=~_|]");
	//
	// public static final Pattern TOPIC_URL = Pattern
	// .compile("#[\\p{Print}\\p{InCJKUnifiedIdeographs}&&[^#]]+#");
	//
	// public static final Pattern MENTION_URL = Pattern
	// .compile("@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26}");
	//
	// public static final Pattern EMOTION_URL =
	// Pattern.compile("\\[(\\S+?)\\]");
	//
	// public static final String WEB_SCHEME = "http://";
	//
	// public static final String TOPIC_SCHEME = "com.mworld.topic://";
	//
	// public static final String MENTION_SCHEME = "com.mworld.mention://";
	// public static SpannableString convertToSpannableString(String txt) {
	// String hackTxt;
	// if (txt.startsWith("[") && txt.endsWith("]")) {
	// hackTxt = txt + " ";
	// } else {
	// hackTxt = txt;
	// }
	// SpannableString value = SpannableString.valueOf(hackTxt);
	// Linkify.addLinks(value, MENTION_URL, MENTION_SCHEME);
	// Linkify.addLinks(value, WEB_URL, WEB_SCHEME);
	// Linkify.addLinks(value, TOPIC_URL, TOPIC_SCHEME);
	//
	// URLSpan[] urlSpans = value.getSpans(0, value.length(), URLSpan.class);
	// MyURLSpan weiboSpan = null;
	// for (URLSpan urlSpan : urlSpans) {
	// weiboSpan = new MyURLSpan(urlSpan.getURL());
	// int start = value.getSpanStart(urlSpan);
	// int end = value.getSpanEnd(urlSpan);
	// value.removeSpan(urlSpan);
	// value.setSpan(weiboSpan, start, end,
	// Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
	// }
	//
	// addEmotions(value);
	// return value;
	// }
	//
	// public static class MyURLSpan extends ClickableSpan {
	//
	// private final String mURL;
	//
	// public MyURLSpan(String url) {
	// mURL = url;
	// }
	//
	// public int getSpanTypeId() {
	// return 11;
	// }
	//
	// public int describeContents() {
	// return 0;
	// }
	//
	// public void writeToParcel(Parcel dest, int flags) {
	// dest.writeString(mURL);
	// }
	//
	// public String getURL() {
	// return mURL;
	// }
	//
	// public void onClick(View widget) {
	// Uri uri = Uri.parse(getURL());
	// Context context = widget.getContext();
	// if (uri.getScheme().startsWith("http")) {
	//
	// } else {
	// Log.i("Emotions", getURL());
	// Toast.makeText(context, getURL(), Toast.LENGTH_LONG).show();
	// }
	// }
	//
	// public void onLongClick(View widget) {
	//
	// }
	//
	// @Override
	// public void updateDrawState(TextPaint tp) {
	// tp.setUnderlineText(false);
	// }
	// }
}
