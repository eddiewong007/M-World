package com.mworld.weibo.entities;

import com.alibaba.fastjson.JSONObject;

/**
 * 我喜欢的微博标签（Tag）结构体。
 * 
 */
public class Tag {

	/** type 取值，0：普通微博，1：私密微博，3：指定分组微博，4：密友微博 */
	public int id;
	/** 分组的组号 */
	public String tag;

	public static Tag parse(JSONObject jsonObject) {
		if (null == jsonObject) {
			return null;
		}

		Tag tag = new Tag();
		tag.id = jsonObject.getIntValue("id", 0);
		tag.tag = jsonObject.getString("tag", "");

		return tag;
	}
}