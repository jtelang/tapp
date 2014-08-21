package com.tapp.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.tapp.R;
import com.tapp.adapters.GameListAdapter;
import com.tapp.adapters.GameListAdapter.OnBuyClickListner;
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

public class GamesTabCatFragment extends BaseFragment implements RequestListener {

	private static String TAG = GamesTabCatFragment.class.getName();

	private View view = null;
	private SearchView mSearchView = null;
	private ListView listView = null;

	private NetworManager networManager = null;
	private int downloadGameRequestId = -1, buyGameRequestId;

	private ArrayList<IdNameData> listGameData = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);

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

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setContentView(view);
		setContentShown(false);

		listGameData = new ArrayList<IdNameData>();
		downloadGames();

		// new Thread(new Runnable() {
		// @Override
		// public void run() {
		//
		// getSongList();
		//
		// getActivity().runOnUiThread(new Runnable() {
		// @Override
		// public void run() {
		//
		// listView.setAdapter(new SongListAdapterOld(getActivity(),
		// listGameData));
		// setContentShown(true);
		// }
		// });
		// }
		// }).start();
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.menu_search, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		mSearchView.setQueryHint(getString(R.string.search_games));

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
	}

	// private void getSongList() {
	//
	// try {
	//
	// listGameData = new ArrayList<SongData>();
	//
	// listGameData.add(new SongData("Superfight", "Action", 0));
	// listGameData.add(new SongData("Grim Jogger", "Action", 1));
	// listGameData.add(new SongData("Kungfu", "Action", 0));
	// listGameData.add(new SongData("Ninja II", "Action", 1));
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// Log.e(TAG, "Error in getSongList : " + e.toString());
	// }
	// }

	OnBuyClickListner buyClickListner = new OnBuyClickListner() {
		@Override
		public void onBuyClicked(String mediaId, String mediaType, String store) {

			buySongRequest(mediaId, mediaType, store);
		}
	};

	private void downloadGames() {

		networManager.isProgressVisible(false);
		downloadGameRequestId = networManager.addRequest(new HashMap<String, String>(), RequestMethod.GET, getActivity(), TappRequestBuilder.WS_ALBUMS);
	}

	private void buySongRequest(String mediaId, String mediaType, String store) {

		networManager.isProgressVisible(true);
		buyGameRequestId = networManager.addRequest(TappRequestBuilder.getBuyMediaRequest(mediaId, mediaType, store), RequestMethod.POST, getActivity(), TappRequestBuilder.WS_BUY_MEDIA);
	}

	@Override
	public void onSuccess(int id, String response) {

		try {
			if (!Utils.isEmpty(response)) {

				if (id == downloadGameRequestId) {

					listGameData = new ArrayList<IdNameData>();
					JSONArray jArrayResult = new JSONArray(response);

					for (int i = 0; i < jArrayResult.length(); i++) {

						JSONObject jObj = jArrayResult.getJSONObject(i);
						listGameData.add(new IdNameData(jObj.getInt("id"), jObj.getString("title")));
					}

					listView.setAdapter(new GameListAdapter(getActivity(), listGameData, buyClickListner));

				} else if (id == buyGameRequestId) {

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
