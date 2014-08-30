package com.tapp.fragments;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tapp.FollowersListActivity;
import com.tapp.R;
import com.tapp.base.BaseFragment;
import com.tapp.data.ConstantData;
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
	private TextView txtStatus = null, txtFullName = null, txtEmail = null, txtBirthday = null, txtAge = null, txtWebpage = null, txtCity = null, txtFollowers = null;
	private ImageView imvProfile = null;
	private ToggleButton tglGender = null;

	private NetworManager networManager = null;
	private int profileRequestId = -1;

	private ImageLoader imageLoader = null;
	private DisplayImageOptions options = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		networManager = NetworManager.getInstance();

		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.ic_launcher).showImageForEmptyUri(R.drawable.ic_launcher).showImageOnFail(R.drawable.ic_launcher).cacheInMemory(true).cacheOnDisk(true).build();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.activity_view_profile, container, false);

		imvProfile = (ImageView) view.findViewById(R.id.imvProfile);
		txtStatus = (TextView) view.findViewById(R.id.txtStatus);
		txtFullName = (TextView) view.findViewById(R.id.txtFullName);
		txtEmail = (TextView) view.findViewById(R.id.txtEmail);
		txtBirthday = (TextView) view.findViewById(R.id.txtBirthday);
		tglGender = (ToggleButton) view.findViewById(R.id.tglGender);
		txtAge = (TextView) view.findViewById(R.id.txtAge);
		txtWebpage = (TextView) view.findViewById(R.id.txtWebpage);
		txtCity = (TextView) view.findViewById(R.id.txtCity);
		txtFollowers = (TextView) view.findViewById(R.id.txtFollowers);
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

		downloadUserProfile(ConstantData.USER_ID);
	}

	private void downloadUserProfile(String userId) {

		networManager.isProgressVisible(false);
		profileRequestId = networManager.addRequest(TappRequestBuilder.getRegisterUserRequest(ConstantData.PHONE_NO), RequestMethod.POST, getActivity(), String.format(TappRequestBuilder.WS_GET_PROFILE, userId));
	}

	@Override
	public void onSuccess(int id, String response) {

		try {
			if (!Utils.isEmpty(response)) {

				if (id == profileRequestId) {

					JSONArray jArrayResponse = new JSONArray(response);

					if (jArrayResponse.length() > 0) {
						JSONObject jObj = jArrayResponse.getJSONObject(0);

						if (Utils.isEmpty(jObj.getString("bio"))) {
							txtStatus.setText(jObj.getString("bio"));
						}
						if (Utils.isEmpty(jObj.getString("full_name"))) {
							txtFullName.setText(jObj.getString("full_name"));
						}
						if (Utils.isEmpty(jObj.getString("email"))) {
							txtEmail.setText(jObj.getString("email"));
						}
						if (Utils.isEmpty(jObj.getString("dob"))) {
							txtBirthday.setText(jObj.getString("dob"));
						}
						if (Utils.isEmpty(jObj.getString("age"))) {
							txtAge.setText(jObj.getString("age"));
						}
						
						tglGender.setChecked(Utils.isEmpty(jObj.getString("gender")) || jObj.getString("gender").equalsIgnoreCase("male") ? true : false);

						if (Utils.isEmpty(jObj.getString("homepage"))) {
							txtWebpage.setText(jObj.getString("homepage"));
						}

						if (Utils.isEmpty(jObj.getString("city"))) {
							txtCity.setText(jObj.getString("city"));
						}

						// txtFollowers.setText(jObj.getString(""));

						String photoURL = jObj.getString("photo");
						if (!Utils.isEmpty(photoURL)) {
							imageLoader.displayImage(photoURL, imvProfile, options);
						} else {
							imvProfile.setImageResource(R.drawable.ic_launcher);
						}
					}
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

			if (Utils.isEmpty(txtFollowers.getText().toString().trim()) || txtFollowers.getText().equals("0")) {
				Toast.displayText(getActivity(), R.string.no_followers_available);
			} else {
				Intent intent = new Intent(getActivity(), FollowersListActivity.class);
				startActivity(intent);
			}
		}
	}
}
