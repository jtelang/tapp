package com.tapp.request;

public class TappRequestBuilder implements PARAMS {

	public static final String WS_ALBUMS = "Music/albums.json";
	public static final String WS_ARTISTS = "Music/artists.json";
	public static final String WS_GENRES = "Music/genres.json";
	public static final String WS_SONGS = "Music/songs.json";

	public static final String WS_ALBUMS_PARAM = "Music/albums/show/%s.json";
	public static final String WS_SONGS_PARAM = "Music/songs/show/%s.json";

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
