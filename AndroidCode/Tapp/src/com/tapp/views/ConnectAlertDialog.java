package com.tapp.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ConnectAlertDialog extends AlertDialog {

	protected Builder builder;
	protected Context context;
	protected Activity activity;

	public ConnectAlertDialog(Context context) {
		super(context);
		this.context = context;
		this.activity = (Activity) context;
		setBuilder();
	}

	public ConnectAlertDialog(Context context, int theme) {
		super(context);
		this.context = context;
		this.activity = (Activity) context;
		setBuilder(theme);
	}

	protected void setBuilder() {
		builder = new Builder(context);
	}

	protected void setBuilder(int theme) {
		builder = new Builder(context);

		setTitle("");
		setMessage("");
		setPositiveButton("");
	}

	public void setTitle(String title) {
		builder.setTitle(title);
	}

	public void setTitle(int resid) {
		builder.setTitle(resid);
	}

	public void setMessage(String message) {
		builder.setMessage(message);
	}

	public void setMessage(int resid) {
		builder.setMessage(resid);
	}

	public void setCancelable(boolean enabled) {
		builder.setCancelable(enabled);
	}

	public void setPositiveButton(String message) {
		setPositiveButton(message, new PositiveButtonClickListener());
	}

	public void setPositiveButton(int resid) {
		setPositiveButton(resid, new PositiveButtonClickListener());
	}

	public void setPositiveButton(String message, DialogInterface.OnClickListener onClickListner) {
		builder.setPositiveButton(message, onClickListner);
	}

	public void setPositiveButton(int resid, DialogInterface.OnClickListener onClickListner) {
		builder.setPositiveButton(resid, onClickListner);
	}

	public void setNegativeButton(String message) {
		builder.setNegativeButton(message, new NegativeButtonClickListener());
	}

	public void setNegativeButton(int resid) {
		builder.setNegativeButton(resid, new NegativeButtonClickListener());
	}

	public void setNegativeButton(String message, DialogInterface.OnClickListener onClickListner) {
		builder.setNegativeButton(message, onClickListner);
	}

	public void setNegativeButton(int resid, DialogInterface.OnClickListener onClickListner) {
		builder.setNegativeButton(resid, onClickListner);
	}

	protected class PositiveButtonClickListener implements DialogInterface.OnClickListener {

		public void onClick(DialogInterface dialog, int which) {
			dialog.cancel();
			dialog.dismiss();

			activity.finish();
		}
	}

	protected class NegativeButtonClickListener implements DialogInterface.OnClickListener {

		public void onClick(DialogInterface dialog, int which) {
			dialog.cancel();
		}
	}

	public void show() {
		if (builder != null) {
			if (!activity.isFinishing()) {
				builder.create().show();
			}
		}
	}

}
