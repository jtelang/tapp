package com.tapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootStartReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent1) {

		try {

//			Intent intent = new Intent(context, TappContactService.class);
//			context.startService(intent);

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Tapp - BootStartReceiver", "Error in start Service : " + e.toString());
		}
	}

}
