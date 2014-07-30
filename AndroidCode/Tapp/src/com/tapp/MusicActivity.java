package com.tapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MusicActivity extends ActionBarActivity implements OnClickListener {

	private Button btnGenres = null, btnArtist = null, btnAlbumb = null, btnSongs = null, btnMusic = null, btnGames = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_music);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		btnGenres = (Button) findViewById(R.id.btnGenres);
		btnArtist = (Button) findViewById(R.id.btnArtist);
		btnAlbumb = (Button) findViewById(R.id.btnAlbumb);
		btnSongs = (Button) findViewById(R.id.btnSongs);
		btnMusic = (Button) findViewById(R.id.btnMusic);
		btnGames = (Button) findViewById(R.id.btnGames);

		btnGenres.setOnClickListener(this);
		btnArtist.setOnClickListener(this);
		btnAlbumb.setOnClickListener(this);
		btnSongs.setOnClickListener(this);
		btnMusic.setOnClickListener(this);
		btnGames.setOnClickListener(this);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

			case R.id.btnGenres :
				Intent intent = new Intent(MusicActivity.this, SongTabActivity.class);
				startActivity(intent);
				break;
			case R.id.btnArtist :
				intent = new Intent(MusicActivity.this, SongTabActivity.class);
				startActivity(intent);
				break;
			case R.id.btnAlbumb :
				intent = new Intent(MusicActivity.this, SongTabActivity.class);
				startActivity(intent);
				break;
			case R.id.btnSongs :
				intent = new Intent(MusicActivity.this, SongTabActivity.class);
				startActivity(intent);
				break;
			case R.id.btnMusic :
				break;
			case R.id.btnGames :
				break;
		}
	}
}
