package com.tapp.adapters;

import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tapp.R;
import com.tapp.data.IdNameData;

public class IdNameListAdapter extends BaseAdapter {

	private ArrayList<IdNameData> list = null;
	private ArrayList<IdNameData> listTemp = null;
	private LayoutInflater mInflater = null;

	public IdNameListAdapter(Activity activity, ArrayList<IdNameData> list) {
		this.list = list;
		mInflater = LayoutInflater.from(activity);

		listTemp = new ArrayList<IdNameData>();
		listTemp.addAll(list);
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
			convertView = mInflater.inflate(R.layout.row_id_name_list, parent, false);
			mHolder = new ViewHolder();

			mHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		final IdNameData data = list.get(position);

		if (data != null) {

			mHolder.txtName.setText(data.getName());
		}

		return convertView;
	}

	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		list.clear();

		if (charText.length() == 0) {
			list.addAll(listTemp);
		} else {
			for (IdNameData data : listTemp) {
				if (data.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
					list.add(data);
				}
			}
		}
		notifyDataSetChanged();
	}

	private static class ViewHolder {

		TextView txtName;
	}
}