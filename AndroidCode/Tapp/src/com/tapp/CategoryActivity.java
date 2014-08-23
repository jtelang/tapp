package com.tapp;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tapp.fragments.GameCategoryFragment;
import com.tapp.fragments.MusicCategoryFragment;

public class CategoryActivity extends ActionBarActivity implements OnClickListener {

	private Button btnMusic = null, btnGames = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_category);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		String cat = getIntent().getStringExtra("category");

		if (cat.equalsIgnoreCase("music")) {

			actionBar.setTitle(getString(R.string.music));

			MusicCategoryFragment fragment = new MusicCategoryFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();

		} else if (cat.equalsIgnoreCase("games")) {

			actionBar.setTitle(getString(R.string.games));

			GameCategoryFragment fragment = new GameCategoryFragment();
			getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
		}

		btnMusic = (Button) findViewById(R.id.btnMusic);
		btnGames = (Button) findViewById(R.id.btnGames);

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

			case R.id.btnMusic :

				MusicCategoryFragment fragmentMusicCategoryFragment = new MusicCategoryFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentMusicCategoryFragment).commit();
				break;

			case R.id.btnGames :

				GameCategoryFragment fragmentGameCategoryFragment = new GameCategoryFragment();
				getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, fragmentGameCategoryFragment).commit();
				break;
		}
	}

}
