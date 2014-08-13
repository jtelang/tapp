package com.tapp.fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.tapp.R;
import com.tapp.adapters.MyCreditListAdapter;
import com.tapp.base.BaseFragment;
import com.tapp.data.MyCreditData;

public class MyCreditsFragment extends BaseFragment {

	private View view = null;
	private ListView listView = null;

	private ArrayList<MyCreditData> list = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_mycredits, null);

		listView = (ListView) view.findViewById(R.id.listView);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setContentView(view);
		setContentShown(false);
		list = new ArrayList<MyCreditData>();

		for (int i = 0; i < 10; i++) {

			list.add(new MyCreditData("" + (15654458 + i), "" + 10));
		}

		listView.setAdapter(new MyCreditListAdapter(getActivity(), list));
		setContentShown(true);
	}

}
