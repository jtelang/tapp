package com.tapp.network;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.tapp.utils.Log;

public class RequestClient {

	private static final String TAG = "REQUEST Client";

	private ArrayList<NameValuePair> params;
	private ArrayList<NameValuePair> headers;
	private String url;

	private int responseCode;
	private String message;
	private String response;

	public String getResponse() {
		return response;
	}

	public String getErrorMessage() {
		return message;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public RequestClient(String url, RequestListener listener, int requestId) {
		this.url = url;
		params = new ArrayList<NameValuePair>();
		headers = new ArrayList<NameValuePair>();
	}

	public void addParam(String name, String value) {
		params.add(new BasicNameValuePair(name, value));
	}

	public void addHeader(String name, String value) {
		headers.add(new BasicNameValuePair(name, value));
	}

	public void execute(RequestMethod method) throws Exception {

		switch (method) {

			case GET : {
				// add parameters
				String combinedParams = "";
				if (params != null && !params.isEmpty()) {
					combinedParams += "?";
					for (NameValuePair p : params) {
						String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
						if (combinedParams.length() > 1) {
							combinedParams += "&" + paramString;
						} else {
							combinedParams += paramString;
						}
					}
				}

				HttpGet request = new HttpGet(url + combinedParams);

				// add headers
				for (NameValuePair h : headers) {
					request.addHeader(h.getName(), h.getValue());
				}

				HttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpclient = new DefaultHttpClient(httpParameters);
				HttpResponse httpResponse = httpclient.execute(request);

				response = EntityUtils.toString(httpResponse.getEntity());

				Log.d(TAG, "URL => " + url + combinedParams);

				break;
			}
			case POST : {

				HttpPost request = new HttpPost(url);

				Log.d(TAG, "URL => " + url);

				// add headers
				for (NameValuePair h : headers) {
					request.addHeader(h.getName(), h.getValue());

				}

				if (params != null && params.size() > 0) {

					request.setEntity(new StringEntity(params.get(0).getValue()));

					// for (int i = 0; i < params.size(); i++) {
					//
					// ArrayList<NameValuePair> nameValuePairs = new
					// ArrayList<NameValuePair>();
					// nameValuePairs.add(new
					// BasicNameValuePair(params.get(i).getName(),
					// params.get(i).getValue()));
					// request.setEntity(new
					// UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
					// }
				}

				HttpParams httpParameters = new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParameters, 10000);
				HttpConnectionParams.setSoTimeout(httpParameters, 15000);

				HttpClient httpClient = new DefaultHttpClient(httpParameters);
				HttpResponse httpResponse = httpClient.execute(request);

				response = EntityUtils.toString(httpResponse.getEntity());

				break;
			}
		}
	}
}
