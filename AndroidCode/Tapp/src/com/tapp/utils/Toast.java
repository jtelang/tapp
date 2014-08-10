package com.tapp.utils;

import android.content.Context;
import android.view.Gravity;

import com.tapp.R;

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

	public static void displayError(Context context, String exception) {

		String message = exception;

		if (exception.contains("UnknownHostException") || exception.contains("SocketException") || exception.contains("ClientProtocolException") || exception.contains("SocketTimeoutException") || exception.contains("IOException")) {
			message = context.getString(R.string.no_internet);
		} else if (exception.contains("OutOfMemoryError")) {
			message = context.getString(R.string.out_of_memory);
		}

		android.widget.Toast toast = android.widget.Toast.makeText(context, message, android.widget.Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
