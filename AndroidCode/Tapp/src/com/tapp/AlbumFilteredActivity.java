package com.tapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.tapp.adapters.AlbumFilteredAdapter;
import com.tapp.data.IdNameData;
import com.tapp.network.NetworManager;
import com.tapp.network.RequestListener;
import com.tapp.network.RequestMethod;
import com.tapp.request.TappRequestBuilder;
import com.tapp.utils.KeyboardUtils;
import com.tapp.utils.Log;
import com.tapp.utils.Toast;
import com.tapp.utils.Utils;

public class AlbumFilteredActivity extends ActionBarActivity implements RequestListener {

	private static String TAG = AlbumFilteredActivity.class.getName();

	private SearchView mSearchView = null;
	private ListView listView = null;

	private NetworManager networManager = null;
	private int albumRequestId = -1, buyAlbumRequestId = -1;

	private ArrayList<IdNameData> listAlbumData = null;
	private AlbumFilteredAdapter adapter = null;
	private int genresId = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_list);

		// Set up the action bar.
		final ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		genresId = getIntent().getIntExtra("id", 0);

		listView = (ListView) findViewById(R.id.listView);

		networManager = NetworManager.getInstance();

		getActionBar().setTitle(getIntent().getStringExtra("title"));

		listAlbumData = new ArrayList<IdNameData>();
		downloadAlbumMusic();

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				Intent intent = new Intent(AlbumFilteredActivity.this, SongListActivity.class);
				intent.putExtra("id", listAlbumData.get(position).getId());
				intent.putExtra("title", getActionBar().getTitle() + " > " + listAlbumData.get(position).getName());
				startActivity(intent);
			}
		});

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
		mSearchView.setQueryHint(getString(R.string.search_album));

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

	private void downloadAlbumMusic() {

		networManager.isProgressVisible(true);
		albumRequestId = networManager.addRequest(new HashMap<String, String>(), RequestMethod.GET, this, String.format(TappRequestBuilder.WS_ALBUMS_PARAM, genresId));
	}

	public void buySongRequest(String mediaId, String mediaType, String store) {

		networManager.isProgressVisible(true);
		buyAlbumRequestId = networManager.addRequest(TappRequestBuilder.getBuyMediaRequest(mediaId, mediaType, store), RequestMethod.POST, this, TappRequestBuilder.WS_BUY_MEDIA);
	}

	@Override
	public void onSuccess(int id, String response) {

		listView.setEmptyView(findViewById(R.id.txtEmptyView));

		try {
			if (!Utils.isEmpty(response)) {

				if (id == albumRequestId) {

					listAlbumData = new ArrayList<IdNameData>();

					JSONObject jObj = new JSONObject(response);
					listAlbumData.add(new IdNameData(jObj.getInt("id"), jObj.getString("title")));

					adapter = new AlbumFilteredAdapter(this, listAlbumData);
					listView.setAdapter(adapter);

				} else if (id == buyAlbumRequestId) {

					if (response.toLowerCase(Locale.getDefault()).contains("error")) {
						Toast.displayText(this, R.string.buy_error);
					} else {
						Toast.displayText(this, R.string.buy_success);
					}
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
