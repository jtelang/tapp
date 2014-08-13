package com.tapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.tapp.data.ConstantData;
import com.tapp.data.DBHelper;
import com.tapp.fragments.FriendListFragment;
import com.tapp.fragments.MyCreditsFragment;
import com.tapp.fragments.MyProfileFragment;
import com.tapp.fragments.NavigationDrawerFragment;
import com.tapp.fragments.OfferListFragment;
import com.tapp.fragments.SelectCategoryFragment;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

		try {
			if (ConstantData.DB == null || !ConstantData.DB.isOpen()) {
				ConstantData.DB = new DBHelper(MainActivity.this);
				ConstantData.DB.open();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in OpenDB", e.toString());
		}
	}

	@Override
	public void onNavigationDrawerItemSelected(int position) {

		Fragment fragment = null;

		switch (position) {

			case 0 :
				fragment = new SelectCategoryFragment();
				break;

			case 1 :
				fragment = new FriendListFragment();
				break;

			case 2 :
				fragment = new OfferListFragment();
				break;

			case 3 :
				fragment = new MyProfileFragment();
				break;

			case 4 :
				fragment = new MyCreditsFragment();
				break;
		}

		clearBackStack();
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();
	}

	private void clearBackStack() {
		try {
			FragmentManager fm = getSupportFragmentManager();
			for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
				fm.popBackStack();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in clearBackStack", e.toString());
		}
	}

	public void setActionBarTitle(String title) {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(title);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {

		if (ConstantData.DB != null && ConstantData.DB.isOpen()) {
			ConstantData.DB.close();
			ConstantData.DB = null;
		}
		super.onDestroy();
	}
}
