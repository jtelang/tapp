package com.tapp.utils;

import java.text.DecimalFormat;

import android.graphics.Bitmap;

public class Utils {

	public static boolean checkEmail(String email) {
		if (email.matches("[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.+[a-zA-Z0-9.]+")) {
			return true;
		}

		return false;
	}

	public static boolean checkDigit(String email) {
		if (email.matches("[0-9]+")) {
			return true;
		}

		return false;
	}

	public static boolean isEmpty(String text) {

		if (text != null && !text.equalsIgnoreCase("null") && !text.trim().equals("")) {
			return false;
		}
		return true;
	}

	public static String formatDecimal(String val) {

		DecimalFormat form = new DecimalFormat("* ##,##,#,##0.00");

		return form.format(Double.parseDouble(val.replace(",", ""))).trim();
	}

	public static Bitmap resizeBitmapWithRatio(Bitmap bitmap, int width) {

		int bwidth = bitmap.getWidth();
		int bheight = bitmap.getHeight();
		int height = (int) Math.floor((double) bheight * ((double) width / (double) bwidth));
		return Bitmap.createScaledBitmap(bitmap, width, height, true);
	}
}
