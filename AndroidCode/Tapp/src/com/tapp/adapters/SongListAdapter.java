package com.tapp.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tapp.BuyActivity;
import com.tapp.R;
import com.tapp.data.SongData;

public class SongListAdapter extends BaseAdapter {

	private Activity activity = null;
	private ArrayList<SongData> list = null;
	private LayoutInflater mInflater = null;

	public SongListAdapter(Activity activity, ArrayList<SongData> list) {
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
			convertView = mInflater.inflate(R.layout.row_song_list, parent, false);
			mHolder = new ViewHolder();

			mHolder.txtSongName = (TextView) convertView.findViewById(R.id.txtSongName);
			mHolder.txtSingerName = (TextView) convertView.findViewById(R.id.txtSingerName);
			mHolder.txtBuyFlag = (TextView) convertView.findViewById(R.id.txtBuyFlag);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		final SongData data = list.get(position);

		if (data != null) {

			mHolder.txtSongName.setText(data.getSongName());
			mHolder.txtSingerName.setText(data.getSingerName());

			if (data.getBuyFlag() == 0) {
				mHolder.txtBuyFlag.setText(R.string.buy);
			} else if (data.getBuyFlag() == 1) {
				mHolder.txtBuyFlag.setText(R.string.recommend);
			}

			mHolder.txtBuyFlag.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if (data.getBuyFlag() == 0) {

						Intent intent = new Intent(activity, BuyActivity.class);
						activity.startActivity(intent);
					}
				}
			});
		}

		return convertView;
	}
	private static class ViewHolder {

		TextView txtSongName;
		TextView txtSingerName;
		TextView txtBuyFlag;
	}
}