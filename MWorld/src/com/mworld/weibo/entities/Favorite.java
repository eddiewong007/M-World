package com.mworld.weibo.entities;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 我喜欢的微博信息结构体。
 * 
 */
public class Favorite {

	/** 我喜欢的微博信息 */
	public Status status;
	/** 我喜欢的微博的 Tag 信息 */
	public ArrayList<Tag> tags;
	/** 创建我喜欢的微博信息的时间 */
	public String favorited_time;

	public static Favorite parse(String jsonString) {
		try {
			JSONObject object = JSON.parseObject(jsonString);
			return Favorite.parse(object);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Favorite parse(JSONObject jsonObject) {
		if (null == jsonObject) {
			return null;
		}

		Favorite favorite = new Favorite();
		favorite.status = Status.parse(jsonObject.getJSONObject("status"));
		favorite.favorited_time = jsonObject.getString("favorited_time");

		JSONArray jsonArray = jsonObject.getJSONArray("tags");
		if (jsonArray != null && jsonArray.size() > 0) {
			int length = jsonArray.size();
			favorite.tags = new ArrayList<Tag>(length);
			for (int ix = 0; ix < length; ix++) {
				favorite.tags.add(Tag.parse(jsonArray.getJSONObject(ix)));
			}
		}

		return favorite;
	}
}
