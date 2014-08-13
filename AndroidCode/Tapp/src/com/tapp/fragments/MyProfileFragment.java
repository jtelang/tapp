package com.tapp.fragments;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.tapp.FollowersListActivity;
import com.tapp.R;
import com.tapp.base.BaseFragment;
import com.tapp.data.MyCreditData;

public class MyProfileFragment extends BaseFragment implements OnClickListener {

	private View view = null;
	private Button btnViewAllFollowers = null;

	private ArrayList<MyCreditData> list = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_view_profile, null);

		btnViewAllFollowers = (Button) view.findViewById(R.id.btnViewAllFollowers);

		btnViewAllFollowers.setOnClickListener(this);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setContentView(view);
		setContentShown(true);
	}

	@Override
	public void onClick(View view) {

		if (view.equals(btnViewAllFollowers)) {

			Intent intent = new Intent(getActivity(), FollowersListActivity.class);
			startActivity(intent);
		}
	}
}
