package com.tapp.fragments;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;

import com.tapp.R;
import com.tapp.ViewProfileActivity;
import com.tapp.adapters.FriendListAdapter;
import com.tapp.base.BaseFragment;
import com.tapp.data.ConstantData;
import com.tapp.data.ContactData;
import com.tapp.network.NetworManager;
import com.tapp.network.RequestListener;
import com.tapp.network.RequestMethod;
import com.tapp.request.TappRequestBuilder;
import com.tapp.utils.KeyboardUtils;
import com.tapp.utils.Toast;
import com.tapp.utils.Utils;

public class FriendListFragment extends BaseFragment implements RequestListener {

	private static String TAG = FriendListFragment.class.getName();

	private View view = null;
	private SearchView mSearchView = null;
	private ListView listView = null;

	private NetworManager networManager = null;
	private int genresRequestId = -1;

	private ArrayList<com.tapp.data.ContactData> list = null;
	private FriendListAdapter adapter = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
		
		networManager = NetworManager.getInstance();
	}

	@Override
	public void onResume() {
		super.onResume();
		networManager.addListener(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		networManager.removeListeners(this);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.menu_search, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		mSearchView.setQueryHint(getString(R.string.search_genres));

		final AutoCompleteTextView edtSearch = (AutoCompleteTextView) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

		edtSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (adapter != null) {
					adapter.filter(edtSearch.getText().toString().trim());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		edtSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (!hasFocus) {
					KeyboardUtils.hideKeyboard(edtSearch);
				}
			}
		});
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		int id = item.getItemId();
		if (id == android.R.id.home) {
			KeyboardUtils.hideKeyboard(mSearchView);
			getFragmentManager().popBackStack();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_list, container, false);

		listView = (ListView) view.findViewById(R.id.listView);
		TextView txtEmptyView = (TextView) view.findViewById(R.id.txtEmptyView);
		listView.setEmptyView(txtEmptyView);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

				Intent intent = new Intent(getActivity(), ViewProfileActivity.class);
				startActivity(intent);
			}
		});

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

			}
		}).start();

	}

	private void getContactList1() {

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

	private void getContactList() {

		try {

			list = new ArrayList<ContactData>();

			if (!ConstantData.DB.isContactExistInDB()) {

				String numbers = insertContactsToDB();

				if (!numbers.equals("")) {
					syncPhoneNumbers(numbers);
				}

			} else {

				list = ConstantData.DB.getContactList();

				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {

						adapter = new FriendListAdapter(getActivity(), list);
						listView.setAdapter(adapter);
						setContentShown(true);
					}
				});
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

	private String insertContactsToDB() {

		String phoneNumbers = "";

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
							phoneNumbers += "," + phoneNo;

						}
						pCur.close();

					}
				}
				ConstantData.DB.endTransaction();

				if (phoneNumbers.startsWith(",")) {
					phoneNumbers = phoneNumbers.replaceFirst(",", "");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in getContactList", e.toString());
		} finally {
			if (ConstantData.DB.isTransactionRunning()) {
				ConstantData.DB.endTransaction();
			}
		}

		return phoneNumbers;
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

	private void syncPhoneNumbers(String phoneNumbers) {

		networManager.isProgressVisible(false);
		genresRequestId = networManager.addRequest(new HashMap<String, String>(), RequestMethod.GET, getActivity(), TappRequestBuilder.WS_GENRES);
	}

	@Override
	public void onSuccess(int id, String response) {

		try {
			if (!Utils.isEmpty(response)) {

				if (id == genresRequestId) {

					JSONArray jArray = new JSONArray(getString(R.string.temp_json));

					ConstantData.DB.beginTransaction();

					for (int i = 0; i < jArray.length(); i++) {

						JSONObject jObj = jArray.getJSONObject(i);

						ConstantData.DB.updateContactByPhoneNo(jObj.getString("phoneNo"), jObj.getInt("contactTypeFlag"), jObj.getString("photoUrl"), jObj.getString("status"));
					}

					ConstantData.DB.endTransaction();

					list = ConstantData.DB.getContactList();

					adapter = new FriendListAdapter(getActivity(), list);
					listView.setAdapter(adapter);

					// listGenresData = new ArrayList<IdNameData>();
					// JSONArray jArrayResult = new JSONArray(response);
					//
					// for (int i = 0; i < jArrayResult.length(); i++) {
					//
					// JSONObject jObj = jArrayResult.getJSONObject(i);
					// listGenresData.add(new IdNameData(jObj.getInt("id"),
					// jObj.getString("genre")));
					// }
					//
					// listView.setAdapter(new IdNameListAdapter(getActivity(),
					// listGenresData));

				}

			} else {

				// Toast.displayText(getActivity(),
				// R.string.invalid_server_response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error in onSuccess : " + e.toString());
		} finally {
			setContentShown(true);
		}
	}
	@Override
	public void onError(int id, String message) {
		Toast.displayError(getActivity(), message);
		setContentShown(true);
	}
}
