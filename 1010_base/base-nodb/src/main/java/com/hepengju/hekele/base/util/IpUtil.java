package com.hepengju.hekele.base.util;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;

/**
 * 
 * 本机IP地址获取, 主机名获取等
 * 
 * @author hepengju
 *
 */
public class IpUtil {

	/**
	 * 获取请求头里面的IP地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
		return WebUtil.getIpAddr(request);
	}
	
	/**
	 * 获取本机的IP地址
	 */
	public static String getIpAddr() {
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			String hostAddress = localHost.getHostAddress();
			return hostAddress;
		} catch (Exception e) {
			return "127.0.0.1";
		}
	}
	
	/**
	 * 获取本机的主机名
	 */
	public static String getHostname() {
		try {
			InetAddress localHost = InetAddress.getLocalHost();
			String hostname = localHost.getHostName();
			return hostname;
		} catch (Exception e) {
			return "";
		}	
	}
	
//	/**
//	 * 获取本机的IP地址
//	 */
//	public static String getIpAddr() {
//		try {
//			InetAddress inetAddress = getLocalHostLANAddress();
//			String hostAddress = inetAddress.getHostAddress();
//			return hostAddress;
//		} catch (Exception e) {
//			return "127.0.0.1";
//		}
//	}
//	
//	/**
//	 * 获取本机的主机名
//	 */
//	public static String getHostname() {
//		try {
//			InetAddress inetAddress = getLocalHostLANAddress();
//			String hostname = inetAddress.getHostName();
//			return hostname;
//		} catch (Exception e) {
//			return "";
//		}	
//	}
//	
//	
//	/**
//	 * 参考: https://www.cnblogs.com/xiaoBlog2016/p/7076230.html
//	 * 实测: 不是很好
//	 * 
//	 * @throws Exception
//	 */
//	public static InetAddress getLocalHostLANAddress() throws Exception {
//	    try {
//	        InetAddress candidateAddress = null;
//	        // 遍历所有的网络接口
//	        for (Enumeration<?> ifaces = NetworkInterface.getNetworkInterfaces(); ifaces.hasMoreElements(); ) {
//	            NetworkInterface iface = (NetworkInterface) ifaces.nextElement();
//	            // 在所有的接口下再遍历IP
//	            for (Enumeration<?> inetAddrs = iface.getInetAddresses(); inetAddrs.hasMoreElements(); ) {
//	                InetAddress inetAddr = (InetAddress) inetAddrs.nextElement();
//	                if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
//	                    if (inetAddr.isSiteLocalAddress()) {
//	                        // 如果是site-local地址，就是它了
//	                        return inetAddr;
//	                    } else if (candidateAddress == null) {
//	                        // site-local类型的地址未被发现，先记录候选地址
//	                        candidateAddress = inetAddr;
//	                    }
//	                }
//	            }
//	        }
//	        if (candidateAddress != null) {
//	            return candidateAddress;
//	        }
//	        // 如果没有发现 non-loopback地址.只能用最次选的方案
//	        InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
//	        return jdkSuppliedAddress;
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	    }
//	    return null;
//	}
}
