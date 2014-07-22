package com.tapp.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;

import com.tapp.views.ConnectAlertDialog;

public class DialogHelper {
	public static Dialog popupMessage(Activity context, Integer title, Integer message, Integer okLabel, Integer cancelLabel, DialogInterface.OnClickListener okClickListener, DialogInterface.OnClickListener cancelClickListener, boolean cancelable) {
		String dialogTitle = (title != null) ? context.getString(title) : null;
		String dialogMessage = (message != null) ? context.getString(message) : null;
		String dialogOkLabel = (okLabel != null) ? context.getString(okLabel) : null;
		String dialogCancelLabel = (cancelLabel != null) ? context.getString(cancelLabel) : null;
		return popupMessage(context, dialogTitle, dialogMessage, dialogOkLabel, dialogCancelLabel, okClickListener, cancelClickListener, cancelable);
	}

	public static Dialog popupMessage(Activity context, Integer title, String message, Integer okLabel, Integer cancelLabel, DialogInterface.OnClickListener okClickListener, DialogInterface.OnClickListener cancelClickListener, boolean cancelable) {
		String dialogTitle = (title != null) ? context.getString(title) : null;
		String dialogOkLabel = (okLabel != null) ? context.getString(okLabel) : null;
		String dialogCancelLabel = (cancelLabel != null) ? context.getString(cancelLabel) : null;
		return popupMessage(context, dialogTitle, message, dialogOkLabel, dialogCancelLabel, okClickListener, cancelClickListener, cancelable);
	}

	public static Dialog popupMessage(Activity context, String title, String message, String okLabel, String cancelLabel, DialogInterface.OnClickListener okClickListener, DialogInterface.OnClickListener cancelClickListener, boolean cancelable) {
		ConnectAlertDialog con = new ConnectAlertDialog(context);
		con.setTitle(title);
		con.setMessage(message);
		con.setCancelable(cancelable);
		if (cancelLabel != null)
			con.setNegativeButton(cancelLabel, cancelClickListener);
		if (okLabel != null)
			con.setPositiveButton(okLabel, okClickListener);
		con.show();
		return con;
	}
}
