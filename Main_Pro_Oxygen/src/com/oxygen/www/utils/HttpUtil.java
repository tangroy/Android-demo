package com.oxygen.www.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.KmWeiboApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.oxygen.www.api.UrlConstants;
import com.oxygen.www.base.Constants;
import com.oxygen.www.base.OxygenApplication;
import com.oxygen.www.widget.GDStringRequest;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

public class HttpUtil {
	/**
	 * 以POST形式加载数据 oauth1.0认证
	 * 
	 * @param uriAPI
	 * @param handler
	 * @param requestcode
	 * @param params
	 */
	public static void Post(String uriAPI, final Handler handler,
			final int requestcode, final Map<String, String> params) {
		final StringBuilder url = new StringBuilder(UrlConstants.API_PREFIX)
				.append(uriAPI);
		JSONObject jsonObject = new JSONObject(params);
		JsonObjectRequest postRequest = new JsonObjectRequest(
				Request.Method.POST, url.toString(), jsonObject,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {
						// 处理返回的JSON数据
						Message msg = new Message();
						msg.what = requestcode;
						msg.obj = response;
						handler.sendMessage(msg);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Message msg = new Message();
						msg.what = requestcode;
						msg.obj = null;// 返回空表示获取失败
						handler.sendMessage(msg);
					}
				})

