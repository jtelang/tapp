package com.tapp.request;

import java.util.HashMap;

import org.json.JSONObject;

import com.tapp.utils.Log;

public class TappRequestBuilder implements PARAMS {

	public static final String WS_ALBUMS = "albums";
	public static final String WS_ARTISTS = "artists";
	public static final String WS_GENRES = "genres";
	public static final String WS_ALBUMS_PARAM = "albums/show/%s";
	public static final String WS_SONGS_PARAM = "songs/show/%s";
	public static final String WS_GAME_TYPE = "gametype";
	public static final String WS_REGISTER_USER = "user";
	public static final String WS_GET_PROFILE = "myUsers/show";
	public static final String WS_MY_FOLLOWERS = "myFollowers/show/%s";

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
