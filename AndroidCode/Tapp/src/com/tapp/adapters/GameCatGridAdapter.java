package com.tapp.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tapp.R;
import com.tapp.data.IdNameData;

public class GameCatGridAdapter extends BaseAdapter {

	private ArrayList<IdNameData> list = null;
	private LayoutInflater mInflater = null;

	public GameCatGridAdapter(Activity activity, ArrayList<IdNameData> list) {
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
			convertView = mInflater.inflate(R.layout.row_game_category_grid, parent, false);
			mHolder = new ViewHolder();

			mHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		IdNameData data = list.get(position);

		if (data != null) {

			mHolder.txtName.setText(data.getName());
		}

		return convertView;
	}
	private static class ViewHolder {

		TextView txtName;
	}
}