package com.tapp.service;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import com.tapp.R;
import com.tapp.data.ConstantData;
import com.tapp.data.ContactData;
import com.tapp.data.DBHelper;
import com.tapp.request.TappRequestBuilder;
import com.tapp.utils.HTTPUtils;

public class TappContactService extends Service {

	private static final String TAG = TappContactService.class.getName();
	private static TappContactService instance = null;
	private int mContactCount;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public static TappContactService getInstance() {
		return instance;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		instance = this;

		Log.e(TAG, "Service started");

		mContactCount = getContactCount();
		this.getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, mObserver);
	}

	private int getContactCount() {
		Cursor cursor = null;
		try {
			cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
			if (cursor != null) {
				return cursor.getCount();
			} else {
				return 0;
			}
		} catch (Exception ignore) {
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return 0;
	}

	private ContentObserver mObserver = new ContentObserver(new Handler()) {

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);

			new Thread(new Runnable() {
				@Override
				public void run() {

					final int currentCount = getContactCount();
					if (currentCount < mContactCount) {
						// DELETE HAPPEN.
						Log.e(TAG, "DELETE HAPPEN");
						checkContact(0);
					} else if (currentCount == mContactCount) {
						// UPDATE HAPPEN.
						Log.e(TAG, "UPDATE HAPPEN");
						checkContact(1);
					} else {
						// INSERT HAPPEN.
						Log.e(TAG, "INSERT HAPPEN");
						checkContact(1);
					}
					mContactCount = currentCount;
				}
			}).start();
		}

	};

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getContentResolver().unregisterContentObserver(mObserver);
		Log.e(TAG, "Service stopped");

		Intent intent = new Intent("com.tapp.contactservice.STOPPED");
		sendBroadcast(intent);
	}

	/**
	 * 
	 * @param operationFlag
	 *            0 means delete, 1 means add/update
	 */

	private void checkContact(int operationFlag) {

		try {

			if (ConstantData.DB == null || !ConstantData.DB.isOpen()) {
				ConstantData.DB = new DBHelper(this);
				ConstantData.DB.open();
			}

			ContentResolver cr = getContentResolver();
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
			String allRawContactIds = "";

			if (cur.getCount() > 0) {

				while (cur.moveToNext()) {

					int id = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts._ID));
					String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

					if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

						Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID, ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.NUMBER},
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{String.valueOf(id)}, null);

						while (pCur.moveToNext()) {

							int rawContactId = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
							String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							phoneNo = PhoneNumberUtils.stripSeparators(phoneNo);
							Uri imageUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(id));

							if (operationFlag == 0) {

								if (allRawContactIds.equals("")) {
									allRawContactIds = "" + rawContactId;
								} else {
									allRawContactIds += "," + rawContactId;
								}

							} else {

								if (!ConstantData.DB.isContactExistInDB(rawContactId, name, phoneNo)) {

									ContactData data = ConstantData.DB.getContact(rawContactId);

									if (data == null) {
										// new added contact
										// Log.e(TAG,
										// "New added contact : raw_id = " +
										// rawContactId + " name = " + name +
										// " phone = " + phoneNo);
										int userType = syncContact(phoneNo);
										ConstantData.DB.insertContact(rawContactId, phoneNo, name, userType, imageUri.toString(), getString(R.string.available));

									} else {

										if (!data.getPhone().equals(phoneNo)) {

											// phone no updated
											// Log.e(TAG,
											// "Update contact(phone) : raw_id = "
											// + rawContactId + " name = " +
											// name + " phone = " + phoneNo);
											int userType = syncContact(phoneNo);
											ConstantData.DB.updateContactByRawId(rawContactId, name, phoneNo, userType, imageUri.toString(), getString(R.string.available));

										} else {

											// name or other details updated
											// Log.e(TAG,
											// "Update contact(name) : raw_id = "
											// + rawContactId + " name = " +
											// name + " phone = " + phoneNo);
											ConstantData.DB.updateContactByRawId(rawContactId, name, phoneNo, data.getUserType(), imageUri.toString(), data.getBio());

										}
									}
								}
							}
						}
						pCur.close();

					}
				}

				if (operationFlag == 0) {

					int rawId = ConstantData.DB.getDeletedContactRawId(allRawContactIds);

					if (rawId != -1) {
						// remove contact
						// Log.e(TAG, "Delete contact : raw_id = " + rawId);
						ConstantData.DB.deleteContact(rawId);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error in checkContact : " + e.toString());
		}
	}

	private int syncContact(String phoneNo) {

		try {

			String response = HTTPUtils.doHTTPGetRequest(ConstantData.SERVER_URL + String.format(TappRequestBuilder.WS_CONTACT_SYNC, ConstantData.USER_ID, phoneNo));

			JSONObject jObjResponse = new JSONObject(response);

			if (jObjResponse.getInt("status") == 0) {

				JSONArray jArrayResult = jObjResponse.getJSONArray("result");

				if (jArrayResult.length() > 0) {

					JSONObject jObj = jArrayResult.getJSONObject(0);

					return jObj.getInt("userType");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error in syncContact : " + e.toString());
		}

		return 0;
	}
}
