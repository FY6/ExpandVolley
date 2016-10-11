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
	     * 创建一个指定请求方式的XmlRequest请求，
	     */
	    public XMLRequest(int method, String url, Listener<XmlPullParser> listener,
	            ErrorListener errorListener) {
	        super(method, url, errorListener);
	        mListener = listener;
	    }

	    /**
	     * 创建一GET的XmlRequest
	     */
	    public XMLRequest(String url, Listener<XmlPullParser> listener, ErrorListener errorListener) {
	        this(Method.GET, url, listener, errorListener);
	    }
	    
	    /**
	     * 当parseNetworkResponse方法成功解析，并且返回解析结果，框架内部自动调用。
	     * 把解析结果交付响应给监听器处理,把解析响应的结果交付给的监听器
	     */ 
	    @Override
	    protected void deliverResponse(XmlPullParser response) {
	    	 System.out.println("-----------" + response.toString());
	    	 mListener.onResponse(response);
	    }
/**
 * 解析原始网络的响应并且返回一个适合的Response类型。
 * 这个方法是在子线程中执行的。
 * 如果该方法返回null，那么响应将不会被交付。交付给deliverResponse方法
 */
	    @Override
	    protected Response<XmlPullParser> parseNetworkResponse(NetworkResponse response) {
	    	 System.out.println("-----------");
	    	try {  
	            String xmlString = new String(response.data,  
	                    HttpHeaderParser.parseCharset(response.headers));  
	            
//	            System.out.println(xmlString);
	            
	            //实例化Xmlpullparser实例
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
