package com.tapp.fragments;

import java.util.ArrayList;
import java.util.Comparator;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.tapp.R;
import com.tapp.ViewProfileActivity;
import com.tapp.adapters.FollowersListAdapter;
import com.tapp.adapters.FriendListAdapter;
import com.tapp.base.BaseFragment;
import com.tapp.data.ConstantData;
import com.tapp.data.ContactData;

public class FollowerListFragment extends BaseFragment {

	private View view = null;
	private ListView listView = null;

	private ArrayList<com.tapp.data.ContactData> list = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_list, null);

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

				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {

						listView.setAdapter(new FollowersListAdapter(getActivity(), list));
						setContentShown(true);
					}
				});
			}
		}).start();

	}

	private void getContactList() {

		try {

			list = new ArrayList<ContactData>();
			ContactData data = new ContactData();
			data.setName("Jack Kalish");
			data.setStatus("Available");
			list.add(data);
			data = new ContactData();
			data.setName("Leo Thomson");
			data.setStatus("Hey there");
			list.add(data);
			data = new ContactData();
			data.setName("John Pitt");
			data.setStatus("Available");
			list.add(data);
			data = new ContactData();
			data.setName("Sima Roy");
			data.setStatus("In a meeting");
			list.add(data);
			data = new ContactData();
			data.setName("Jenny Loyer");
			data.setStatus("Available");
			list.add(data);

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in getContactList", e.toString());
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
