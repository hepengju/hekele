package com.hepengju.hekele.base.util;

import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * xml工具类
 * <p>
 * 修正: 20190715 何鹏举 不适用XStream, 而使用JAXB处理
 *
 * @author hepengju
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
}
