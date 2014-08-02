package com.tapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.tapp.MusicTabActivity;
import com.tapp.R;
import com.tapp.base.BaseFragment;

public class MusicCategoryFragment extends BaseFragment implements OnClickListener {

	private static String TAG = MusicCategoryFragment.class.getName();

	private View view = null;
	private Button btnGenres = null, btnArtist = null, btnAlbum = null, btnSongs = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_music_category, null);

		btnGenres = (Button) view.findViewById(R.id.btnGenres);
		btnArtist = (Button) view.findViewById(R.id.btnArtist);
		btnAlbum = (Button) view.findViewById(R.id.btnAlbum);
		btnSongs = (Button) view.findViewById(R.id.btnSongs);

		btnGenres.setOnClickListener(this);
		btnArtist.setOnClickListener(this);
		btnAlbum.setOnClickListener(this);
		btnSongs.setOnClickListener(this);

		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		setContentView(view);
		setContentShown(true);

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

			case R.id.btnGenres :
				Intent intent = new Intent(getActivity(), MusicTabActivity.class);
				intent.putExtra("tab", "genres");
				startActivity(intent);
				break;
			case R.id.btnArtist :
				intent = new Intent(getActivity(), MusicTabActivity.class);
				intent.putExtra("tab", "artist");
				startActivity(intent);
				break;
			case R.id.btnAlbum :
				intent = new Intent(getActivity(), MusicTabActivity.class);
				intent.putExtra("tab", "album");
				startActivity(intent);
				break;
			case R.id.btnSongs :
				intent = new Intent(getActivity(), MusicTabActivity.class);
				intent.putExtra("tab", "songs");
				startActivity(intent);
				break;
		}
	}
}
