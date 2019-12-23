package com.hepengju.hekele.base.util;

import com.hepengju.hekele.base.core.exception.HeException;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 发送Http请求工具
 * 
 * <br> 参考: 官方QuickStart
 * 
 * TODO: 由于CloseableHttpClient是可重用的,具体查看官方文档后确认是否可以抽取出来重用(而不用每次创建后关闭)
 * 
 * @author he_pe
 */
public class HttpUtil {

	/**
	 * 发送get请求
	 */
	public static String sendGet(String url,Map<String,String> paramMap){
		try(CloseableHttpClient client = HttpClients.createDefault()){
			URI uri = new URIBuilder(url).addParameters(mapToNameValueList(paramMap)).build();
			HttpGet get = new HttpGet(uri);
			try(CloseableHttpResponse response = client.execute(get)){
				return EntityUtils.toString(response.getEntity(),"UTF-8");
			}
		} catch (Exception e) {
			throw new HeException(e);
		}
	}
	
	/**
	 * 发送post请求
	 */
	public static String sendPost(String url,Map<String,String> paramMap){
		try(CloseableHttpClient client = HttpClients.createDefault()){
			HttpPost post = new HttpPost(url);
			post.setEntity(new UrlEncodedFormEntity(mapToNameValueList(paramMap), "UTF-8"));
			post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
			try(CloseableHttpResponse response = client.execute(post)){
				return EntityUtils.toString(response.getEntity(),"UTF-8");
			}
		} catch (IOException e) {
			throw new HeException(e);
		}
	}
	
	/**
	 * 发送post请求,格式为json
	 */
	public static String sendPostJson(String url,String json) {
		try(CloseableHttpClient client = HttpClients.createDefault()){
			HttpPost post = new HttpPost(url);
			post.setEntity(new StringEntity(json, "UTF-8"));
			post.setHeader("Content-Type", "application/json; charset=utf-8");
			try(CloseableHttpResponse response = client.execute(post)){
				return EntityUtils.toString(response.getEntity(),"UTF-8");
			}
		} catch (IOException e) {
			throw new HeException(e);
		}
	}
	
	// 请求参数Map,转换为http-client所需的名值对列表
	private static List<NameValuePair> mapToNameValueList(Map<String,String> paramMap){
		List<NameValuePair> params = new ArrayList<>();
		if(Objects.nonNull(paramMap))
			paramMap.forEach((k,v) -> params.add(new BasicNameValuePair(k, v)));
		return params;
	}
	
}