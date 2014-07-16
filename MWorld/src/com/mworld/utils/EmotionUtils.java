package com.mworld.utils;

import java.io.IOException;
import java.util.HashMap;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class EmotionUtils {
	private static final String TAG = "EmotionUtils";

	private static final String[] text = { "[爱你]", "[抱抱]", "[悲伤]", "[鄙视]",
			"[闭嘴]", "[馋嘴]", "[吃惊]", "[打哈欠]", "[鼓掌]", "[哈哈]", "[害羞]", "[汗]",
			"[呵呵]", "[黑线]", "[哼]", "[可爱]", "[可怜]", "[挖鼻屎]", "[泪]", "[酷]",
			"[懒得理你]", "[钱]", "[亲亲]", "[花心]", "[失望]", "[书呆子]", "[衰]", "[睡觉]",
			"[偷笑]", "[吐]", "[委屈]", "[嘻嘻]", "[嘘]", "[疑问]", "[阴险]", "[右哼哼]",
			"[左哼哼]", "[晕]", "[抓狂]", "[怒]", "[拜拜]", "[思考]", "[怒骂]", "[囧]",
			"[困]", "[愤怒]", "[感冒]", "[生病]", "[挤眼]", "[奥特曼]", "[good]", "[弱]",
			"[ok]", "[耶]", "[来]", "[不要]", "[赞]", "[熊猫]", "[兔子]", "[猪头]", "[心]",
			"[伤心]", "[蜡烛]", "[威武]", "[蛋糕]", "[礼物]", "[围观]", "[钟]", "[太阳]",
			"[月亮]", "[右边亮了]", "[得意地笑]", "[求关注]", "[偷乐]", "[笑哈哈]", "[转发]",
			"[浮云]" };

	private static final HashMap<String, String> MAP = new HashMap<String, String>();
	static {
		int picIndex = 1;
		for (String t : text) {
			MAP.put(t, "e" + (picIndex++) + ".png");
		}
	}

	/**
	 * 通过表情名称获取表情的Bitmap图像
	 * 
	 * @param context
	 *            上下文对象
	 * @param key
	 *            表情的名称
	 * @return 表情的Bitmap图像
	 */
	public static Bitmap getEmotion(Context context, String key) {
		String fileName = "default_emotions_package/" + MAP.get(key);
		try {
			AssetManager am = context.getAssets();
			return BitmapFactory.decodeStream(am.open(fileName));
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
		}
		return null;
	}

}
