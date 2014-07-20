package com.mworld.support.utils;

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

public class StatusBuilder {

	private static final Pattern NAME_PATTERN = Pattern.compile(
			"@([\\u4e00-\\u9fa5\\w\\-\\—]{2,30})", Pattern.CASE_INSENSITIVE);
	private static final Pattern TOPIC_PATTERN = Pattern
			.compile("#([^\\#|^\\@|.]+)#");
	private static final Pattern EMOTION_PATTERN = Pattern
			.compile("\\[(\\S+?)\\]");

	private Context mContext;
	private SpannableStringBuilder mStyle;

	/**
	 * 构造函数
	 * 
	 * @param context
	 * @param text
	 */
	public StatusBuilder(Context context, String text) {
		mContext = context;
		mStyle = new SpannableStringBuilder(text);
	}

	/**
	 * 匹配昵称
	 * 
	 * @return
	 */
	public StatusBuilder matchName() {
		Matcher matcher = NAME_PATTERN.matcher(mStyle);
		while (matcher.find()) {
			mStyle.setSpan(
					new ForegroundColorSpan(Color.rgb(0x00, 0xbf, 0xff)),
					matcher.start(), matcher.end(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return this;
	}

	/**
	 * 匹配话题
	 * 
	 * @return
	 */
	public StatusBuilder matchTopic() {
		Matcher matcher = TOPIC_PATTERN.matcher(mStyle);
		while (matcher.find()) {
			mStyle.setSpan(
					new ForegroundColorSpan(Color.rgb(0x00, 0xbf, 0xff)),
					matcher.start(), matcher.end(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return this;
	}

	/**
	 * 匹配表情
	 * 
	 * @return
	 */
	public StatusBuilder matchEmotions() {
		Matcher matcher = EMOTION_PATTERN.matcher(mStyle);
		while (matcher.find()) {
			String key = matcher.group(0);
			int start = matcher.start();
			int end = matcher.end();
			if (end - start < 8) {
				Bitmap bitmap = EmotionUtils.getEmotion(mContext, key);
				if (bitmap != null) {
					ImageSpan imageSpan = new ImageSpan(mContext, bitmap,
							ImageSpan.ALIGN_BASELINE);
					mStyle.setSpan(imageSpan, start, end,
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
		}
		return this;
	}

	/**
	 * 返回构造好的SpannableStringBuilder对象
	 * 
	 * @return
	 */
	public SpannableStringBuilder build() {
		return mStyle;
	}
}
