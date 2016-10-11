package com.example.volleydemo2;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;

import android.app.Activity;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.volleydemo2.domain.Antivirus;
import com.example.volleydemo2.utils.GSONRequest;
import com.example.volleydemo2.utils.XMLRequest;

/**
 * 自定义Gson网络请求
 * 
 * @author wfy
 * 
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		new Thread() {
			public void run() {
				// 发送post请求
				String url = "http://192.168.56.1:8080";

				StringRequest stringRequest = new StringRequest(Method.POST,
						url, new Listener<String>() {

							@Override
							public void onResponse(String response) {
								// TODO Auto-generated method stub

							}
						}, new ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
							}
						}) {

					@Override
					protected Map<String, String> getParams()
							throws AuthFailureError {
						System.out.println("-----------");

						Map<String, String> hashMap = new HashMap<>();
						hashMap.put("md5", "123456");
						hashMap.put("desc", "有毒");
						return hashMap;
					}
				};

				Volley.newRequestQueue(MainActivity.this).add(stringRequest);
				try {
					String string = new String(stringRequest.getBody(), "UTF-8");
					System.out.println(string);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (AuthFailureError e) {
					e.printStackTrace();
				}
				// String url = "http://192.168.56.1:8080/antivirus2.json";
				// GSONRequest<Antivirus> gsonRequest = gsonRequest(url);
				// url = "http://192.168.56.1:8080/news.xml";
				// XMLRequest xmlRequest = xmlRequestToXmlPullParser(url);

				// 加入请求对中
				// Volley.newRequestQueue(MainActivity.this).add(gsonRequest);
				// Volley.newRequestQueue(MainActivity.this).add(xmlRequest);
			}

			private GSONRequest<Antivirus> gsonRequest(String url) {
				GSONRequest<Antivirus> gsonRequest = new GSONRequest<Antivirus>(
						url, Antivirus.class, new Listener<Antivirus>() {

							@Override
							public void onResponse(Antivirus response) {
								System.out.println(response.getMD5());
								System.out.println(response.getDesc());
								System.out.println(response.getName());
								System.out.println(response.getType());
							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								System.out.println(error.getStackTrace()
										+ "----");
							}
						});
				return gsonRequest;
			}

			private XMLRequest xmlRequestToXmlPullParser(String url) {
				XMLRequest xmlRequest = new XMLRequest(Method.GET, url,
						new Listener<XmlPullParser>() {

							@Override
							public void onResponse(XmlPullParser response) {
								try {
									int eventType = response.getEventType();
									// int eventType = response.next();
									while (eventType != XmlPullParser.END_DOCUMENT) {
										eventType = response.next();
										System.out.println(eventType);
										switch (eventType) {
										case XmlPullParser.START_TAG:
											System.out.println(response
													.getName());
											break;
										}
									}
								} catch (Exception e) {
									e.printStackTrace();
								}

							}
						}, new ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								System.out.println("error---"
										+ error.getStackTrace().toString());
							}
						});
				return xmlRequest;
			}
		}.start();
	}
}
