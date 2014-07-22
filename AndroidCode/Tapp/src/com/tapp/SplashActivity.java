package com.tapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.tapp.data.ConstantData;
import com.tapp.data.DBHelper;
import com.tapp.service.TappContactService;

public class SplashActivity extends Activity {

	// for set the splash screen animation time
	private final int SPLASH_DISPLAY_LENGHT = 3000;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {

			// setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen);
			// setTheme(com.WazaBe.HoloEverywhere.R.style.Holo_Theme_Light_NoActionBar_Fullscreen);

			setContentView(R.layout.activity_splash);

			// LinearLayout splashScreenLayout = (LinearLayout)
			// findViewById(R.id.splashScreenLayout);
			//
			// Animation hyperSpaceJumpAnimation =
			// AnimationUtils.loadAnimation(SplashScreen.this,
			// R.anim.hyperspace_jump);
			// splashScreenLayout.startAnimation(hyperSpaceJumpAnimation);

			try {

				if (ConstantData.DB == null || !ConstantData.DB.isOpen()) {
					ConstantData.DB = new DBHelper(this);
					ConstantData.DB.open();
				}

			} catch (Exception e) {
				e.printStackTrace();
				Log.e("Error in DB open", e.toString());
			}

			// ConstantData.LOCAL_PATH = "/data/data/" +
			// getApplication().getPackageName() + "/friend_images/";
			//
			// if (!new File(ConstantData.LOCAL_PATH).exists()) {
			//
			// Utils.createDirectory(SplashScreen.this,
			// ConstantData.LOCAL_PATH);
			//
			// }
			//
			new Handler().postDelayed(new Runnable() {
				public void run() {

					Intent i = new Intent(SplashActivity.this, LoginActivity.class);
					startActivity(i);
					finish();
				}
			}, SPLASH_DISPLAY_LENGHT);

			if (TappContactService.getInstance() == null) {
				Intent intent = new Intent(SplashActivity.this, TappContactService.class);
				startService(intent);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void onBackPressed() {

		System.exit(0);

		super.onBackPressed();
	}

}