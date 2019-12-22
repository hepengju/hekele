package com.hepengju.hekele.base.util;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * xml工具类
 *
 * 修正: 20190715 何鹏举 不适用XStream, 而使用JAXB处理
 *
 * @author hepengju
 *
 */
public class XmlUtil {

	public static String toXml(Object obj) {
		StringWriter writer = new StringWriter();
		JAXB.marshal(obj, writer);
		return writer.getBuffer().toString();
	}

	public static <T> T fromXml(String xml, Class<T> clazz) {
		return JAXB.unmarshal(new StringReader(xml), clazz);
	}

//  旧版本
//	static XStream xs;
//
//	//XStream的初始化设置
//	static {
//		xs = new XStream();
//		XStream.setupDefaultSecurity(xs);
//		xs.ignoreUnknownElements();
//	    xs.addPermission(AnyTypePermission.ANY);
//	}
//
//	public static String toXml(Object obj) {
//		return xs.toXML(obj);
//	}
//
//	@SuppressWarnings("unchecked")
//	public static <T> T fromXml(String xml) {
//		return (T) xs.fromXML(xml);
//	}

}
