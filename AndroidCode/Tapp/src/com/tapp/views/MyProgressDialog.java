package com.tapp.views;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.tapp.R;
import com.tapp.utils.Log;

/**
 * MyProgressDialog class is used to show progress bar with transparent
 * background using custom theme
 * 
 * @author
 * 
 */

public class MyProgressDialog extends Dialog {

	public MyProgressDialog(Context context) {
		super(context, R.style.Theme_CustomDialog);
		init();
	}

	// initialize progress dialog
	private void init() {

		try {
			if (super.isShowing()) {
				super.dismiss();
			}
			super.requestWindowFeature(Window.FEATURE_NO_TITLE);
			super.setContentView(R.layout.fragment_progress);
			// super.addContentView(new ProgressBar(getContext()), new
			// LayoutParams(LayoutParams.WRAP_CONTENT,
			// LayoutParams.WRAP_CONTENT));

			super.setCancelable(false);
			super.show();
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in MyProgressDialog", e.toString());
		}
	}

	// dismiss progress dialog
	public void dismiss() {

		if (this != null && this.isShowing()) {
			super.dismiss();
		}
	}
}
