package com.tapp.request;

import java.util.HashMap;

import org.json.JSONObject;

import com.tapp.utils.Log;

public class TappRequestBuilder implements PARAMS {

	public static final String WS_ALBUMS = "albums.json";
	public static final String WS_ARTISTS = "artists.json";
	public static final String WS_GENRES = "genres.json";
	// public static final String WS_SONGS = "songs.json";
	public static final String WS_ALBUMS_PARAM = "albums/show/%s.json";
	public static final String WS_SONGS_PARAM = "songs/show/%s.json";
	public static final String WS_GAME_TYPE = "gametype.json";
	// public static final String WS_GAME_PARAM = "gamecollection/show/1.json";
	public static final String WS_REGISTER_USER = "user.json";
	public static final String WS_GET_PROFILE = "profile.json";

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