		{

			protected final String TYPE_UTF8_CHARSET = "charset=UTF-8";  
			  
	        // 重写parseNetworkResponse方法改变返回头参数解决乱码问题  
	        // 主要是看服务器编码，如果服务器编码不是UTF-8的话那么就需要自己转换，反之则不需要  
	        @Override  
	        protected Response<JSONObject> parseNetworkResponse(  
	                NetworkResponse response) {  
	            try {  
	                String type = response.headers.get(HTTP.CONTENT_TYPE);  
	                if (type == null) {  
	                    type = TYPE_UTF8_CHARSET;  
	                    response.headers.put(HTTP.CONTENT_TYPE, type);  
	                } else if (!type.contains("UTF-8")) {  
	                    type += ";" + TYPE_UTF8_CHARSET;  
	                    response.headers.put(HTTP.CONTENT_TYPE, type);  
	                }  
	            } catch (Exception e) {  
	            }  
	            return super.parseNetworkResponse(response);  
	        }  
			
			@Override
			protected Map<String, String> getParams() {
				return params;
			}

			// oauth 1.0 http认证
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				OAuthService service = new ServiceBuilder()
						.provider(KmWeiboApi.class)
						.apiKey(Constants.CONSUMER_KTY)
						.apiSecret(Constants.CONSUMER_SECRET).build();
				OAuthRequest request = new OAuthRequest(Verb.POST,
						url.toString());
				String oauth_token =(String)UserInfoUtils.getUserInfo(OxygenApplication.context, Constants.OAUTH_TOKEN, "");
				String oauth_token_secret =(String)UserInfoUtils.getUserInfo(OxygenApplication.context, Constants.OAUTH_TOKEN_SECRET, "");
				Token accessToken = new Token(oauth_token,oauth_token_secret,"");
				
				service.signRequest(accessToken, request);
				Map<String, String> headerMap = request.getHeaders();
				headerMap
				.put("User-Agent",
						Build.MODEL
								+ "/Android "
								+ android.os.Build.VERSION.RELEASE
								+ "/"
								+ "Package:com.oxygen.www/Version:"
								+ GDUtil.GetVersionCodename(OxygenApplication.context));
				return headerMap;
			}

		};
		postRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		OxygenApplication.requestQueue.add(postRequest);

	}

	public static void Get(final String uriAPI, final Handler handler,
			final int requestcode) {

		final StringBuilder url = new StringBuilder(UrlConstants.API_PREFIX)
				.append(uriAPI);
		StringRequest jsonObjectRequest = null;
		try {
			jsonObjectRequest = new StringRequest(Request.Method.GET,
					new String(url.toString().getBytes(), "utf-8"),
					new Response.Listener<String>() {
						@Override
						public void onResponse(String response) {
							Message msg = new Message();
							msg.what = requestcode;
							msg.obj = response;
							handler.sendMessage(msg);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Message msg = new Message();
							msg.what = requestcode;
							msg.obj = null;// 返回空表示获取失败
							handler.sendMessage(msg);
						}
					}) {
				
				protected final String TYPE_UTF8_CHARSET = "charset=UTF-8";  
		        // 重写parseNetworkResponse方法改变返回头参数解决乱码问题  
		        // 主要是看服务器编码，如果服务器编码不是UTF-8的话那么就需要自己转换，反之则不需要  
		        @Override  
		        protected Response<String> parseNetworkResponse(  
		                NetworkResponse response) {  
		            try {  
		                String type = response.headers.get(HTTP.CONTENT_TYPE);  
		                if (type == null) {  
		                    type = TYPE_UTF8_CHARSET;  
		                    response.headers.put(HTTP.CONTENT_TYPE, type);  
		                } else if (!type.contains("UTF-8")) {  
		                    type += ";" + TYPE_UTF8_CHARSET;  
		                    response.headers.put(HTTP.CONTENT_TYPE, type);  
		                }  
		            } catch (Exception e) {  
		            }  
		            return super.parseNetworkResponse(response);  
		        }  

				@Override
				public Map<String, String> getHeaders() throws AuthFailureError {

					OAuthService service = new ServiceBuilder()
							.provider(KmWeiboApi.class)
							.apiKey(Constants.CONSUMER_KTY)
							.apiSecret(Constants.CONSUMER_SECRET).build();
					OAuthRequest request = new OAuthRequest(Verb.GET,
							url.toString());
					String oauth_token =(String)UserInfoUtils.getUserInfo(OxygenApplication.context, Constants.OAUTH_TOKEN, "");
					String oauth_token_secret =(String)UserInfoUtils.getUserInfo(OxygenApplication.context, Constants.OAUTH_TOKEN_SECRET, "");
					Token accessToken = new Token(
							oauth_token,oauth_token_secret,"");
					service.signRequest(accessToken, request);
					Map<String, String> headerMap = request.getHeaders();
					headerMap
							.put("User-Agent",
									"Device:"+Build.MODEL
											+ "/Android:"
											+ android.os.Build.VERSION.RELEASE
											+ "/"
											+ "Package:com.oxygen.www/Version:"
											+ GDUtil.GetVersionCodename(OxygenApplication.context));
					return headerMap;
				}

			};
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(30 * 1000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
		jsonObjectRequest.setShouldCache(true);
		OxygenApplication.requestQueue.add(jsonObjectRequest);
	}

	/**
	 * post请求 无验证
	 * 
	 * @param uriAPI
	 * @param username
	 * @param password
	 */
	public static void Post_Noauth(String uriAPI, final Handler handler,
			final int requestcode, final Map<String, String> map) {
		StringBuilder url = new StringBuilder(UrlConstants.API_PREFIX)
				.append(uriAPI);
		GDStringRequest stringRequest = new GDStringRequest(
				Request.Method.POST, url.toString(),
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Message msg = new Message();
						msg.what = requestcode;
						msg.obj = response;
						handler.sendMessage(msg);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Message msg = new Message();
						msg.what = requestcode;
						msg.obj = null;// 返回空表示获取失败
						handler.sendMessage(msg);
					}
				}) {
			@Override
			protected Map<String, String> getParams() {
				return map;
			}

		};

		OxygenApplication.requestQueue.add(stringRequest);
	}

	/**
	 * 通过get方式请求网络 无验证
	 * 
	 * @param uriAPI
	 * @param c
	 * @param handler
	 * 
	 * @param requestcode
	 */
	public static void Get_Noauth(String uriAPI, final Handler handler,
			final int requestcode) {
		GDStringRequest jsonObjectRequest = new GDStringRequest(
				Request.Method.GET, uriAPI, new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						Message msg = new Message();
						msg.what = requestcode;
						msg.obj = response;
						handler.sendMessage(msg);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Message msg = new Message();
						msg.what = requestcode;
						msg.obj = null;// 返回空表示获取失败
						handler.sendMessage(msg);
					}
				}) {

		};
		OxygenApplication.requestQueue.add(jsonObjectRequest);
	}

	/**
	 * 七牛图片上传
	 * 
	 * @param token
	 * @param path_file
	 * @param key
	 * @param handler
	 * @param requestcode
	 */
	public static void UploadPhotoForQiuniu(final String token,
			final String path_file, final String key, final Handler handler,
			final int requestcode) {
		OxygenApplication.cachedThreadPool.execute(new Runnable() { 
			public void run() {
				File file = new File(path_file);
				UploadManager uploadManager = new UploadManager();
				uploadManager.put(file, key, token, new UpCompletionHandler() {
					@Override
					public void complete(String arg0, ResponseInfo info,
							JSONObject arg2) {
						Message msg = new Message();
						msg.what = requestcode;
						msg.obj = info;
						handler.sendMessage(msg);
					}
				}, null);
			}
		});

	}

	/**
	 * 七牛图片上传
	 * 
	 * @param token
	 * @param file
	 *            字节
	 * @param key
	 * @param handler
	 * @param requestcode
	 */
	public static void UploadPhotoForQiuniu(final String token,
			final byte[] file, final String key, final Handler handler,
			final int requestcode) {
		OxygenApplication.cachedThreadPool.execute(new Runnable() { 
			public void run() {
				UploadManager uploadManager = new UploadManager();
				uploadManager.put(file, key, token, new UpCompletionHandler() {
					@Override
					public void complete(String arg0, ResponseInfo info,
							JSONObject arg2) {
						Message msg = new Message();
						msg.what = requestcode;
						msg.obj = info;
						handler.sendMessage(msg);
					}
				}, null);
			}
		});

	}
}