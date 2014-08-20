package com.tapp.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
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
import com.tapp.base.BaseFragment;
import com.tapp.data.ConstantData;
import com.tapp.data.ContactData;
import com.tapp.network.NetworManager;
import com.tapp.network.RequestListener;
import com.tapp.network.RequestMethod;
import com.tapp.request.TappRequestBuilder;
import com.tapp.utils.Toast;
import com.tapp.utils.Utils;

public class FollowerListFragment extends BaseFragment implements RequestListener {

	private static String TAG = FollowerListFragment.class.getName();

	private View view = null;
	private ListView listView = null;

	private NetworManager networManager = null;
	private int genresRequestId = -1;

	private ArrayList<com.tapp.data.ContactData> list = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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

	private void getMyFollowersList() {

		networManager.isProgressVisible(false);
		genresRequestId = networManager.addRequest(new HashMap<String, String>(), RequestMethod.GET, getActivity(), String.format(TappRequestBuilder.WS_MY_FOLLOWERS, ConstantData.USER_ID));
	}

	@Override
	public void onSuccess(int id, String response) {

		try {
			if (!Utils.isEmpty(response)) {

				if (id == genresRequestId) {
					//
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
