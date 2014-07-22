package com.tapp.utils;

import android.content.Context;
import android.view.Gravity;

public class Toast {

	public static void displayText(Context context, String message) {

		android.widget.Toast toast = android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static void displayText(Context context, int stringId) {

		android.widget.Toast toast = android.widget.Toast.makeText(context, context.getString(stringId), android.widget.Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
