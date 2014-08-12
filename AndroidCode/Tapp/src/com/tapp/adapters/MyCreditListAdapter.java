package com.tapp.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tapp.R;
import com.tapp.data.MyCreditData;

public class MyCreditListAdapter extends BaseAdapter {

	private ArrayList<MyCreditData> list = null;
	private LayoutInflater mInflater = null;

	public MyCreditListAdapter(Activity activity, ArrayList<MyCreditData> list) {
		this.list = list;

		mInflater = LayoutInflater.from(activity);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return list.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_my_credits, parent, false);
			mHolder = new ViewHolder();

			mHolder.txtHeader = (TextView) convertView.findViewById(R.id.txtHeader);
			mHolder.txtPlace = (TextView) convertView.findViewById(R.id.txtPlace);
			mHolder.txtDate = (TextView) convertView.findViewById(R.id.txtDate);
			mHolder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
			mHolder.txtYouEarned = (TextView) convertView.findViewById(R.id.txtYouEarned);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		final MyCreditData data = list.get(position);

		if (data != null) {

		}

		return convertView;
	}
	private static class ViewHolder {

		TextView txtHeader;
		TextView txtPlace;
		TextView txtDate;
		TextView txtPrice;
		TextView txtYouEarned;
	}
}