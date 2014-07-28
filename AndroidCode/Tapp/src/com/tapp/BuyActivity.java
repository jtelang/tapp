package com.tapp;

import java.util.Calendar;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class BuyActivity extends ActionBarActivity {

	private Spinner spnMonth = null, spnYear = null;

	private String[] arrayMonth = null, arrayYear = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_buy);

		spnMonth = (Spinner) findViewById(R.id.spnMonth);
		spnYear = (Spinner) findViewById(R.id.spnYear);

		arrayMonth = getResources().getStringArray(R.array.month);
		arrayYear = new String[20];
		Calendar cal = Calendar.getInstance();

		for (int i = 0; i < 20; i++) {
			arrayYear[i] = String.valueOf(cal.get(Calendar.YEAR) + i);
		}

		spnMonth.setAdapter(new ArrayAdapter<String>(BuyActivity.this, R.layout.row_spinner, arrayMonth));
		spnYear.setAdapter(new ArrayAdapter<String>(BuyActivity.this, R.layout.row_spinner, arrayYear));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_buy, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
