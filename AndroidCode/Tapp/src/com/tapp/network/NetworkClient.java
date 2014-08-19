package com.tapp.network;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;

import com.tapp.data.ConstantData;
import com.tapp.utils.Log;
import com.tapp.views.MyProgressDialog;

public class NetworkClient extends AsyncTask<HashMap<String, String>, String, HashMap<String, String>> {

	private static final String TAG = "NETWORK Client";
	private RequestListener listener;
	private int requestId;
	private RequestMethod method;
	private String webserviceName;
	private Context context;
	private boolean isProgressVisible = false;
	private MyProgressDialog progressDialog;

	public NetworkClient(int requestId, RequestListener listener, RequestMethod method, String webserviceName, Context context, boolean isProgressVisible) {
		this.listener = listener;
		this.requestId = requestId;
		this.method = method;
		this.webserviceName = webserviceName;
		this.context = context;
		this.isProgressVisible = isProgressVisible;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (isProgressVisible)
			showProgressDialog();
	}

	@Override
	protected HashMap<String, String> doInBackground(HashMap<String, String>... request) {

		HashMap<String, String> response = new HashMap<String, String>();

		try {

			RequestClient client = new RequestClient(ConstantData.SERVER_URL + webserviceName, listener, requestId);
			client.addHeader("Content-Type", "application/json");
			if (request.length > 0) {
				for (Map.Entry<String, String> entry : request[0].entrySet()) {
					client.addParam(entry.getKey(), entry.getValue());
				}
			}
			client.execute(method);
			String result = client.getResponse();
			Log.d(TAG, "Response => " + client.getResponse());

			response.put("status", "1");
			response.put("result", result);

		} catch (Exception e) {
			e.printStackTrace();
			response.put("status", "0");
			response.put("result", e.toString());
		}

		return response;
	}

	@Override
	protected void onPostExecute(HashMap<String, String> response) {
		super.onPostExecute(response);

		dismissProgressDialog();

		if (response != null && response.size() > 0) {
			// Log.d(TAG, "[onPostExecute] status= " + response.get("status"));
			// Log.d(TAG, "[onPostExecute] result= " + response.get("result"));
			// Log.d(TAG, "[onPostExecute] listener= " + listener);

			if (response.get("status").equals("1")) {
				listener.onSuccess(requestId, response.get("result"));
			} else {
				listener.onError(requestId, response.get("result"));
			}
		}
	}

	public void showProgressDialog() {

		progressDialog = new MyProgressDialog(context);
	}

	/**
	 * Dismiss custom progress dialog
	 */
	public void dismissProgressDialog() {

		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
}
