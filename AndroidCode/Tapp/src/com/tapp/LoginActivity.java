package com.tapp;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneNumberUtils;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tapp.data.ConstantData;
import com.tapp.network.NetworManager;
import com.tapp.network.RequestListener;
import com.tapp.network.RequestMethod;
import com.tapp.request.PARAMS;
import com.tapp.request.TappRequestBuilder;
import com.tapp.utils.KeyboardUtils;
import com.tapp.utils.Log;
import com.tapp.utils.PrefManager;
import com.tapp.utils.Toast;
import com.tapp.utils.Utils;

public class LoginActivity extends ActionBarActivity implements OnClickListener, RequestListener {

	private static String TAG = LoginActivity.class.getName();

	private Button btnGo = null;
	private EditText edtPhone = null;
	private Spinner spnCountryName = null;
	private TextView txtCountryCode = null;

	private NetworManager networManager = null;
	PrefManager prefManager = null;
	private int registerUserRequestId = -1;

	private String[] arrayCountryCode = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		spnCountryName = (Spinner) findViewById(R.id.spnCountry);
		edtPhone = (EditText) findViewById(R.id.edtPhone);
		btnGo = (Button) findViewById(R.id.btnGo);
		txtCountryCode = (TextView) findViewById(R.id.txtCountryCode);

		btnGo.setOnClickListener(this);

		networManager = NetworManager.getInstance();
		prefManager = PrefManager.getInstance(this);

		arrayCountryCode = getResources().getStringArray(R.array.country_code);

		spnCountryName.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				txtCountryCode.setText(arrayCountryCode[spnCountryName.getSelectedItemPosition()]);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
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

	private void registerUser() {

		String phone = txtCountryCode.getText().toString().trim() + PhoneNumberUtils.stripSeparators(edtPhone.getText().toString().trim());

		networManager.isProgressVisible(true);
		registerUserRequestId = networManager.addRequest(TappRequestBuilder.getRegisterUserRequest(phone), RequestMethod.POST, LoginActivity.this, TappRequestBuilder.WS_REGISTER_USER);
	}

	@Override
	public void onSuccess(int id, String response) {

		try {
			if (!Utils.isEmpty(response)) {

				if (id == registerUserRequestId) {

					JSONObject jObjResponse = new JSONObject(response);

					if (jObjResponse.has("id") && !Utils.isEmpty(jObjResponse.getString("id"))) {

						ConstantData.USER_ID = jObjResponse.getString("id");
						ConstantData.PHONE_NO = jObjResponse.getString("phone");

						Editor editor = prefManager.getPrefs().edit();
						editor.putString(PARAMS.KEY_USER_ID, ConstantData.USER_ID);
						editor.putString(PARAMS.KEY_PHONE_NO, ConstantData.PHONE_NO);
						editor.commit();

						Intent intent = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(intent);
						finish();

					} else {
						Toast.displayText(this, R.string.registration_failed);
					}
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

		if (view.equals(btnGo)) {

			if (edtPhone.getText().toString().trim().equals("")) {

				Toast.displayText(LoginActivity.this.getBaseContext(), R.string.enter_phone);

			} else {

				KeyboardUtils.hideKeyboard(edtPhone);

				String phone = txtCountryCode.getText().toString().trim() + edtPhone.getText().toString().trim();

				phone = PhoneNumberUtils.formatNumber(phone);

				phone = Html.fromHtml(String.format(getString(R.string.is_your_number), phone)).toString();

				AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
				builder.setTitle(getString(R.string.number_confirmation));
				builder.setMessage(phone);
				builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						registerUser();
					}
				});
				builder.setNegativeButton(getString(R.string.edit), null);
				AlertDialog dialog = builder.show();
				TextView messageText = (TextView) dialog.findViewById(android.R.id.message);
				messageText.setGravity(Gravity.CENTER);
				dialog.show();
			}
		}
	}
}
