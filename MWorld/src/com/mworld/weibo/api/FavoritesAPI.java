package com.mworld.weibo.api;

import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

/**
 * 此类封装了收藏的接口。
 */
public class FavoritesAPI extends BaseAPI {

	private static final String SERVER_URL_PRIX = API_SERVER + "/favorites";

	public FavoritesAPI(String accessToken) {
		super(accessToken);
	}

	/**
	 * 获取当前登录用户的收藏列表。
	 * 
	 * @param count
	 *            单页返回的记录条数，默认为50
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param listener
	 *            异步请求回调接口
	 */
	public void favorites(int count, int page, AjaxCallBack<String> listener) {
		AjaxParams params = buildCountPage(count, page);
		requestAsync(SERVER_URL_PRIX + ".json", params, HTTPMETHOD_GET,
				listener);
	}

	/**
	 * 获取当前用户的收藏列表的ID。
	 * 
	 * @param count
	 *            单页返回的记录条数，默认为50
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param listener
	 *            异步请求回调接口
	 */
	public void ids(int count, int page, AjaxCallBack<String> listener) {
		AjaxParams params = buildCountPage(count, page);
		requestAsync(SERVER_URL_PRIX + "/ids.json", params, HTTPMETHOD_GET,
				listener);
	}

	/**
	 * 根据收藏ID获取指定的收藏信息。
	 * 
	 * @param id
	 *            需要查询的收藏ID
	 * @param listener
	 *            异步请求回调接口
	 */
	public void show(long id, AjaxCallBack<String> listener) {
		AjaxParams params = new AjaxParams();
		params.put("id", String.valueOf(id));
		requestAsync(SERVER_URL_PRIX + "/show.json", params, HTTPMETHOD_GET,
				listener);
	}

	/**
	 * 根据标签获取当前登录用户该标签下的收藏列表。
	 * 
	 * @param tid
	 *            需要查询的标签ID
	 * @param count
	 *            单页返回的记录条数，默认为50
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param listener
	 *            异步请求回调接口
	 */
	public void byTags(long tid, int count, int page,
			AjaxCallBack<String> listener) {
		AjaxParams params = buildCountPage(count, page);
		params.put("tid", String.valueOf(tid));
		requestAsync(SERVER_URL_PRIX + "/by_tags.json", params, HTTPMETHOD_GET,
				listener);
	}

	/**
	 * 获取当前登录用户的收藏标签列表。
	 * 
	 * @param count
	 *            单页返回的记录条数，默认为50
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param listener
	 *            异步请求回调接口
	 */
	public void tags(int count, int page, AjaxCallBack<String> listener) {
		AjaxParams params = buildCountPage(count, page);
		requestAsync(SERVER_URL_PRIX + "/tags.json", params, HTTPMETHOD_GET,
				listener);
	}

	/**
	 * 获取当前用户某个标签下的收藏列表的ID。
	 * 
	 * @param tid
	 *            需要查询的标签ID。
	 * @param count
	 *            单页返回的记录条数，默认为50
	 * @param page
	 *            返回结果的页码，默认为1
	 * @param listener
	 *            异步请求回调接口
	 */
	public void byTagsIds(long tid, int count, int page,
			AjaxCallBack<String> listener) {
		AjaxParams params = buildCountPage(count, page);
		params.put("tid", String.valueOf(tid));
		requestAsync(SERVER_URL_PRIX + "/by_tags/ids.json", params,
				HTTPMETHOD_GET, listener);
	}

	/**
	 * 添加一条微博到收藏里。
	 * 
	 * @param id
	 *            要收藏的微博ID
	 * @param listener
	 *            异步请求回调接口
	 */
	public void create(long id, AjaxCallBack<String> listener) {
		AjaxParams params = new AjaxParams();
		params.put("id", String.valueOf(id));
		requestAsync(SERVER_URL_PRIX + "/create.json", params, HTTPMETHOD_POST,
				listener);
	}

	/**
	 * 取消收藏一条微博。
	 * 
	 * @param id
	 *            要取消收藏的微博ID。
	 * @param listener
	 *            异步请求回调接口
	 */
	public void destroy(long id, AjaxCallBack<String> listener) {
		AjaxParams params = new AjaxParams();
		params.put("id", String.valueOf(id));
		requestAsync(SERVER_URL_PRIX + "/destroy.json", params,
				HTTPMETHOD_POST, listener);
	}

	/**
	 * 根据收藏ID批量取消收藏。
	 * 
	 * @param ids
	 *            要取消收藏的收藏ID，最多不超过10个
	 * @param listener
	 *            异步请求回调接口
	 */
	public void destroyBatch(long[] ids, AjaxCallBack<String> listener) {
		AjaxParams params = new AjaxParams();
		StringBuilder strb = new StringBuilder();
		for (long id : ids) {
			strb.append(id).append(",");
		}
		strb.deleteCharAt(strb.length() - 1);
		params.put("ids", strb.toString());
		requestAsync(SERVER_URL_PRIX + "/destroy_batch.json", params,
				HTTPMETHOD_POST, listener);
	}

	/**
	 * 更新一条收藏的收藏标签。
	 * 
	 * @param id
	 *            需要更新的收藏ID
	 * @param tags
	 *            需要更新的标签内容，最多不超过2条
	 * @param listener
	 *            异步请求回调接口
	 */
	public void tagsUpdate(long id, String[] tags, AjaxCallBack<String> listener) {
		AjaxParams params = new AjaxParams();
		params.put("id", String.valueOf(id));
		StringBuilder strb = new StringBuilder();
		for (String tag : tags) {
			strb.append(tag).append(",");
		}
		strb.deleteCharAt(strb.length() - 1);
		params.put("tags", strb.toString());
		requestAsync(SERVER_URL_PRIX + "/tags/update.json", params,
				HTTPMETHOD_POST, listener);
	}

	/**
	 * 更新当前登录用户所有收藏下的指定标签。
	 * 
	 * @param id
	 *            需要更新的标签ID
	 * @param tag
	 *            需要更新的标签内容
	 * @param listener
	 *            异步请求回调接口
	 */
	public void tagsUpdateBatch(long id, String tag,
			AjaxCallBack<String> listener) {
		AjaxParams params = new AjaxParams();
		params.put("tid", String.valueOf(id));
		params.put("tag", tag);
		requestAsync(SERVER_URL_PRIX + "/tags/update_batch.json", params,
				HTTPMETHOD_POST, listener);
	}

	/**
	 * 删除当前登录用户所有收藏下的指定标签。
	 * 
	 * @param tid
	 *            需要删除的标签ID
	 * @param listener
	 *            异步请求回调接口
	 */
	public void tagsDestroyBatch(long tid, AjaxCallBack<String> listener) {
		AjaxParams params = new AjaxParams();
		params.put("tid", String.valueOf(tid));
		requestAsync(SERVER_URL_PRIX + "/tags/destroy_batch.json", params,
				HTTPMETHOD_POST, listener);
	}

	private AjaxParams buildCountPage(int count, int page) {
		AjaxParams params = new AjaxParams();
		params.put("count", String.valueOf(count));
		params.put("page", String.valueOf(page));
		return params;
	}
}
