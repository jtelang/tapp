package com.tapp;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.tapp.adapters.IdNameListAdapter;
import com.tapp.data.IdNameData;
import com.tapp.network.NetworManager;
import com.tapp.network.RequestListener;
import com.tapp.network.RequestMethod;
import com.tapp.request.TappRequestBuilder;
import com.tapp.utils.KeyboardUtils;
import com.tapp.utils.Log;
import com.tapp.utils.Toast;
import com.tapp.utils.Utils;

public class SongListActivity extends ActionBarActivity implements RequestListener {

	private static String TAG = SongListActivity.class.getName();

	private SearchView mSearchView = null;
	private ListView listView = null;

	private NetworManager networManager = null;
	private int albumRequestId = -1;

	private ArrayList<IdNameData> listSongData = null;
	private int artistId = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_list);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		artistId = getIntent().getIntExtra("id", 0);

		listView = (ListView) findViewById(R.id.listView);
		TextView txtEmptyView = (TextView) findViewById(R.id.txtEmptyView);
		listView.setEmptyView(txtEmptyView);

		networManager = NetworManager.getInstance();

		getActionBar().setTitle(getIntent().getStringExtra("title"));

		listSongData = new ArrayList<IdNameData>();
		downloadSongs();

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
		super.onCreateOptionsMenu(menu);
		menu.clear();
		getMenuInflater().inflate(R.menu.menu_search, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		mSearchView.setQueryHint(getString(R.string.search_song));

		final AutoCompleteTextView edtSearch = (AutoCompleteTextView) mSearchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);

		edtSearch.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_SEARCH) {

					KeyboardUtils.hideKeyboard(edtSearch);

					if (mSearchView.getQuery().toString().equals("")) {
						// Toast.makeText(getActivity(),
						// getString(R.string.search_bill_no),
						// Toast.LENGTH_LONG).show();
						return false;
					}

					return true;
				}
				return false;
			}
		});
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void downloadSongs() {

		networManager.isProgressVisible(true);
		albumRequestId = networManager.addRequest(new HashMap<String, String>(), RequestMethod.GET, this, String.format(TappRequestBuilder.WS_SONGS_PARAM, artistId));
	}

	@Override
	public void onSuccess(int id, String response) {

		try {
			if (!Utils.isEmpty(response)) {

				if (id == albumRequestId) {

					listSongData = new ArrayList<IdNameData>();

					JSONObject jObj = new JSONObject(response);
					listSongData.add(new IdNameData(jObj.getInt("id"), jObj.getString("title")));

					// JSONArray jArrayResult = new JSONArray(response);
					// for (int i = 0; i < jArrayResult.length(); i++) {
					//
					// JSONObject jObj = jArrayResult.getJSONObject(i);
					// listSongData.add(new IdNameData(jObj.getInt("id"),
					// jObj.getString("title")));
					// }

					listView.setAdapter(new IdNameListAdapter(this, listSongData));

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
