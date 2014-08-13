package com.tapp.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.tapp.R;
import com.tapp.data.OfferData;

public class OfferListAdapter extends BaseAdapter {

	private Activity activity = null;
	private ArrayList<OfferData> list = null;
	private LayoutInflater mInflater = null;

	public OfferListAdapter(Activity activity, ArrayList<OfferData> list) {
		this.activity = activity;
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
			convertView = mInflater.inflate(R.layout.row_offer_list, parent, false);
			mHolder = new ViewHolder();

			mHolder.txtTitle = (TextView) convertView.findViewById(R.id.txtTile);
			mHolder.txtRecommendedBy = (TextView) convertView.findViewById(R.id.txtRecommendedBy);
			mHolder.txtReceivedDate = (TextView) convertView.findViewById(R.id.txtReceivedDate);
			mHolder.txtExpireDate = (TextView) convertView.findViewById(R.id.txtExpireDate);
			mHolder.txtRetailer = (TextView) convertView.findViewById(R.id.txtRetailer);
			mHolder.txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		final OfferData data = list.get(position);

		if (data != null) {

			mHolder.txtTitle.setText(data.getTitle());
			mHolder.txtRecommendedBy.setText(activity.getString(R.string.recommended_by) + " " + data.getRecommendedBy());
			mHolder.txtReceivedDate.setText(activity.getString(R.string.offer_received) + " " + data.getReceivedDate());
			mHolder.txtExpireDate.setText(activity.getString(R.string.offer_expire) + " " + data.getExpireDate());
			mHolder.txtRetailer.setText(activity.getString(R.string.retailer) + " " + data.getRetailer());
			mHolder.txtPrice.setText("$" + data.getPrice());
		}

		return convertView;
	}
	private static class ViewHolder {

		TextView txtTitle;
		TextView txtRecommendedBy;
		TextView txtReceivedDate;
		TextView txtExpireDate;
		TextView txtRetailer;
		TextView txtPrice;
	}
}