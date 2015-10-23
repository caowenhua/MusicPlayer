package org.yekeqi.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {
	public static JSONObject fromObject(Object object) throws JSONException {
		GsonBuilder gsonb = new GsonBuilder();
		gsonb.setDateFormat("yyyy-MM-dd hh:mm:ss");
		Gson gson = gsonb.create();
		return new JSONObject(gson.toJson(object));
	}

	public static String toString(Object object) {
		Gson gson = new Gson();
		return gson.toJson(object);
	}

	public static JSONArray fromObject_Array(Object object)
			throws JSONException {
		GsonBuilder gsonb = new GsonBuilder();
		Gson gson = gsonb.create();
		gsonb.setDateFormat("yyyy-MM-dd HH:mm:ss");
		return new JSONArray(gson.toJson(object));
	}


	public static Object toBean(String jsonString, Class<?> beanclass) {
		GsonBuilder gsonb = new GsonBuilder();
		gsonb.setDateFormat("yyyy-MM-dd HH:mm:ss");
		Gson gson = gsonb.create();
		return gson.fromJson(jsonString, beanclass);
	}

	/**
	 * <code>toBean</code>
	 * 
	 * @description: TODO(json对象转化为类)
	 * @param object
	 * @param beanclass
	 * @return
	 * @since Apr 11, 2011 zhangzhanqiang
	 */

	public static Object toBean(JSONObject object, Class<?> beanclass) {
		return toBean(object.toString(), beanclass);
	}

	public static boolean isBadJson(String json) {
		return !isGoodJson(json);
	}

	public static boolean isGoodJson(String json) {
		// if (StringUtils.isBlank(json)) {
		// return false;
		// }
		try {
			new JsonParser().parse(json);
			return true;
		} catch (JsonParseException e) {
			// logger.error("bad json: " + json);
			return false;
		}
	}

}
