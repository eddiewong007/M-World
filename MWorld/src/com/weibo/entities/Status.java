package com.weibo.entities;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 微博（status）对象数据结构
 * 
 * @author MengMeng
 * 
 */
public class Status implements Parcelable {

	/** 微博创建时间 */
	public String created_at;
	/** 微博ID */
	public long id;
	/** 微博MID */
	public long mid;
	/** 字符串型的微博ID */
	public String idstr;
	/** 微博信息内容 */
	public String text;
	/** 微博来源 */
	public String source;
	/** 是否已收藏，true：是，false：否 */
	public boolean favorited;
	/** 是否被截断，true：是，false：否 */
	public boolean truncated;
	/** （暂未支持）回复ID */
	public String in_reply_to_status_id;
	/** （暂未支持）回复人UID */
	public String in_reply_to_user_id;
	/** （暂未支持）回复人昵称 */
	public String in_reply_to_screen_name;
	/** 缩略图片地址，没有时不返回此字段 */
	public String thumbnail_pic;
	/** 中等尺寸图片地址，没有时不返回此字段 */
	public String bmiddle_pic;
	/** 原始图片地址，没有时不返回此字段 */
	public String original_pic;
	/** 地理信息字段 */
	public Geo geo;
	/** 微博作者的用户信息字段 */
	public User user;
	/** 被转发的原微博信息字段，当该微博为转发微博时返回 */
	public Status retweeted_status;
	/** 转发数 */
	public int reposts_count;
	/** 评论数 */
	public int comments_count;
	/** 表态数 */
	public int attitudes_count;
	/** 暂未支持 */
	public int mlevel;
	/** 微博的可见性及指定可见分组信息。 */
	public Visible visible;
	/** 微博配图地址。多图时返回多图链接。无配图返回“[]” */
	public ArrayList<String> pic_urls;

	/** 微博流内的推广微博ID */
	public String ad;

	public Status() {

	}

	public static Status parse(String jsonString) {
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}

		try {
			return Status.parse(JSON.parseObject(jsonString));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static Status parse(JSONObject jsonObject) {
		if (null == jsonObject) {
			return null;
		}

		Status status = new Status();
		status.created_at = jsonObject.getString("created_at");
		status.id = jsonObject.getLongValue("id");
		status.mid = jsonObject.getLongValue("mid");
		status.idstr = jsonObject.getString("idstr");
		status.text = jsonObject.getString("text");
		status.source = jsonObject.getString("source");
		status.favorited = jsonObject.getBooleanValue("favorited", false);
		status.truncated = jsonObject.getBooleanValue("truncated", false);

		// Have NOT supported
		status.in_reply_to_status_id = jsonObject
				.getString("in_reply_to_status_id");
		status.in_reply_to_user_id = jsonObject
				.getString("in_reply_to_user_id");
		status.in_reply_to_screen_name = jsonObject
				.getString("in_reply_to_screen_name");

		status.thumbnail_pic = jsonObject.getString("thumbnail_pic");
		status.bmiddle_pic = jsonObject.getString("bmiddle_pic");
		status.original_pic = jsonObject.getString("original_pic");
		status.geo = Geo.parse(jsonObject.getJSONObject("geo"));
		status.user = User.parse(jsonObject.getJSONObject("user"));
		status.retweeted_status = Status.parse(jsonObject
				.getJSONObject("retweeted_status"));
		status.reposts_count = jsonObject.getIntValue("reposts_count");
		status.comments_count = jsonObject.getIntValue("comments_count");
		status.attitudes_count = jsonObject.getIntValue("attitudes_count");
		status.mlevel = jsonObject.getIntValue("mlevel"); // Have NOT supported
		status.visible = Visible.parse(jsonObject.getJSONObject("visible"));

		JSONArray picUrlsArray = jsonObject.getJSONArray("pic_urls");
		if (picUrlsArray != null && !picUrlsArray.isEmpty()) {
			int size = picUrlsArray.size();
			status.pic_urls = new ArrayList<String>(size);
			JSONObject tmpObject = null;
			for (int ix = 0; ix < size; ix++) {
				tmpObject = picUrlsArray.getJSONObject(ix);
				if (tmpObject != null) {
					status.pic_urls.add(tmpObject.getString("thumbnail_pic"));
				}
			}
		}

		status.ad = jsonObject.getString("ad", "");

		return status;
	}

	public Status(Parcel in) {
		created_at = in.readString();
		id = in.readLong();
		mid = in.readLong();
		idstr = in.readString();
		text = in.readString();
		source = in.readString();
		favorited = in.readInt() == 1 ? true : false;
		truncated = in.readInt() == 1 ? true : false;

		// Have NOT supported
		in_reply_to_status_id = in.readString();
		in_reply_to_user_id = in.readString();
		in_reply_to_screen_name = in.readString();

		thumbnail_pic = in.readString();
		bmiddle_pic = in.readString();
		original_pic = in.readString();
		geo = Geo.CREATOR.createFromParcel(in);
		user = User.CREATOR.createFromParcel(in);
		retweeted_status = Status.CREATOR.createFromParcel(in);
		reposts_count = in.readInt();
		comments_count = in.readInt();
		attitudes_count = in.readInt();
		mlevel = in.readInt();
		visible = Visible.CREATOR.createFromParcel(in);
		in.readStringList(pic_urls);
		ad = in.readString();

	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Status> CREATOR = new Parcelable.Creator<Status>() {
		public Status createFromParcel(Parcel in) {
			return new Status(in);
		}

		public Status[] newArray(int size) {
			return new Status[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(created_at);
		dest.writeLong(id);
		dest.writeLong(mid);
		dest.writeString(idstr);
		dest.writeString(text);
		dest.writeString(source);
		dest.writeInt(favorited ? 1 : 0);
		dest.writeInt(truncated ? 1 : 0);
		dest.writeString(in_reply_to_status_id);
		dest.writeString(in_reply_to_user_id);
		dest.writeString(in_reply_to_screen_name);
		dest.writeString(thumbnail_pic);
		dest.writeString(bmiddle_pic);
		dest.writeString(original_pic);
		dest.writeParcelable(geo, flags);
		dest.writeParcelable(user, flags);
		dest.writeParcelable(retweeted_status, flags);
		dest.writeInt(reposts_count);
		dest.writeInt(comments_count);
		dest.writeInt(attitudes_count);
		dest.writeInt(mlevel);
		dest.writeParcelable(visible, flags);
		dest.writeStringList(pic_urls);
		dest.writeString(ad);
	}
}
