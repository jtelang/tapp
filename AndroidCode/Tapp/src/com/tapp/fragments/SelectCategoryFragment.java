package com.tapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.tapp.CategoryActivity;
import com.tapp.R;
import com.tapp.base.BaseFragment;

public class SelectCategoryFragment extends BaseFragment implements OnClickListener {

	private View view = null;
	private Button btnMusic = null, btnGames = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_select_category, null);

		btnMusic = (Button) view.findViewById(R.id.btnMusic);
		btnGames = (Button) view.findViewById(R.id.btnGames);

		btnMusic.setOnClickListener(this);
		btnGames.setOnClickListener(this);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setContentView(view);
		setContentShown(true);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

			case R.id.btnMusic :

				Intent intent = new Intent(getActivity(), CategoryActivity.class);
				intent.putExtra("category", "music");
				startActivity(intent);
				break;

			case R.id.btnGames :

				intent = new Intent(getActivity(), CategoryActivity.class);
				intent.putExtra("category", "games");
				startActivity(intent);
				break;
		}
	}
}
