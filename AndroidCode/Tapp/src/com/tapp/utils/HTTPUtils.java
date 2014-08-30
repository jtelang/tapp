package com.tapp.utils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

/**
 * 
 * HTTPUtils is used to provide HTTP utility methods for application
 * 
 * @author
 * 
 */

public class HTTPUtils {

	/**
	 * This method is used to send GET request
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */

	public static String doHTTPGetRequest(String url) throws OutOfMemoryError, Exception {

		String result = "";

		url = url.replaceAll(" ", "%20");
		url = url.replaceAll("\"", "%22");
		Log.i("URL", url);

		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpget = new HttpGet(url);
		// set header
		httpget.addHeader("Content-Type", "application/json");
		// execute HTTP get request
		HttpResponse response = httpclient.execute(httpget);

		result = EntityUtils.toString(response.getEntity());

		return result;
	}

	/**
	 * This method is used to send post request on server
	 * 
	 * @param url
	 * @param param
	 * @return
	 * @throws Exception
	 */

	public static String doHTTPPostRequest(String url, List<NameValuePair> nameValuePair) throws OutOfMemoryError, Exception {

		String result = "";

		Log.i("URL", url);
		Log.i("REQ BODY", nameValuePair.toString());

		// System.setProperty("java.net.preferIPv6Addresses", "false");

		// Create an Object of HttpClient
		HttpClient httpClient = new DefaultHttpClient();

		// Create an Object of HttpPost
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair, HTTP.UTF_8));

		// Execute HttpPost request
		HttpResponse httpResponse = httpClient.execute(httpPost);

		result = EntityUtils.toString(httpResponse.getEntity());

		return result;
	}

	/**
	 * This static method is used to download bitmap from given url
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap doBitmapRequest(String url) throws OutOfMemoryError, Exception {

		Bitmap bitmap = null;

		Log.i("URL", url);

		if (url != null && !url.equals("") && url.length() > 0) {

			URL myUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();

			conn.setDoInput(true);
			conn.connect();
			if (conn.getResponseCode() == 200) {
				InputStream is = conn.getInputStream();
				bitmap = BitmapFactory.decodeStream(is);
			}
		}

		return bitmap;
	}

	public static boolean doImageRequestAndSave(Context context, String url, String imagePath) throws OutOfMemoryError, Exception {

		Log.i("URL", url);

		InputStream input = null;
		FileOutputStream output = null;

		if (url != null && !url.equals("") && url.length() > 0) {

			try {
				URL myUrl = new URL(url);
				input = myUrl.openConnection().getInputStream();
				output = new FileOutputStream(imagePath);
				byte[] buffer = new byte[2048];
				int bytesRead = 0;
				while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
					output.write(buffer, 0, bytesRead);
				}

				return true;

			} finally {
				if (output != null) {
					output.close();
				}
				if (input != null) {
					input.close();
				}
			}
		}

		return false;
	}

	public static String encodeURL(String str) {

		String myString = null;

		myString = str.replace("`", "%60");
		myString = myString.replace("~", "%7E");
		myString = myString.replace("^", "%5E");
		myString = myString.replace("+", "%2B");
		myString = myString.replace("-", "%2D");
		myString = myString.replace("_", "%5F");
		myString = myString.replace("*", "%2A");
		myString = myString.replace(",", "%2C");
		myString = myString.replace(".", "%2E");
		myString = myString.replace("\'", "%27");
		// myString = myString.replace("","%2F");
		myString = myString.replace(";", "%3B");
		myString = myString.replace("=", "%3D");
		myString = myString.replace("?", "%3F");
		myString = myString.replace("@", "%40");
		myString = myString.replace(" ", "%20");
		myString = myString.replace("\t", "%%%");
		myString = myString.replace("$", "%24");
		myString = myString.replace("#", "%23");
		myString = myString.replace("<", "%3C");
		myString = myString.replace(">", "%3E");
		myString = myString.replace("\n", "@@@");
		myString = myString.replace("(", "%28");
		myString = myString.replace(")", "%29");
		myString = myString.replace("{", "%7B");
		myString = myString.replace("}", "%7D");
		myString = myString.replace("[", "%5B");
		myString = myString.replace("]", "%5D");
		myString = myString.replace("!", "%21");
		myString = myString.replace("&", "%26");
		myString = myString.replace("\"", "%22");
		myString = myString.replace("\\", "%68");

		return myString;
	}

	public static String setClientString(String str) {

		String myString = null;

		myString = str.replace("%60", "`");
		myString = myString.replace("%7E", "~");
		myString = myString.replace("%5E", "^");
		myString = myString.replace("%2B", "+");
		myString = myString.replace("%2D", "-");
		myString = myString.replace("%5F", "_");
		myString = myString.replace("%2A", "*");
		myString = myString.replace("%2C", ",");
		myString = myString.replace("%2E", ".");
		myString = myString.replace("%27", "\'");
		myString = myString.replace("%2F", "");
		myString = myString.replace("%3B", ";");
		myString = myString.replace("%3D", "=");
		myString = myString.replace("%3F", "?");
		myString = myString.replace("%40", "@");
		myString = myString.replace("%20", " ");
		myString = myString.replace("%%%", "\t");
		myString = myString.replace("%24", "$");
		myString = myString.replace("%23", "#");
		myString = myString.replace("%3C", "<");
		myString = myString.replace("%3E", ">");
		myString = myString.replace("@@@", "\n");
		myString = myString.replace("%28", "(");
		myString = myString.replace("%29", ")");
		myString = myString.replace("%7B", "{");
		myString = myString.replace("%7D", "}");
		myString = myString.replace("%5B", "[");
		myString = myString.replace("%5D", "]");
		myString = myString.replace("%21", "!");
		myString = myString.replace("%26", "&");
		myString = myString.replace("%22", "\"");
		myString = myString.replace("%68", "\\");

		return myString;
	}
}
