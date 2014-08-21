package com.tapp.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tapp.R;
import com.tapp.SongListActivity;
import com.tapp.data.IdNameData;
import com.tapp.request.PARAMS;

public class SongListAdapter extends BaseAdapter {

	private Activity mActivity = null;
	private ArrayList<IdNameData> mArrayList = null;
	private LayoutInflater mInflater = null;

	public SongListAdapter(Activity activity, ArrayList<IdNameData> list) {

		this.mActivity = activity;
		this.mArrayList = list;

		mInflater = LayoutInflater.from(activity);
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

				((SongListActivity) mActivity).buySongRequest("" + data.getId(), PARAMS.MEDIA_TYPE_SONG, "Amazon");
			}
		});

		return convertView;
	}
	private static class ViewHolder {

		TextView txtName;
		TextView txtBuy;
	}
}