package com.tapp.network;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;

import com.tapp.R;
import com.tapp.utils.ConnectivityTools;
import com.tapp.utils.FormatUtils;
import com.tapp.utils.Log;

public class NetworManager implements RequestListener {

	private static NetworManager instance = null;
	ArrayList<RequestListener> arrRequestlisteners = null;
	private int requestId;
	public boolean isAvailable = false;
	private Context context = null;

	public static NetworManager getInstance() {
		if (instance == null) {
			instance = new NetworManager();
		}
		return instance;
	}

	@SuppressWarnings("unchecked")
	public synchronized int addRequest(HashMap<String, String> request, RequestMethod method, Context context, String webserviceName) {

		try {
			this.context = context;

			requestId = FormatUtils.getInstance().getUniqueId();

			if (ConnectivityTools.isNetworkAvailable(context)) {

				NetworkClient networkClient = new NetworkClient(requestId, this, method, webserviceName, context, isAvailable);
				networkClient.execute(request);

			} else {
				onError(requestId, context.getString(R.string.no_internet));
			}
		} catch (Exception e) {
			onError(requestId, e.toString());
		}

		return requestId;
	}

	private NetworManager() {
		arrRequestlisteners = new ArrayList<RequestListener>();
	}

	public int getRequestId() {
		return requestId;

	}

	public void isProgressVisible(boolean isAvailable) {
		this.isAvailable = isAvailable;

	}

	public void addListener(RequestListener listener) {

		if (!arrRequestlisteners.contains(listener)) {
			arrRequestlisteners.add(listener);
		}

	}

	@Override
	public void onSuccess(int id, String response) {
		Log.d(NetworManager.class.getSimpleName(), "[onSuccess] arrRequestlisteners=" + arrRequestlisteners.size());
		if (arrRequestlisteners != null && arrRequestlisteners.size() > 0) {
			for (RequestListener listener : arrRequestlisteners) {
				if (listener != null)
					listener.onSuccess(id, response);
			}
		}
	}

	@Override
	public void onError(final int id, final String message) {
		if (arrRequestlisteners != null && arrRequestlisteners.size() > 0) {
			for (final RequestListener listener : arrRequestlisteners) {
				if (listener != null) {

					if (message == null || message.contains("SocketException") || message.contains("UnknownHostException") || message.contains("SocketTimeoutException")) {

						listener.onError(id, context.getString(R.string.no_internet));

					} else {

						listener.onError(id, message);

					}
				}
			}
		}
	}

	public void removeListeners(RequestListener listener) {
		arrRequestlisteners.remove(listener);
		arrRequestlisteners.trimToSize();
	}

	public int getListenerSize() {
		return arrRequestlisteners.size();
	}

}
