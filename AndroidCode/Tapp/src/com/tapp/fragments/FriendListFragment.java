package com.tapp.fragments;

import java.util.ArrayList;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.RawContacts;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.tapp.R;
import com.tapp.adapters.FriendListAdapter;
import com.tapp.base.BaseFragment;
import com.tapp.data.ConstantData;
import com.tapp.data.ContactData;

public class FriendListFragment extends BaseFragment {

	private View view = null;
	private ListView listView = null;
	private TextView txtEmptyView = null;

	private ArrayList<com.tapp.data.ContactData> list = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_friend_list, null);

		listView = (ListView) view.findViewById(R.id.listView);
		txtEmptyView = (TextView) view.findViewById(R.id.txtEmptyView);
		listView.setEmptyView(txtEmptyView);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setContentView(view);
		setContentShown(false);

		new Thread(new Runnable() {
			@Override
			public void run() {

				getContactList();

				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {

						listView.setAdapter(new FriendListAdapter(getActivity(), list));
						setContentShown(true);
					}
				});
			}
		}).start();

	}

	private void getContactList() {

		try {

			list = new ArrayList<ContactData>();

			if (!ConstantData.DB.isContactExistInDB()) {

				insertContactsToDB();

				JSONArray jArray = new JSONArray(getString(R.string.temp_json));

				ConstantData.DB.beginTransaction();

				for (int i = 0; i < jArray.length(); i++) {

					JSONObject jObj = jArray.getJSONObject(i);

					ConstantData.DB.updateContactByPhoneNo(jObj.getString("phoneNo"), jObj.getInt("contactTypeFlag"), jObj.getString("photoUrl"), jObj.getString("status"));
				}

				ConstantData.DB.endTransaction();
			}

			list = ConstantData.DB.getContactList();

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in getContactList", e.toString());
		} finally {
			if (ConstantData.DB.isTransactionRunning()) {
				ConstantData.DB.endTransaction();
			}
		}
	}

	private void insertContactsToDB() {

		try {

			ContentResolver cr = getActivity().getContentResolver();
			Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

			if (cur.getCount() > 0) {

				ConstantData.DB.beginTransaction();

				while (cur.moveToNext()) {

					int id = cur.getInt(cur.getColumnIndex(ContactsContract.Contacts._ID));
					String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

					if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {

						Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID, ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.NUMBER},
								ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{String.valueOf(id)}, null);

						// pCur.moveToFirst();
						// for (int i = 0; i < pCur.getColumnCount(); i++) {
						// Log.e("Cursor", pCur.getColumnName(i)+
						// " -------- "+pCur.getString(i));
						// }

						while (pCur.moveToNext()) {

							int rawContactId = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.RAW_CONTACT_ID));
							// int type =
							// pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
							String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
							phoneNo = PhoneNumberUtils.stripSeparators(phoneNo);

							Uri imageUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(id));

							ConstantData.DB.insertContact(rawContactId, phoneNo, name, 0, imageUri.toString(), getString(R.string.available));

						}
						pCur.close();

					}
				}
				ConstantData.DB.endTransaction();
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in getContactList", e.toString());
		} finally {
			if (ConstantData.DB.isTransactionRunning()) {
				ConstantData.DB.endTransaction();
			}
		}
	}
	private String getContactType(int type) {

		switch (type) {

			case ContactsContract.CommonDataKinds.Phone.TYPE_CUSTOM :
				return "Custom";
			case ContactsContract.CommonDataKinds.Phone.TYPE_HOME :
				return "Home";
			case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE :
				return "Mobile";
			case ContactsContract.CommonDataKinds.Phone.TYPE_ASSISTANT :
				return "Assistant";
			case ContactsContract.CommonDataKinds.Phone.TYPE_CALLBACK :
				return "Callback";
			case ContactsContract.CommonDataKinds.Phone.TYPE_CAR :
				return "Car";
			case ContactsContract.CommonDataKinds.Phone.TYPE_COMPANY_MAIN :
				return "Company Main";
			case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_HOME :
				return "Fax Home";
			case ContactsContract.CommonDataKinds.Phone.TYPE_FAX_WORK :
				return "Fax work";
			case ContactsContract.CommonDataKinds.Phone.TYPE_ISDN :
				return "ISDN";
			case ContactsContract.CommonDataKinds.Phone.TYPE_MAIN :
				return "Main";
			case ContactsContract.CommonDataKinds.Phone.TYPE_MMS :
				return "MMS";
			case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER_FAX :
				return "Other Fax";
			case ContactsContract.CommonDataKinds.Phone.TYPE_PAGER :
				return "Pager";
			case ContactsContract.CommonDataKinds.Phone.TYPE_RADIO :
				return "Radio";
			case ContactsContract.CommonDataKinds.Phone.TYPE_TELEX :
				return "Telex";
			case ContactsContract.CommonDataKinds.Phone.TYPE_TTY_TDD :
				return "TTY TDO";
			case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE :
				return "Work Mobile";
			case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_PAGER :
				return "Work Pager";
			default :
				return "Other";
		}
	}

	/**
	 * Comparator to sort concert's list by name
	 */
	Comparator<ContactData> comparatorByName = new Comparator<ContactData>() {

		@Override
		public int compare(ContactData arg0, ContactData arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}
	};
}
