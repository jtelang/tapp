package com.tapp.fragments;

import java.util.ArrayList;
import java.util.Comparator;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.tapp.R;
import com.tapp.adapters.OfferListAdapter;
import com.tapp.base.BaseFragment;
import com.tapp.data.ContactData;
import com.tapp.data.OfferData;

public class OfferListFragment extends BaseFragment {

	private View view = null;
	private ListView listView = null;

	private ArrayList<OfferData> list = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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

		new Thread(new Runnable() {
			@Override
			public void run() {

				getOfferList();

				getActivity().runOnUiThread(new Runnable() {
					@Override
					public void run() {

						listView.setAdapter(new OfferListAdapter(getActivity(), list));
						setContentShown(true);
					}
				});
			}
		}).start();

	}

	private void getOfferList() {

		try {

			list = new ArrayList<OfferData>();
			OfferData data = new OfferData();
			data.setTitle("Mer De Noms");
			data.setRecommendedBy("Sandeep");
			data.setReceivedDate("8 Aug 2014");
			data.setExpireDate("8 Sep 2014");
			data.setRetailer("Amazon");
			data.setPrice("1.99");
			list.add(data);

			data = new OfferData();
			data.setTitle("Mer De Noms");
			data.setRecommendedBy("Sandeep");
			data.setReceivedDate("8 Aug 2014");
			data.setExpireDate("8 Sep 2014");
			data.setRetailer("Amazon");
			data.setPrice("1.99");
			list.add(data);

			data = new OfferData();
			data.setTitle("Mer De Noms");
			data.setRecommendedBy("Sandeep");
			data.setReceivedDate("8 Aug 2014");
			data.setExpireDate("8 Sep 2014");
			data.setRetailer("Amazon");
			data.setPrice("1.99");
			list.add(data);

		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Error in getContactList", e.toString());
		}
	}

	/**
	 * Comparator to sort concert's list by name
	 */
	Comparator<ContactData> comparatorByName = new Comparator<ContactData>() {

		@Override
		public int compare(ContactData arg0, ContactData arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}
	};
}
