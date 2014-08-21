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
import com.tapp.data.AlbumData;

public class AlbumListAdapterOld extends BaseAdapter {

	private Activity activity = null;
	private ArrayList<AlbumData> list = null;
	private LayoutInflater mInflater = null;
	private ImageLoader imageLoader = null;
	private DisplayImageOptions options = null;

	public AlbumListAdapterOld(Activity activity, ArrayList<AlbumData> list) {
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
			convertView = mInflater.inflate(R.layout.row_album_list, parent, false);
			mHolder = new ViewHolder();

			mHolder.imvPhoto = (ImageView) convertView.findViewById(R.id.imvPhoto);
			mHolder.txtAlbumName = (TextView) convertView.findViewById(R.id.txtAlbumName);
			mHolder.txtSingerName = (TextView) convertView.findViewById(R.id.txtSingerName);

			convertView.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) convertView.getTag();
		}

		final AlbumData data = list.get(position);

		if (data != null) {

			mHolder.txtAlbumName.setText(data.getAlbumName());
			mHolder.txtSingerName.setText(data.getSingerName());

//			if (!Utils.isEmpty(data.getImageUrl())) {
//				imageLoader.displayImage(data.getImageUrl(), mHolder.imvPhoto, options);
//			} else {
//				mHolder.imvPhoto.setImageResource(R.drawable.ic_launcher);
//			}
		}

		return convertView;
	}
	private static class ViewHolder {

		ImageView imvPhoto;
		TextView txtAlbumName;
		TextView txtSingerName;
	}
}