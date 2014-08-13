package com.tapp;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.tapp.adapters.FollowersListAdapter;
import com.tapp.data.ContactData;

public class FollowersListActivity extends ActionBarActivity {

	private ListView listView = null;

	private ArrayList<com.tapp.data.ContactData> list = null;

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

		new Thread(new Runnable() {
			@Override
			public void run() {

				getContactList();

				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						listView.setAdapter(new FollowersListAdapter(FollowersListActivity.this, list));
					}
				});
			}
		}).start();
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
}
