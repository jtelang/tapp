package com.tapp.request;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONObject;

import com.tapp.data.ConstantData;
import com.tapp.utils.Log;

public class TappRequestBuilder implements PARAMS {

	public static final String VERSION = "1";

	// web service
	public static final String WS_ALBUMS = "albums";
	public static final String WS_ARTISTS = "artists";
	public static final String WS_GENRES = "genres";
	public static final String WS_ALBUMS_PARAM = "albums/show/%s";
	public static final String WS_SONGS_PARAM = "songs/show/%s";
	public static final String WS_GAME_TYPE = "gametype";
	public static final String WS_GAMELIST_BY_CATEGORY = "myGames/show/%s";
	public static final String WS_REGISTER_USER = "user";
	public static final String WS_GET_PROFILE = "myUsers/show?pn=%s";
	public static final String WS_MY_FOLLOWERS = "myFollowers/show/%s";
	public static final String WS_ADD_FOLLOWER = "myFollowers/save?uid=%s&fid=%s";
	public static final String WS_BUY_MEDIA = "mediabuy";

	public static HashMap<String, String> getRegisterUserRequest(String phone) {

		JSONObject jObj = new JSONObject();

		try {

			jObj.put(TAG_PHONE, phone);

		} catch (Exception e) {
			e.printStackTrace();
		}

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(TAG_DATA, jObj.toString());
		Log.e("JSON Request", jObj.toString());

		return parameters;
	}

	public static HashMap<String, String> getProfileRequest(String phone) {

		JSONObject jObj = new JSONObject();

		try {

			jObj.put(TAG_PHONE, phone);

		} catch (Exception e) {
			e.printStackTrace();
		}

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(TAG_DATA, jObj.toString());
		Log.e("JSON Request", jObj.toString());

		return parameters;
	}

	public static HashMap<String, String> getBuyMediaRequest(String mediaId, String mediaType, String store) {

		JSONObject jObj = new JSONObject();

		try {

			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());

			jObj.put(TAG_USER, ConstantData.USER_ID);
			jObj.put(TAG_MEDIA_ID, mediaId);
			jObj.put(TAG_MEDIA_TYPE, mediaType);
			jObj.put(TAG_BUY_DATE, format.format(Calendar.getInstance().getTime()));
			jObj.put(TAG_RECOMMEND, true);
			jObj.put(TAG_STORE, store);

		} catch (Exception e) {
			e.printStackTrace();
		}

		HashMap<String, String> parameters = new HashMap<String, String>();
		parameters.put(TAG_DATA, jObj.toString());
		Log.e("JSON Request", jObj.toString());

		return parameters;
	}

	// public static HashMap<String, String> getLoginRequest(String userName,
	// String password, String email, int loginType) {
	//
	// JSONObject jObj = new JSONObject();
	//
	// try {
	//
	// jObj.put(TAG_SOCIAL_FLAG, loginType);
	// jObj.put(TAG_DEVICE_ID, ConstantData.GCM_REGISTERED_ID);
	// jObj.put(TAG_DEVICE_TYPE, "android");
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// HashMap<String, String> parameters = new HashMap<String, String>();
	// parameters.put(TAG_DATA, jObj.toString());
	// Log.e("JSON Request", jObj.toString());
	//
	// return parameters;
	// }

}
