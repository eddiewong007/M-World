package com.mworld.weibo.entities;

import java.util.ArrayList;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import android.text.TextUtils;

/**
 * 评论列表结构体
 * 
 * @author MengMeng
 * 
 */
public class CommentsList {
	/** 评论列表 */
	public ArrayList<Comment> commentList;
	public String previous_cursor;
	public String next_cursor;
	public int total_number;

	public static CommentsList parse(String jsonString) {
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		CommentsList comments = new CommentsList();
		try {
			JSONObject jsonObject = JSON.parseObject(jsonString);
			comments.previous_cursor = jsonObject.getString("previous_cursor",
					"0");
			comments.next_cursor = jsonObject.getString("next_cursor", "0");
			comments.total_number = jsonObject.getIntValue("total_number", 0);

			JSONArray jsonArray = jsonObject.getJSONArray("comments");
			comments.commentList = new ArrayList<Comment>();
			if (jsonArray != null && !jsonArray.isEmpty()) {
				int size = jsonArray.size();
				for (int ix = 0; ix < size; ix++) {
					comments.commentList.add(Comment.parse(jsonArray
							.getJSONObject(ix)));
				}
			} else {

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return comments;
	}
}
