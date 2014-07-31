package com.tapp.fragments;

import java.util.ArrayList;

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
import com.tapp.adapters.AlbumListAdapter;
import com.tapp.base.BaseFragment;
import com.tapp.data.AlbumData;
import com.tapp.utils.KeyboardUtils;
import com.tapp.utils.Log;

public class GenresFragment extends BaseFragment {

	private static String TAG = GenresFragment.class.getName();

	private View view = null;
	private SearchView mSearchView = null;
	private ListView listView = null;

	private ArrayList<AlbumData> listGenresData = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_genres, null);

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

		new Thread(new Runnable() {
			@Override
			public void run() {

				getGenresList();

				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {

						listView.setAdapter(new AlbumListAdapter(getActivity(), listGenresData));
						setContentShown(true);
					}
				});
			}
		}).start();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		menu.clear();
		inflater.inflate(R.menu.menu_search, menu);
		MenuItem searchItem = menu.findItem(R.id.action_search);
		mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
		mSearchView.setQueryHint(getString(R.string.search_genres));

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

	private void getGenresList() {

		try {

			listGenresData = new ArrayList<AlbumData>();

			listGenresData.add(new AlbumData("For Emma, Forever Ago", "Bon lver", ""));
			listGenresData.add(new AlbumData("Mer De Noms", "A Perfect Circle", ""));
			listGenresData.add(new AlbumData("Narrow Stairs", "Death Cab For Cutie", ""));

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error in displayAlbumList : " + e.toString());
		}
	}
}
