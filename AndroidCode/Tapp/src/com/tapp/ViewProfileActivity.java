package com.tapp;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.tapp.data.ConstantData;
import com.tapp.network.NetworManager;
import com.tapp.network.RequestListener;
import com.tapp.network.RequestMethod;
import com.tapp.request.TappRequestBuilder;
import com.tapp.utils.Log;
import com.tapp.utils.Toast;
import com.tapp.utils.Utils;

public class ViewProfileActivity extends ActionBarActivity implements OnClickListener, RequestListener {

	private static String TAG = ViewProfileActivity.class.getName();
	
	private Button btnViewAllFollowers = null;

	private NetworManager networManager = null;
	private int profileRequestId = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_view_profile);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		btnViewAllFollowers = (Button) findViewById(R.id.btnViewAllFollowers);

		btnViewAllFollowers.setOnClickListener(this);

		networManager = NetworManager.getInstance();

		registerUser();
	}

	@Override
	public void onResume() {
		super.onResume();
		networManager.addListener(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		networManager.removeListeners(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_view_profile, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		} else if (item.getItemId() == R.id.action_add_me) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void registerUser() {

		networManager.isProgressVisible(true);
		profileRequestId = networManager.addRequest(TappRequestBuilder.getRegisterUserRequest(ConstantData.PHONE_NO), RequestMethod.POST, ViewProfileActivity.this, TappRequestBuilder.WS_GET_PROFILE);
	}

	@Override
	public void onSuccess(int id, String response) {

		try {
			if (!Utils.isEmpty(response)) {

				if (id == profileRequestId) {

					JSONObject jObjResponse = new JSONObject(response);

					// if (Utils.isEmpty(jObjResponse.getString("id"))) {
					//
					// ConstantData.USER_ID = jObjResponse.getString("id");
					// ConstantData.PHONE_NO = jObjResponse.getString("phone");
					//
					// Intent intent = new Intent(LoginActivity.this,
					// MainActivity.class);
					// startActivity(intent);
					// finish();
					//
					// } else {
					// Toast.displayText(this, R.string.registration_failed);
					// }
				}

			} else {

				// Toast.displayText(getActivity(),
				// R.string.invalid_server_response);
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e(TAG, "Error in onSuccess : " + e.toString());
		}
	}

	@Override
	public void onError(int id, String message) {
		Toast.displayError(this, message);
	}

	@Override
	public void onClick(View view) {

		if (view.equals(btnViewAllFollowers)) {

			// Intent intent = new Intent(ViewProfileActivity.this,
			// BuyActivity.class);
			// startActivity(intent);
		}
	}
}
