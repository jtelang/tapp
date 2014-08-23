package com.tapp.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.TextView;

import com.tapp.GameTabActivity;
import com.tapp.R;
import com.tapp.adapters.GameCatGridAdapter;
import com.tapp.base.BaseFragment;
import com.tapp.data.IdNameData;
import com.tapp.network.NetworManager;
import com.tapp.network.RequestListener;
import com.tapp.network.RequestMethod;
import com.tapp.request.TappRequestBuilder;
import com.tapp.utils.Log;
import com.tapp.utils.Toast;
import com.tapp.utils.Utils;

public class GameCategoryFragment extends BaseFragment implements RequestListener {

	private static String TAG = GameCategoryFragment.class.getName();

	private View view = null;
	private GridView gridView = null;

	private NetworManager networManager = null;
	private int gameCateRequestId = -1;

	private ArrayList<IdNameData> listCategory = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		networManager = NetworManager.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_game_category, container, false);

		gridView = (GridView) view.findViewById(R.id.gridView);
		TextView txtEmptyView = (TextView) view.findViewById(R.id.txtEmptyView);
		gridView.setEmptyView(txtEmptyView);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				Intent intent = new Intent(getActivity(), GameTabActivity.class);
				intent.putExtra("title", listCategory.get(position).getName());
				intent.putExtra("categoryId", listCategory.get(position).getId());
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

		listCategory = new ArrayList<IdNameData>();
		downloadGameCategories();
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

	private void downloadGameCategories() {

		networManager.isProgressVisible(false);
		gameCateRequestId = networManager.addRequest(new HashMap<String, String>(), RequestMethod.GET, getActivity(), TappRequestBuilder.WS_GAME_TYPE);
	}

	@Override
	public void onSuccess(int id, String response) {

		try {
			if (!Utils.isEmpty(response)) {

				if (id == gameCateRequestId) {

					listCategory = new ArrayList<IdNameData>();
					JSONArray jArrayResult = new JSONArray(response);

					for (int i = 0; i < jArrayResult.length(); i++) {

						JSONObject jObj = jArrayResult.getJSONObject(i);
						listCategory.add(new IdNameData(jObj.getInt("id"), jObj.getString("gametype")));
					}

					gridView.setAdapter(new GameCatGridAdapter(getActivity(), listCategory));
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
