package com.example.volleydemo2.utils;

import java.io.UnsupportedEncodingException;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 把New的类型传进来，通过反射创建对象，并通过回调把bean传回去
 * 
 * 
 * 使用方法： 只要创建GSONRequest对象，并且把 Listener和ErrorListener以及Class对象传进来
 * GSONRequest(String url, Class<?> entry, Listener<T> listener,ErrorListener
 * errorListener) listener:等到parseNetworkResponse方法处理完数据，并返回不为null是，
 * 就通过listener回调deliverResponse方法。
 * 
 * @author wfy
 * 
 * @param <T>
 */
public class GSONRequest<T> extends Request<T> {
	// 回调监听
	private final Listener<T> mListener;
	// 通过回调返回的实体类的class对象
	private Class<?> clazz;

	/**
	 * 
	 * @param method
	 *            请求方式
	 * @param url
	 *            请求的资源地址
	 * @param entry
	 *            把请求的数据封装至该实体类
	 * @param listener
	 *            回调监听接口
	 * @param errorListener
	 *            错误监听，框架内部自行调用
	 */
	public GSONRequest(int method, String url, Class<?> entry,
			Listener<T> listener, ErrorListener errorListener) {
		super(method, url, errorListener);
		mListener = listener;
		clazz = entry;
	}

	/**
	 * 
	 * @param url
	 *            请求的资源地址
	 * @param entry
	 *            把请求的数据封装至该实体类
	 * @param listener
	 *            回调监听接口
	 * @param errorListener
	 *            错误监听，框架内部自行调用
	 */
	public GSONRequest(String url, Class<?> entry, Listener<T> listener,
			ErrorListener errorListener) {
		this(Method.GET, url, entry, listener, errorListener);
	}

	/**
	 * 此方法是我们得到服务器响应数据，并且对数据进行处理的。 此方法如果返回null，那么不会调用deliverResponse方法，
	 * 而是会调用onErrorResponse方法
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		Gson gson = new Gson();
		try {
			String data = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			// 构造javabean对象
			T entry = (T) gson.fromJson(data, clazz);
			return Response.success(entry,
					HttpHeaderParser.parseCacheHeaders(response));
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 当parseNetworkResponse此方法调用完成，并且返回的结果不为null，就会调用此方法。
	 */
	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}
}
