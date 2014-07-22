package com.tapp.utils;

import java.lang.reflect.Field;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

public class TypefaceUtils {

	public static final int NORMAL = 0;
	public static final int BOLD = 1;
	public static final int THIN = 2;

	private static TypefaceUtils mInstance = null;
	private Typeface mTypeface;
	private Typeface mBoldTypeface;
	private Typeface mThinTypeface;

	public static TypefaceUtils getInstance(Context c) {
		if (mInstance == null) {
			mInstance = new TypefaceUtils(c);
		}
		return mInstance;
	}

	private TypefaceUtils(Context c) {

		mTypeface = Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-Light.ttf");
		mBoldTypeface = Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-Medium.ttf");
//		mBoldTypeface = Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-Regular.ttf");
		mThinTypeface = Typeface.createFromAsset(c.getAssets(), "fonts/Roboto-Thin.ttf");
	}

	private TypefaceUtils() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Method to set typeface on TextView or button
	 * 
	 * @param mTextView
	 * @param style
	 */
	public void applyTypeface(TextView mTextView, int style) {
		if (mTextView == null) {
			return;
		}
		switch (style) {
			case NORMAL :
				mTextView.setTypeface(mTypeface);
				break;
			case BOLD :
				mTextView.setTypeface(mBoldTypeface);
				break;
			case THIN :
				mTextView.setTypeface(mThinTypeface);
				break;
			default :
				mTextView.setTypeface(mTypeface);
				break;
		}
	}

	/**
	 * Method to set typeface on TextView or button
	 * 
	 * @param mTextView
	 */
	public void applyTypeface(TextView mTextView) {
		this.applyTypeface(mTextView, Typeface.NORMAL);
	}

	/**
	 * Using reflection to override default typeface NOTICE: DO NOT FORGET TO
	 * SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE OVERRIDDEN
	 * 
	 * @param context
	 *            to work with assets
	 * @param defaultFontNameToOverride
	 *            for example "monospace"
	 * @param customFontFileNameInAssets
	 *            file name of the font from assets
	 */
	public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
		try {
			final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

			final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
			defaultFontTypefaceField.setAccessible(true);
			defaultFontTypefaceField.set(null, customFontTypeface);
		} catch (Exception e) {
			Log.e("Error in font", "Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride);
		}
	}
}
