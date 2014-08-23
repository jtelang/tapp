package com.tapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.tapp.adapters.FollowersListAdapter;
import com.tapp.data.ConstantData;
import com.tapp.data.ContactData;
import com.tapp.network.NetworManager;
import com.tapp.network.RequestListener;
import com.tapp.network.RequestMethod;
import com.tapp.request.TappRequestBuilder;
import com.tapp.utils.Toast;
import com.tapp.utils.Utils;

public class FollowersListActivity extends ActionBarActivity implements RequestListener {

	private static String TAG = FollowersListActivity.class.getName();

	private ListView listView = null;

	private NetworManager networManager = null;
	private int followerListRequestId = -1;

	private ArrayList<ContactData> listFollowers = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_list);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		listView = (ListView) findViewById(R.id.listView);
		TextView txtEmptyView = (TextView) findViewById(R.id.txtEmptyView);
		listView.setEmptyView(txtEmptyView);

		networManager = NetworManager.getInstance();

		downloadFollowerList();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// getMenuInflater().inflate(R.menu.menu_view_profile, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void downloadFollowerList() {

		networManager.isProgressVisible(true);
		followerListRequestId = networManager.addRequest(new HashMap<String, String>(), RequestMethod.GET, this, String.format(TappRequestBuilder.WS_MY_FOLLOWERS, ConstantData.USER_ID));
	}

	@Override
	public void onSuccess(int id, String response) {

		try {
			if (!Utils.isEmpty(response)) {

				if (id == followerListRequestId) {

					listFollowers = new ArrayList<ContactData>();

					JSONArray jArrayResult = new JSONArray(response);

					for (int i = 0; i < jArrayResult.length(); i++) {

						JSONObject jObj = jArrayResult.getJSONObject(i);
						ContactData data = new ContactData();
						data.setName(jObj.getString("full_name"));
						data.setStatus(jObj.getString("bio"));
						data.setPhotoUrl(jObj.getString("photo"));
						listFollowers.add(data);
					}
					listView.setAdapter(new FollowersListAdapter(this, listFollowers));
				}

			} else {

				// Toast.displayText(this, R.string.invalid_server_response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error in onSuccess : " + e.toString());
		}
	}
	@Override
	public void onError(int id, String message) {
		Toast.displayError(this, message);
	}
}
