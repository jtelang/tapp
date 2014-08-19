package com.tapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.tapp.data.ConstantData;

public class PrefManager {

	private static PrefManager instance = null;
	private SharedPreferences prefs;

	public static PrefManager getInstance(Context context) {
		if (instance == null)
			instance = new PrefManager(context);
		return instance;
	}

	private PrefManager(Context context) {
		prefs = new ObscuredSharedPreferences(context, context.getSharedPreferences(ConstantData.SHARED_PREF_NAME, Context.MODE_PRIVATE));
	}

	public SharedPreferences getPrefs() {
		return prefs;
	}

}
