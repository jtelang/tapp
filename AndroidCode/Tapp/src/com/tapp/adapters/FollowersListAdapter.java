package com.tapp.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tapp.R;
import com.tapp.data.ContactData;
import com.tapp.utils.Utils;

public class FollowersListAdapter extends BaseAdapter {

	private Activity activity = null;
	private ArrayList<ContactData> list = null;
	private LayoutInflater mInflater = null;
	private ImageLoader imageLoader = null;
	private DisplayImageOptions options = null;

	public FollowersListAdapter(Activity activity, ArrayList<ContactData> list) {
		this.activity = activity;
		this.list = list;

		mInflater = LayoutInflater.from(activity);

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher).showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true).cacheOnDisk(true).build();
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
			convertView = mInflater.inflate(R.layout.row_followers_list, parent, false);
			mHolder = new ViewHolder();

			mHolder.imvPhoto = (ImageView) convertView.findViewById(R.id.imvPhoto);
			mHolder.txtName = (TextView) convertView.findViewById(R.id.txtName);
			mHolder.txtStatus = (TextView) convertView.findViewById(R.id.txtStatus);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		final ContactData data = list.get(position);

		if (data != null) {

			mHolder.txtName.setText(data.getName());
			mHolder.txtStatus.setText(data.getBio());

			if (!Utils.isEmpty(data.getPhoto())) {
				imageLoader.displayImage(data.getPhoto(), mHolder.imvPhoto, options);
			} else {
				mHolder.imvPhoto.setImageResource(R.drawable.ic_launcher);
			}
		}

		return convertView;
	}
	private static class ViewHolder {

		TextView txtName;
		TextView txtStatus;
		ImageView imvPhoto;
	}
}