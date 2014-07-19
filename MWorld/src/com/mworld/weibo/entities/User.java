package com.mworld.weibo.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

public class User implements Parcelable {

	private static final String TAG = "User";

	/** 用户UID */
	private String id;
	/** 字符串型的用户UID */
	private String idstr;
	/** 用户昵称 */
	private String screenName;
	/** 友好显示名称 */
	private String name;
	/** 用户所在省级ID */
	private int province;
	/** 用户所在城市ID */
	private int city;
	/** 用户所在地 */
	private String location;
	/** 用户个人描述 */
	private String description;
	/** 用户博客地址 */
	private String url;
	/** 用户头像地址（中图），50×50像素 */
	private String profileImageUrl;
	/** 用户的微博统一URL地址 */
	private String profileUrl;
	/** 用户的个性化域名 */
	private String domain;
	/** 用户的微号 */
	private String weihao;
	/** 性别，m：男、f：女、n：未知 */
	private String gender;
	/** 粉丝数 */
	private int followersCount;
	/** 关注数 */
	private int friendsCount;
	/** 微博数 */
	private int statusesCount;
	/** 收藏数 */
	private int favouritesCount;
	/** 用户创建（注册）时间 */
	private String createdAt;
	/** 暂未支持 */
	private boolean following;
	/** 是否允许所有人给我发私信，true：是，false：否 */
	private boolean allowAllActMsg;
	/** 是否允许标识用户的地理位置，true：是，false：否 */
	private boolean geoEnabled;
	/** 是否是微博认证用户，即加V用户，true：是，false：否 */
	private boolean verified;
	/**
	 * * -1普通用户; 0名人, 1政府, 2企业, 3媒体, 4校园, 5网站, 6应用, 7团体（机构）, 8待审企业, 200初级达人, *
	 * 220中高级达人, 400已故V用户。 -1为普通用户，200和220为达人用户，0为黄V用户，其它即为蓝V用户
	 */
	private int verifiedType;
	/** 用户备注信息，只有在查询用户关系时才返回此字段 */
	private String remark;
	/** 用户的最近一条微博信息字段 详细 */
	// private Status status;
	/** 是否允许所有人对我的微博进行评论，true：是，false：否 */
	private boolean allowAllComment;
	/** 用户头像地址（大图），180×180像素 */
	private String avatarLarge;
	/** 用户头像地址（高清），高清头像原图 */
	private String avatarHd;
	/** 认证原因 */
	private String verifiedReason;
	/** 该用户是否关注当前登录用户，true：是，false：否 */
	private boolean followMe;
	/** 用户的在线状态，0：不在线、1：在线 */
	private int onlineStatus;
	/** 用户的互粉数 */
	private int biFollowersCount;
	/** 用户当前的语言版本，zh-cn：简体中文，zh-tw：繁体中文，en：英语 */
	private String lang;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIdstr() {
		return idstr;
	}

	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}

	public String getScreenName() {
		return screenName;
	}

	public void setScreenName(String screenName) {
		this.screenName = screenName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getProvince() {
		return province;
	}

	public void setProvince(int province) {
		this.province = province;
	}

	public int getCity() {
		return city;
	}

	public void setCity(int city) {
		this.city = city;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProfileImageUrl() {
		return profileImageUrl;
	}

	public void setProfileImageUrl(String profileImageUrl) {
		this.profileImageUrl = profileImageUrl;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getWeihao() {
		return weihao;
	}

	public void setWeihao(String weihao) {
		this.weihao = weihao;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public int getFollowersCount() {
		return followersCount;
	}

	public void setFollowersCount(int followersCount) {
		this.followersCount = followersCount;
	}

	public int getFriendsCount() {
		return friendsCount;
	}

	public void setFriendsCount(int friendsCount) {
		this.friendsCount = friendsCount;
	}

	public int getStatusesCount() {
		return statusesCount;
	}

	public void setStatusesCount(int statusesCount) {
		this.statusesCount = statusesCount;
	}

	public int getFavouritesCount() {
		return favouritesCount;
	}

	public void setFavouritesCount(int favouritesCount) {
		this.favouritesCount = favouritesCount;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public boolean isFollowing() {
		return following;
	}

	public boolean getFollowing() {
		return following;
	}

	public void setFollowing(boolean following) {
		this.following = following;
	}

	public boolean isAllowAllActMsg() {
		return allowAllActMsg;
	}

	public boolean getAllowAllActMsg() {
		return allowAllActMsg;
	}

	public void setAllowAllActMsg(boolean allowAllActMsg) {
		this.allowAllActMsg = allowAllActMsg;
	}

	public boolean isGeoEnabled() {
		return geoEnabled;
	}

	public boolean getGeoEnabled() {
		return geoEnabled;
	}

	public void setGeoEnabled(boolean geoEnabled) {
		this.geoEnabled = geoEnabled;
	}

	public boolean isVerified() {
		return verified;
	}

	public boolean getVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}

	public int getVerifiedType() {
		return verifiedType;
	}

	public void setVerifiedType(int verifiedType) {
		this.verifiedType = verifiedType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public boolean isAllowAllComment() {
		return allowAllComment;
	}

	public boolean getAllowAllComment() {
		return allowAllComment;
	}

	public void setAllowAllComment(boolean allowAllComment) {
		this.allowAllComment = allowAllComment;
	}

	public String getAvatarLarge() {
		return avatarLarge;
	}

	public void setAvatarLarge(String avatarLarge) {
		this.avatarLarge = avatarLarge;
	}

	public String getAvatarHd() {
		return avatarHd;
	}

	public void setAvatarHd(String avatarHd) {
		this.avatarHd = avatarHd;
	}

	public String getVerifiedReason() {
		return verifiedReason;
	}

	public void setVerifiedReason(String verifiedReason) {
		this.verifiedReason = verifiedReason;
	}

	public boolean isFollowMe() {
		return followMe;
	}

	public boolean getFollowMe() {
		return followMe;
	}

	public void setFollowMe(boolean followMe) {
		this.followMe = followMe;
	}

	public int getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(int onlineStatus) {
		this.onlineStatus = onlineStatus;
	}

	public int getBiFollowersCount() {
		return biFollowersCount;
	}

	public void setBiFollowersCount(int biFollowersCount) {
		this.biFollowersCount = biFollowersCount;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public User() {

	}

	public static User parse(String jsonString) {
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}
		try {
			return User.parse(JSON.parseObject(jsonString));
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}
		return null;
	}

	public static User parse(JSONObject jsonObject) {
		if (null == jsonObject) {
			return null;
		}
		User user = new User();
		user.id = jsonObject.getString("id", "");
		user.idstr = jsonObject.getString("idstr", "");
		user.screenName = jsonObject.getString("screen_name", "");
		user.name = jsonObject.getString("name", "");
		user.province = jsonObject.getIntValue("province", -1);
		user.city = jsonObject.getIntValue("city", -1);
		user.location = jsonObject.getString("location", "");
		user.description = jsonObject.getString("description", "");
		user.url = jsonObject.getString("url", "");
		user.profileImageUrl = jsonObject.getString("profile_image_url", "");
		user.profileUrl = jsonObject.getString("profile_url", "");
		user.domain = jsonObject.getString("domain", "");
		user.weihao = jsonObject.getString("weihao", "");
		user.gender = jsonObject.getString("gender", "");
		user.followersCount = jsonObject.getIntValue("followers_count", 0);
		user.friendsCount = jsonObject.getIntValue("friends_count", 0);
		user.statusesCount = jsonObject.getIntValue("statuses_count", 0);
		user.favouritesCount = jsonObject.getIntValue("favourites_count", 0);
		user.createdAt = jsonObject.getString("created_at", "");
		user.following = jsonObject.getBooleanValue("following", false);
		user.allowAllActMsg = jsonObject.getBooleanValue("allow_all_act_msg",
				false);
		user.geoEnabled = jsonObject.getBooleanValue("geo_enabled", false);
		user.verified = jsonObject.getBooleanValue("verified", false);
		user.verifiedType = jsonObject.getIntValue("verified_type", -1);
		user.remark = jsonObject.getString("remark", "");
		// user.status =Status.parse(jsonObject.getJSONObject("status"));

		user.allowAllComment = jsonObject.getBooleanValue("allow_all_comment",
				true);
		user.avatarLarge = jsonObject.getString("avatar_large", "");
		user.avatarHd = jsonObject.getString("avatar_hd", "");
		user.verifiedReason = jsonObject.getString("verified_reason", "");
		user.followMe = jsonObject.getBooleanValue("follow_me", false);
		user.onlineStatus = jsonObject.getIntValue("online_status", 0);
		user.biFollowersCount = jsonObject.getIntValue("bi_followers_count", 0);
		user.lang = jsonObject.getString("lang", "");
		return user;
	}

	private User(Parcel in) {
		id = in.readString();
		idstr = in.readString();
		screenName = in.readString();
		name = in.readString();
		province = in.readInt();
		city = in.readInt();
		location = in.readString();
		description = in.readString();
		url = in.readString();
		profileImageUrl = in.readString();
		profileUrl = in.readString();
		domain = in.readString();
		weihao = in.readString();
		gender = in.readString();
		followersCount = in.readInt();
		friendsCount = in.readInt();
		statusesCount = in.readInt();
		favouritesCount = in.readInt();
		createdAt = in.readString();
		following = in.readInt() == 1 ? true : false;
		allowAllActMsg = in.readInt() == 1 ? true : false;
		geoEnabled = in.readInt() == 1 ? true : false;
		verified = in.readInt() == 1 ? true : false;
		verifiedType = in.readInt();
		remark = in.readString();
		// status = Status.CREATOR.createFromParcel(in);
		allowAllComment = in.readInt() == 1 ? true : false;
		avatarLarge = in.readString();
		avatarHd = in.readString();
		verifiedReason = in.readString();
		followMe = in.readInt() == 1 ? true : false;
		onlineStatus = in.readInt();
		biFollowersCount = in.readInt();
		lang = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
		public User createFromParcel(Parcel in) {
			return new User(in);
		}

		public User[] newArray(int size) {
			return new User[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(idstr);
		dest.writeString(screenName);
		dest.writeString(name);
		dest.writeInt(province);
		dest.writeInt(city);
		dest.writeString(location);
		dest.writeString(description);
		dest.writeString(url);
		dest.writeString(profileImageUrl);
		dest.writeString(profileUrl);
		dest.writeString(domain);
		dest.writeString(weihao);
		dest.writeString(gender);
		dest.writeInt(followersCount);
		dest.writeInt(friendsCount);
		dest.writeInt(statusesCount);
		dest.writeInt(favouritesCount);
		dest.writeString(createdAt);
		dest.writeInt(following ? 1 : 0);
		dest.writeInt(allowAllActMsg ? 1 : 0);
		dest.writeInt(geoEnabled ? 1 : 0);
		dest.writeInt(verified ? 1 : 0);
		dest.writeInt(verifiedType);
		dest.writeString(remark);
		// dest.writeParcelable(status, flags);
		dest.writeInt(allowAllComment ? 1 : 0);
		dest.writeString(avatarLarge);
		dest.writeString(avatarHd);
		dest.writeString(verifiedReason);
		dest.writeInt(followMe ? 1 : 0);
		dest.writeInt(onlineStatus);
		dest.writeInt(biFollowersCount);
		dest.writeString(lang);
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof User && id.equals(((User) obj).getId());
	}

	@Override
	public int hashCode() {
		return getId().hashCode();
	}

}