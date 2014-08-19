package com.tapp.fragments;

import java.util.ArrayList;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.tapp.FollowersListActivity;
import com.tapp.R;
import com.tapp.base.BaseFragment;
import com.tapp.data.ConstantData;
import com.tapp.data.MyCreditData;
import com.tapp.network.NetworManager;
import com.tapp.network.RequestListener;
import com.tapp.network.RequestMethod;
import com.tapp.request.TappRequestBuilder;
import com.tapp.utils.Log;
import com.tapp.utils.Toast;
import com.tapp.utils.Utils;

public class MyProfileFragment extends BaseFragment implements OnClickListener, RequestListener {

	private static String TAG = MyProfileFragment.class.getName();

	private View view = null;
	private Button btnViewAllFollowers = null;

	private NetworManager networManager = null;
	private int profileRequestId = -1;

	private ArrayList<MyCreditData> list = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		networManager = NetworManager.getInstance();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_view_profile, null);

		btnViewAllFollowers = (Button) view.findViewById(R.id.btnViewAllFollowers);

		btnViewAllFollowers.setOnClickListener(this);

		return super.onCreateView(inflater, container, savedInstanceState);
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
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setContentView(view);
		setContentShown(false);

		downloadUserProfile();
	}

	private void downloadUserProfile() {

		networManager.isProgressVisible(false);
		profileRequestId = networManager.addRequest(TappRequestBuilder.getRegisterUserRequest(ConstantData.PHONE_NO), RequestMethod.POST, getActivity(), TappRequestBuilder.WS_GET_PROFILE);
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
		} finally {
			setContentShown(true);
		}
	}

	@Override
	public void onError(int id, String message) {
		Toast.displayError(getActivity(), message);
		setContentShown(true);
	}

	@Override
	public void onClick(View view) {

		if (view.equals(btnViewAllFollowers)) {

			Intent intent = new Intent(getActivity(), FollowersListActivity.class);
			startActivity(intent);
		}
	}
}
