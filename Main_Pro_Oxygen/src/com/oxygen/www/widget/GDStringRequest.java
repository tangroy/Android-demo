package com.oxygen.www.widget;

import java.io.UnsupportedEncodingException;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

public class GDStringRequest extends StringRequest{

	public GDStringRequest(int method, String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(method, url, listener, errorListener);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		 String str = null;
	        try {
	            str = new String(response.data,"utf-8");
	        } catch (UnsupportedEncodingException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }
	        return Response.success(str, HttpHeaderParser.parseCacheHeaders(response));
	}

	 

}
