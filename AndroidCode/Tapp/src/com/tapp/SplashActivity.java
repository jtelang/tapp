package com.tapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.tapp.data.ConstantData;
import com.tapp.data.DBHelper;
import com.tapp.service.TappContactService;
import com.tapp.utils.ConnectivityTools;

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

			// if (TappContactService.getInstance() == null) {
			// Intent intent = new Intent(SplashActivity.this,
			// TappContactService.class);
			// startService(intent);
			// }

			new Handler().postDelayed(new Runnable() {
				public void run() {

					Intent i = new Intent(SplashActivity.this, LoginActivity.class);
					startActivity(i);
					finish();
				}
			}, SPLASH_DISPLAY_LENGHT);

			// if (ConnectivityTools.isNetworkAvailable(SplashActivity.this)) {
			// getRegisterIDForGCM();
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void getRegisterIDForGCM() {

		try {

			GCMRegistrar.checkDevice(this);
			GCMRegistrar.checkManifest(this);
			String registerId = GCMRegistrar.getRegistrationId(this);

			if (!ConstantData.GCM_REGISTERED_ID.equals(registerId)) {
				ConstantData.GCM_REGISTERED_ID = "";
			}

			if (ConstantData.GCM_REGISTERED_ID.equals("")) {

				GCMRegistrar.register(this, ConstantData.SENDER_ID);

				new Thread(new Runnable() {
					public void run() {

						try {
							Looper.prepare();
							while (ConstantData.GCM_REGISTERED_ID.equals("")) {
								Thread.sleep(500);
							}

							Log.i("Join8 GCM SenderId", ConstantData.GCM_REGISTERED_ID);

							Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
							startActivity(intent);
							finish();

						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}).start();

			} else {
				Log.i("Join8 GCM SenderId", ConstantData.GCM_REGISTERED_ID);
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in SplashScreen", e.toString());
		} finally {

			new Handler().postDelayed(new Runnable() {
				public void run() {

					Intent i = new Intent(SplashActivity.this, LoginActivity.class);
					startActivity(i);
					finish();
				}
			}, SPLASH_DISPLAY_LENGHT);
		}
	}

	@Override
	public void onBackPressed() {

		System.exit(0);

		super.onBackPressed();
	}

}