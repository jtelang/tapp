package com.tapp.service;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.tapp.SplashActivity;
import com.tapp.data.ConstantData;

public class HandleNotification extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences sharedPref = getSharedPreferences(ConstantData.SHARED_PREF_NAME, MODE_PRIVATE);

		if (!sharedPref.getBoolean("isAppRunning", false)) {

			Intent intent = new Intent(HandleNotification.this, SplashActivity.class);
			startActivity(intent);
		}
		finish();
	}
}
