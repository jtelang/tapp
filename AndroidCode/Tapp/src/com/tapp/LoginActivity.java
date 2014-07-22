package com.tapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.tapp.utils.DialogHelper;
import com.tapp.utils.KeyboardUtils;
import com.tapp.utils.Toast;

public class LoginActivity extends ActionBarActivity implements OnClickListener {

	private Button btnGo = null;
	private EditText edtPhone = null;
	private Spinner spnCountryName = null;
	private TextView txtCountryCode = null;

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
	public void onClick(View view) {

		if (view.equals(btnGo)) {

			if (edtPhone.getText().toString().trim().equals("")) {

				Toast.displayText(LoginActivity.this.getBaseContext(), R.string.enter_phone);

			} else {

				KeyboardUtils.hideKeyboard(edtPhone);

				String phone = txtCountryCode.getText().toString().trim() + edtPhone.getText().toString().trim();

				phone = PhoneNumberUtils.formatNumber(phone);

				// phone = Html.fromHtml("<b>" + phone + "</b>").toString();

				DialogHelper.popupMessage(LoginActivity.this, getString(R.string.number_confirmation), phone, getString(R.string.yes), getString(R.string.edit), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						Intent intent = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					}
				}, null, true);

			}
		}
	}
}
