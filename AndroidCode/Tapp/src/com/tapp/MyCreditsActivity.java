package com.tapp;

import java.util.ArrayList;

import com.tapp.adapters.MyCreditListAdapter;
import com.tapp.data.MyCreditData;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.ListView;

public class MyCreditsActivity extends ActionBarActivity {

	private ListView listView = null;

	private ArrayList<MyCreditData> list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_mycredits);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		listView = (ListView) findViewById(R.id.listView);

		list = new ArrayList<MyCreditData>();

		for (int i = 0; i < 10; i++) {

			list.add(new MyCreditData("" + (15654458 + i), "" + 10));
		}

		listView.setAdapter(new MyCreditListAdapter(this, list));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
