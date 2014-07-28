package com.tapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ViewProfileActivity extends ActionBarActivity implements OnClickListener {

	private Button btnViewAllFollowers = null;
	private TextView txtFullName = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_view_profile);

		btnViewAllFollowers = (Button) findViewById(R.id.btnViewAllFollowers);

		btnViewAllFollowers.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_view_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View view) {

		if (view.equals(btnViewAllFollowers)) {

//			Intent intent = new Intent(ViewProfileActivity.this, BuyActivity.class);
//			startActivity(intent);
		}
	}
}
