package com.example.volleydemo2.utils;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.HttpHeaderParser;

public class XMLRequest extends Request<XmlPullParser> {
	 private final Listener<XmlPullParser> mListener;

	    /**
	     * ����һ��ָ������ʽ��XmlRequest����
	     */
	    public XMLRequest(int method, String url, Listener<XmlPullParser> listener,
	            ErrorListener errorListener) {
	        super(method, url, errorListener);
	        mListener = listener;
	    }

	    /**
	     * ����һGET��XmlRequest
	     */
	    public XMLRequest(String url, Listener<XmlPullParser> listener, ErrorListener errorListener) {
	        this(Method.GET, url, listener, errorListener);
	    }
	    
	    /**
	     * ��parseNetworkResponse�����ɹ����������ҷ��ؽ������������ڲ��Զ����á�
	     * �ѽ������������Ӧ������������,�ѽ�����Ӧ�Ľ���������ļ�����
	     */ 
	    @Override
	    protected void deliverResponse(XmlPullParser response) {
	    	 System.out.println("-----------" + response.toString());
	    	 mListener.onResponse(response);
	    }
/**
 * ����ԭʼ�������Ӧ���ҷ���һ���ʺϵ�Response���͡�
 * ��������������߳���ִ�еġ�
 * ����÷�������null����ô��Ӧ�����ᱻ������������deliverResponse����
 */
	    @Override
	    protected Response<XmlPullParser> parseNetworkResponse(NetworkResponse response) {
	    	 System.out.println("-----------");
	    	try {  
	            String xmlString = new String(response.data,  
	                    HttpHeaderParser.parseCharset(response.headers));  
	            
//	            System.out.println(xmlString);
	            
	            //ʵ����Xmlpullparserʵ��
	            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();  
	            XmlPullParser xmlPullParser = factory.newPullParser();  
	            
	            xmlPullParser.setInput(new StringReader(xmlString)); 
	            //Returns a successful response containing the parsed result. 
	            return Response.success(xmlPullParser, HttpHeaderParser.parseCacheHeaders(response));  
	        } catch (UnsupportedEncodingException e) {  
	            return Response.error(new ParseError(e));  
	        } catch (XmlPullParserException e) {  
	            return Response.error(new ParseError(e));  
	        }  
	    }
}
