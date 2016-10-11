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
 * ��New�����ʹ�������ͨ�����䴴�����󣬲�ͨ���ص���bean����ȥ
 * 
 * 
 * ʹ�÷����� ֻҪ����GSONRequest���󣬲��Ұ� Listener��ErrorListener�Լ�Class���󴫽���
 * GSONRequest(String url, Class<?> entry, Listener<T> listener,ErrorListener
 * errorListener) listener:�ȵ�parseNetworkResponse�������������ݣ������ز�Ϊnull�ǣ�
 * ��ͨ��listener�ص�deliverResponse������
 * 
 * @author wfy
 * 
 * @param <T>
 */
public class GSONRequest<T> extends Request<T> {
	// �ص�����
	private final Listener<T> mListener;
	// ͨ���ص����ص�ʵ�����class����
	private Class<?> clazz;

	/**
	 * 
	 * @param method
	 *            ����ʽ
	 * @param url
	 *            �������Դ��ַ
	 * @param entry
	 *            ����������ݷ�װ����ʵ����
	 * @param listener
	 *            �ص������ӿ�
	 * @param errorListener
	 *            �������������ڲ����е���
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
	 *            �������Դ��ַ
	 * @param entry
	 *            ����������ݷ�װ����ʵ����
	 * @param listener
	 *            �ص������ӿ�
	 * @param errorListener
	 *            �������������ڲ����е���
	 */
	public GSONRequest(String url, Class<?> entry, Listener<T> listener,
			ErrorListener errorListener) {
		this(Method.GET, url, entry, listener, errorListener);
	}

	/**
	 * �˷��������ǵõ���������Ӧ���ݣ����Ҷ����ݽ��д���ġ� �˷����������null����ô�������deliverResponse������
	 * ���ǻ����onErrorResponse����
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		Gson gson = new Gson();
		try {
			String data = new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
			// ����javabean����
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
	 * ��parseNetworkResponse�˷���������ɣ����ҷ��صĽ����Ϊnull���ͻ���ô˷�����
	 */
	@Override
	protected void deliverResponse(T response) {
		mListener.onResponse(response);
	}
}
