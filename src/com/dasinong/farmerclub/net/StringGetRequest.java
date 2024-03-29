package com.dasinong.farmerclub.net;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.dasinong.farmerclub.DsnApplication;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper;
import com.dasinong.farmerclub.ui.manager.SharedPreferencesHelper.Field;
import com.dasinong.farmerclub.utils.Logger;

public class StringGetRequest extends Request<String> {
	// private Map<String, String> mMap;
	private Response.Listener<String> mListener;
	public String cookieFromResponse;
	private String mHeader;
	private Map<String, String> sendHeader = new HashMap<String, String>(1);

	public StringGetRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
		super(Request.Method.GET, url, errorListener);
		mListener = listener;
		// mMap = map;
	}

	// 当http请求是post时，则需要该使用该函数设置往里面添加的键值对
	// @Override
	// protected Map<String, String> getParams() throws AuthFailureError {
	// return mMap;
	// }
	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {

		// try {
		// return Response.success(new String(response.data, "UTF-8"),
		// HttpHeaderParser.parseCacheHeaders(response));
		// } catch (UnsupportedEncodingException e) {
		// return Response.error(new ParseError(e));
		// } catch (Exception je) {
		// return Response.error(new ParseError(je));
		// }

		try {
			String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
			return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} 
	}

	@Override
	protected void deliverResponse(String response) {
		mListener.onResponse(response);
	}
}
