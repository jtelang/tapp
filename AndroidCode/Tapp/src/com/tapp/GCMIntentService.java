package com.tapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.tapp.data.ConstantData;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "Tapp-GCM - Main";

	public GCMIntentService() {
		super(ConstantData.SENDER_ID);
	}

	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);

		ConstantData.GCM_REGISTERED_ID = registrationId;

		// registerGCMToken();
	}

	/**
	 * Method called on device unregistered
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		// ServerUtilities.unregister(context, registrationId);
	}

	/**
	 * Method called on Receiving a new message
	 * */
	@Override
	protected void onMessage(Context context, Intent intent) {
		Log.i(TAG, "Received message");
		Log.i(TAG, "intent" + intent.getDataString() + "\n" + intent.toString());

		String data = intent.getExtras().getString("TEST");
		Log.e("Notification Message", data);

		// int totalCount = 0;
		// String[] str = data.split(",");
		//
		// SharedPreferences sharedPref =
		// getSharedPreferences(ConstantData.SHARED_PREF_NAME, MODE_PRIVATE);
		// Editor editor = sharedPref.edit();
		//
		// for (int i = 0; i < str.length; i++) {
		//
		// String[] temp = str[i].split("#");
		//
		// int count = Integer.parseInt(temp[1]);
		// totalCount += count;
		//
		// editor.putInt(temp[0], count);
		// }
		//
		// editor.commit();
		//
		// if (totalCount > 0) {
		generateNotification(context, data);
		// }
	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		// notifies user
		// generateNotification(context, message);
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context, String message) {

		String title = context.getString(R.string.app_name)+ " Notification";

		Intent notificationIntent = new Intent(context, SplashActivity.class);
		// set intent so it does not start a new activity
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

		// notificationIntent.putExtra("isFromNotification", true);
//		notificationIntent.putExtra("redirectTo", redirectTo);
//		notificationIntent.putExtra("reference", reference);

		NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent contentIntent = PendingIntent.getActivity(context, new Random().nextInt(), notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_launcher).setContentTitle(title).setStyle(new NotificationCompat.BigTextStyle().bigText(message)).setContentText(message);
		mBuilder.setContentIntent(contentIntent);
		mBuilder.setAutoCancel(true);
		Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		mBuilder.setSound(alarmSound);
		mBuilder.setVibrate(new long[]{1000, 1000});
		mNotificationManager.notify(0, mBuilder.build());

		// int icon = R.drawable.ic_drawer;
		// String message = "You have " + totalCount +
		// " new updates for RCC App";
		//
		// long when = System.currentTimeMillis();
		// NotificationManager notificationManager = (NotificationManager)
		// context.getSystemService(Context.NOTIFICATION_SERVICE);
		// Notification notification = new Notification(icon, message, when);
		//
		// String title = context.getString(R.string.app_name);
		//
		// Intent notificationIntent = new Intent(context,
		// HandleNotification.class);
		// // set intent so it does not start a new activity
		// notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// PendingIntent intent = PendingIntent.getActivity(context, 0,
		// notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		// notification.setLatestEventInfo(context, title, message, intent);
		// notification.flags |= Notification.FLAG_AUTO_CANCEL;
		//
		// // Play default notification sound
		// notification.defaults |= Notification.DEFAULT_SOUND;
		//
		// // Vibrate if vibrate is enabled
		// notification.defaults |= Notification.DEFAULT_VIBRATE;
		// notificationManager.notify(0, notification);
	}

	private void registerGCMToken() {

		try {

			JSONArray jArray = new JSONArray();
			JSONObject jObj = new JSONObject();
			jObj.put("device_token", ConstantData.GCM_REGISTERED_ID);
			jObj.put("device_type", "2");
			jArray.put(jObj);

			URL uri = new URL("http://23.21.226.54/web/collegeapp/ws/register.php?bulk_data=" + jArray.toString());
			URLConnection conn = uri.openConnection();

			HttpURLConnection httpConn = (HttpURLConnection) conn;
			httpConn.setAllowUserInteraction(false);
			httpConn.setInstanceFollowRedirects(true);
			httpConn.setRequestMethod("GET");
			httpConn.connect();
			int response = httpConn.getResponseCode();
			if (response == HttpURLConnection.HTTP_OK) {
				InputStream is = httpConn.getInputStream();
				String result = convertStreamToString(is);
				Log.e("result", result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in registerGCMToken", e.toString());
		}
	}

	public static String convertStreamToString(InputStream is) throws OutOfMemoryError, Exception {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		if (sb != null && sb.length() > 0) {
			sb = sb.delete(sb.length() - 1, sb.length());
		}
		reader.close();
		is.close();
		return sb.toString();
	}
}
