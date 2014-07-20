package com.mworld.weibo.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

/**
 * 地理信息（geo）对象数据结构
 * 
 * @author MengMeng
 * 
 */
public class Geo implements Parcelable {
	private static final String TAG = "Geo";

	/** 经度坐标 */
	public String longitude;
	/** 维度坐标 */
	public String latitude;
	/** 所在城市的城市代码 */
	public String city;
	/** 所在省份的省份代码 */
	public String province;
	/** 所在城市的城市名称 */
	public String city_name;
	/** 所在省份的省份名称 */
	public String province_name;
	/** 所在的实际地址，可以为空 */
	public String address;
	/** 地址的汉语拼音，不是所有情况都会返回该字段 */
	public String pinyin;
	/** 更多信息，不是所有情况都会返回该字段 */
	public String more;

	public Geo() {

	}

	/**
	 * 将json字符串解析成Geo对象
	 * 
	 * @param jsonString
	 *            待解析的json字符串
	 * @return 解析出来的Geo对象
	 */
	public static Geo parse(String jsonString) {
		if (TextUtils.isEmpty(jsonString)) {
			return null;
		}
		try {
			return Geo.parse(JSON.parseObject(jsonString));
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage());
		}

		return null;
	}

	/**
	 * 将JSONObject解析成Geo对象
	 * 
	 * @param jsonObject
	 *            待解析的JSONObject
	 * @return 解析出来的Geo对象
	 */
	public static Geo parse(JSONObject jsonObject) {
		if (null == jsonObject) {
			return null;
		}
		Geo geo = new Geo();

		geo.longitude = jsonObject.getString("longitude");
		geo.latitude = jsonObject.getString("latitude");
		geo.city = jsonObject.getString("city");
		geo.province = jsonObject.getString("province");
		geo.city_name = jsonObject.getString("city_name");
		geo.province_name = jsonObject.getString("province_name");
		geo.address = jsonObject.getString("address");
		geo.pinyin = jsonObject.getString("pinyin");
		geo.more = jsonObject.getString("more");
		return geo;
	}

	public Geo(Parcel in) {
		longitude = in.readString();
		latitude = in.readString();
		city = in.readString();
		province = in.readString();
		city_name = in.readString();
		province_name = in.readString();
		address = in.readString();
		pinyin = in.readString();
		more = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Parcelable.Creator<Geo> CREATOR = new Parcelable.Creator<Geo>() {
		public Geo createFromParcel(Parcel in) {
			return new Geo(in);
		}

		public Geo[] newArray(int size) {
			return new Geo[size];
		}
	};

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(longitude);
		dest.writeString(latitude);
		dest.writeString(city);
		dest.writeString(province);
		dest.writeString(city_name);
		dest.writeString(province_name);
		dest.writeString(address);
		dest.writeString(pinyin);
		dest.writeString(more);

	}

}