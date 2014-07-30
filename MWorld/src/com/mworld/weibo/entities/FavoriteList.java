package com.mworld.weibo.entities;

import java.util.ArrayList;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 我喜欢的微博信息列表结构体。
 * 
 */
public class FavoriteList {

	/** 微博列表 */
	public ArrayList<Favorite> favoriteList;
	public ArrayList<Status> statuses;
	public int total_number;

	public static FavoriteList parse(String jsonString) {
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		FavoriteList favorites = new FavoriteList();
		try {
			JSONObject jsonObject = JSON.parseObject(jsonString);
			favorites.total_number = jsonObject.getIntValue("total_number", 0);

			JSONArray jsonArray = jsonObject.getJSONArray("favorites");
			if (jsonArray != null && jsonArray.size() > 0) {
				int length = jsonArray.size();
				favorites.favoriteList = new ArrayList<Favorite>(length);
				favorites.statuses = new ArrayList<Status>(length);
				for (int ix = 0; ix < length; ix++) {
					favorites.favoriteList.add(Favorite.parse(jsonArray
							.getJSONObject(ix)));
					favorites.statuses
							.add(favorites.favoriteList.get(ix).status);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return favorites;
	}
}
