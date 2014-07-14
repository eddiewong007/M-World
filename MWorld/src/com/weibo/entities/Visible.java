package com.weibo.entities;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 微博的可见性及指定可见分组信息对象数据结构
 * 
 * @author MengMeng
 * 
 */
public class Visible {

	public static final int VISIBLE_NORMAL = 0;
	public static final int VISIBLE_PRIVACY = 1;
	public static final int VISIBLE_GROUPED = 2;
	public static final int VISIBLE_FRIEND = 3;

	/** type 取值，0：普通微博，1：私密微博，3：指定分组微博，4：密友微博 */
	public int type;
	/** 分组的组号 */
	public int list_id;

	public static Visible parse(String jsonString) {
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			return Visible.parse(JSON.parseObject(jsonString));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Visible parse(JSONObject jsonObject) {
		if (null == jsonObject) {
			return null;
		}
		Visible visible = new Visible();

		visible.type = jsonObject.getIntValue("type");
		visible.list_id = jsonObject.getIntValue("list_id");
		return visible;
	}
}
