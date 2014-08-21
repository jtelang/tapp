package com.tapp.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.tapp.R;
import com.tapp.SongListActivity;
import com.tapp.adapters.AlbumFragmentAdapter;
import com.tapp.adapters.AlbumFragmentAdapter.OnBuyClickListner;
import com.tapp.base.BaseFragment;
import com.tapp.data.IdNameData;
import com.tapp.network.NetworManager;
import com.tapp.network.RequestListener;
import com.tapp.network.RequestMethod;
import com.tapp.request.TappRequestBuilder;
import com.tapp.utils.KeyboardUtils;
import com.tapp.utils.Log;
import com.tapp.utils.Toast;
import com.tapp.utils.Utils;

public class AlbumFragment extends BaseFragment implements RequestListener {

	private static String TAG = AlbumFragment.class.getName();

	private View view = null;
	private SearchView mSearchView = null;
	private ListView listView = null;

	private NetworManager networManager = null;
	private int albumRequestId = -1, buyAlbumRequestId = -1;

	private ArrayList<IdNameData> listAlbumData = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);

		networManager = NetworManager.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_list, container, false);

		listView = (ListView) view.findViewById(R.id.listView);
		TextView txtEmptyView = (TextView) view.findViewById(R.id.txtEmptyView);
		listView.setEmptyView(txtEmptyView);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				Intent intent = new Intent(getActivity(), SongListActivity.class);
				intent.putExtra("id", listAlbumData.get(position).getId());
				intent.putExtra("title", getActivity().getActionBar().getTitle() + " -> " + listAlbumData.get(position).getName());
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

		listAlbumData = new ArrayList<IdNameData>();
		downloadAlbumMusic();

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
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.menu_search, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		mSearchView.setQueryHint(getString(R.string.search_album));

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

		edtSearch.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if (!hasFocus) {
					KeyboardUtils.hideKeyboard(edtSearch);
				}
			}
		});
	}

	OnBuyClickListner buyClickListner = new OnBuyClickListner() {
		@Override
		public void onBuyClicked(String mediaId, String mediaType, String store) {

			buySongRequest(mediaId, mediaType, store);
		}
	};

	private void downloadAlbumMusic() {

		networManager.isProgressVisible(false);
		albumRequestId = networManager.addRequest(new HashMap<String, String>(), RequestMethod.GET, getActivity(), TappRequestBuilder.WS_ALBUMS);
	}

	private void buySongRequest(String mediaId, String mediaType, String store) {

		networManager.isProgressVisible(true);
		buyAlbumRequestId = networManager.addRequest(TappRequestBuilder.getBuyMediaRequest(mediaId, mediaType, store), RequestMethod.POST, getActivity(), TappRequestBuilder.WS_BUY_MEDIA);
	}

	@Override
	public void onSuccess(int id, String response) {

		try {
			if (!Utils.isEmpty(response)) {

				if (id == albumRequestId) {

					listAlbumData = new ArrayList<IdNameData>();
					JSONArray jArrayResult = new JSONArray(response);

					for (int i = 0; i < jArrayResult.length(); i++) {

						JSONObject jObj = jArrayResult.getJSONObject(i);
						listAlbumData.add(new IdNameData(jObj.getInt("id"), jObj.getString("title")));
					}

					listView.setAdapter(new AlbumFragmentAdapter(getActivity(), listAlbumData, buyClickListner));

				} else if (id == buyAlbumRequestId) {

					if (response.toLowerCase(Locale.getDefault()).contains("error")) {
						Toast.displayText(getActivity(), R.string.buy_error);
					} else {
						Toast.displayText(getActivity(), R.string.buy_success);
					}
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
