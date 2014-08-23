package com.tapp.adapters;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tapp.R;
import com.tapp.data.IdNameData;
import com.tapp.request.PARAMS;

public class GameListAdapter extends BaseAdapter {

	private ArrayList<IdNameData> mArrayList = null;
	private ArrayList<IdNameData> mArrayListTemp = null;
	private LayoutInflater mInflater = null;
	private OnBuyClickListner listner = null;

	public GameListAdapter(Activity activity, ArrayList<IdNameData> list, OnBuyClickListner listner) {

		this.mArrayList = list;
		this.listner = listner;

		mInflater = LayoutInflater.from(activity);

		mArrayListTemp = new ArrayList<IdNameData>();
		mArrayListTemp.addAll(list);
	}

	@Override
	public int getCount() {
		return mArrayList.size();
	}

	@Override
	public Object getItem(int position) {
		return mArrayList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return mArrayList.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder mHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.row_song_list, parent, false);
			mHolder = new ViewHolder();

			mHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			mHolder.txtBuy = (TextView) convertView.findViewById(R.id.txtBuy);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		final IdNameData data = mArrayList.get(position);

		if (data != null) {

			mHolder.txtName.setText(data.getName());
		}

		mHolder.txtBuy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (listner != null) {
					listner.onBuyClicked("" + data.getId(), PARAMS.MEDIA_TYPE_SONG, "iTunes");
				}
			}
		});

		return convertView;
	}

	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		mArrayList.clear();

		if (charText.length() == 0) {
			mArrayList.addAll(mArrayListTemp);
		} else {
			for (IdNameData data : mArrayListTemp) {
				if (data.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
					mArrayList.add(data);
				}
			}
		}
		notifyDataSetChanged();
	}

	private static class ViewHolder {

		TextView txtName;
		TextView txtBuy;
	}

	public interface OnBuyClickListner {

		public void onBuyClicked(String mediaId, String mediaType, String store);
	}
}