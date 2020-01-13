package com.hepengju.hekele.base.util;

import com.hepengju.hekele.base.core.exception.HeException;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.UserAgent;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static java.net.URLEncoder.encode;

/**
 * Web工具类: 取IP地址,获取当前Request等
 * 
 * @author he_pe 2018-03-05
 */
@Component
public class WebUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	
	public static final String COMPUTER   = "C"         ; // "Computer"
	public static final String MOBILE     = "M"         ; // "Mobile"
	public static final String USER_AGENT = "User-Agent";

	/**
	 * 处理文件下载的通用方法
	 */
	public static void handleFileDownload(String attachment){
		String fileName = attachment;
		// 这种方式: 谷歌/火狐OK, IE/Edge不行
		//try {fileName = new String(fileName.getBytes("UTF-8"),"ISO-8859-1");} catch (UnsupportedEncodingException e) {}

		// 这种方式: 谷歌/火狐/IE/Edge都可以
		try {fileName = encode(attachment, "UTF-8").replaceAll("\\+", "%20"); } catch (UnsupportedEncodingException e) {}
		HttpServletResponse response = WebUtil.getHttpServletResponse();
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"; filename*=utf-8''" + fileName);

		// 保留原始的文件名, 便于前端解析 --> 需要使用; 两种都设置下
		// var filename = resp.headers['original-filename'];
		// filename = decodeURIComponent(escape(filename));
		String webDecodeName = new String(attachment.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
		response.setHeader("Original-Filename", webDecodeName);

		// 以下两个也加入, 以便可能的使用
		response.setHeader("Original-Filename-Java", attachment);
		response.setHeader("Original-Filename-Encode", fileName);
	}

	public static void handleFileDownload(String attachment, InputStream inputStream) {
		handleFileDownload(attachment);
		try (InputStream is = inputStream) {
			IOUtils.copy(is, WebUtil.getHttpServletResponse().getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void handleFileDownload(String attachment, byte[] byteArray){
		handleFileDownload(attachment);
		if(byteArray != null){
			try {
				ServletOutputStream outputStream = WebUtil.getHttpServletResponse().getOutputStream();
				outputStream.write(byteArray);
				outputStream.flush();
			} catch (IOException e) {
				throw new HeException(e);
			}
		}
	}

	/**
	 * 获取请求头或者请求参数的值
	 */
	public static String getHeaderOrParameterValue(HttpServletRequest request, String headerName, boolean headerDecode){
		try {
			return StringUtils.isBlank(request.getHeader(headerName))
					? request.getParameter(headerName)
					: (headerDecode ? URLDecoder.decode(request.getHeader(headerName), StandardCharsets.UTF_8.name())
					: request.getHeader(headerName));
		} catch (UnsupportedEncodingException e) {
			return request.getParameter(headerName);
		}
	}

	/**
	 * 设备
	 */
	@Data @AllArgsConstructor
	public static class Device {
		private String deviceType; // 电脑, 手机
		private String osName;     // 操作系统
		private String browser;    // 浏览器
	}

	/**
	 * 获取设备信息
	 */
	public static Device getDevice() {
		String userAgentHeader = getHttpServletRequest().getHeader(USER_AGENT);
		return getDevice(userAgentHeader);
	}

	public static Device getDevice(String userAgentHeader) {
		UserAgent userAgent = UserAgent.parseUserAgentString(userAgentHeader);
		String brower = userAgent.getBrowser().getName();
		//考虑手机端的浏览器访问, 此处采用解析为Unknown时空格分隔取第一个
		if (Browser.UNKNOWN.getName().equals(brower) && userAgentHeader != null) {
			brower = userAgentHeader.split(" ")[0];
		}

		String osName = userAgent.getOperatingSystem().getName();
		String deviceType = StringUtils.containsAny(userAgentHeader,"Android", "iPhone", "iPad") ? MOBILE : COMPUTER;
		return new Device(deviceType, osName, brower);
	}

	public static boolean isComputer() {
		return COMPUTER.equals(getDevice().getDeviceType());
	}

	/**
	 * 根据Request获取IP地址
	 * 
	 * 使用Nginx等反向代理软件， 则不能通过request.getRemoteAddr()获取IP地址
	 * 如果使用了多级反向代理的话，X-Forwarded-For的值并不止一个，
	 * 而是一串IP地址，X-Forwarded-For中第一个非unknown的有效IP字符串，则为真实IP地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
		// 代理处理
		String ip = request.getHeader("x-forwarded-for");
		if (blankOrUnknown(ip)) ip = request.getHeader("Proxy-Client-IP");
		if (blankOrUnknown(ip)) ip = request.getHeader("WL-Proxy-Client-IP");
		if (blankOrUnknown(ip)) ip = request.getHeader("HTTP_CLIENT_IP");
		if (blankOrUnknown(ip)) ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		
		// 多级代理
		if (StringUtils.isNotBlank(ip) && ip.contains(",")) ip = ip.split(",")[0];
		
		// 正常处理
		if (blankOrUnknown(ip)) ip = request.getRemoteAddr();

		// 特殊设置
		if("0:0:0:0:0:0:0:1".equals(ip) || "localhost".equals(ip)) ip = "127.0.0.1";
		
		//非空限定
		if(ip == null) ip = "unknown";
		
		return ip;
	}
	
	private static boolean blankOrUnknown(String ip) { return StringUtils.isBlank(ip) || "unknown".equalsIgnoreCase(ip); }
	
	/**
	 * 是否为ajax请求
	 */
	public static boolean isAjax(HttpServletRequest request){
		 return request.getHeader("x-requested-with") != null && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		WebUtil.applicationContext = applicationContext;
	}

	/**
	 * 取得实际路径
	 */
	public static String getRealPath(String path) {
		return getHttpServletRequest().getServletContext().getRealPath(path);
	}
	public static Object getBean(String beanName){ return applicationContext.getBean(beanName); }
	public static <T> T  getBean(Class<T> requiredType){
		return applicationContext.getBean(requiredType);
	}
	public static <T> Map<String, T> getBeansOfType(Class<T> requiredType){ return applicationContext.getBeansOfType(requiredType); }
	public static HttpServletRequest getHttpServletRequest() { return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();}
	public static HttpServletResponse getHttpServletResponse() { return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();}

}
