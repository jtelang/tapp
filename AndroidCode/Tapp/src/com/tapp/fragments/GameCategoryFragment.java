package com.tapp.fragments;

import java.util.ArrayList;

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
import com.tapp.utils.Log;

public class GameCategoryFragment extends BaseFragment {

	private static String TAG = GameCategoryFragment.class.getName();

	private View view = null;
	private GridView gridView = null;

	private ArrayList<String> listCategory = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_game_category, null);

		gridView = (GridView) view.findViewById(R.id.gridView);
		TextView txtEmptyView = (TextView) view.findViewById(R.id.txtEmptyView);
		gridView.setEmptyView(txtEmptyView);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				String title = listCategory.get(position);
				if (title.contains("(")) {
					title = title.substring(0, title.lastIndexOf("(")).trim() + " Games";
				}
				Intent intent = new Intent(getActivity(), GameTabActivity.class);
				intent.putExtra("title", title);
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

		new Thread(new Runnable() {
			@Override
			public void run() {

				getCategoryList();

				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {

						gridView.setAdapter(new GameCatGridAdapter(getActivity(), listCategory));
						setContentShown(true);
					}
				});
			}
		}).start();

	}

	private void getCategoryList() {

		try {

			listCategory = new ArrayList<String>();

			listCategory.add("Action (408)");
			listCategory.add("Adventure (48)");
			listCategory.add("Puzzles (96)");
			listCategory.add("Sports (19)");
			listCategory.add("Racing (35)");
			listCategory.add("Driving (26)");
			listCategory.add("Games for girls (271)");
			listCategory.add("Bubble-Shooter (15)");
			listCategory.add("Board Game (53)");
			listCategory.add("Painting (15)");

		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error in getCategoryList : " + e.toString());
		}
	}
}
