package com.tapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.tapp.data.ConstantData;

public class CryptoManager {

	private static CryptoManager instance = null;
	private SharedPreferences prefs;

	public static CryptoManager getInstance(Context context) {
		if (instance == null)
			instance = new CryptoManager(context);
		return instance;
	}

	private CryptoManager(Context context) {
		prefs = new ObscuredSharedPreferences(context, context.getSharedPreferences(ConstantData.SHARED_PREF_NAME, Context.MODE_PRIVATE));
	}

	public SharedPreferences getPrefs() {
		return prefs;
	}

}
