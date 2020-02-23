package com.hepengju.hekele.base.config.springmvc;

import com.alibaba.fastjson.JSON;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 与Spring整合的日期转换器(前台以普通参数传递到后台的日期解析)
 *
 * <br>
 * <br>1.采用fastjson的日期转换,可以支持多种格式
 * <br>2.fastjson的Github首页: https://github.com/alibaba/fastjson
 * <br>3.支持的日期格式如下:
 *   <li> ISO-8601日期格式
 *   <li> yyyy-MM-dd
 *   <li> yyyy-MM-dd HH:mm:ss
 *   <li> yyyy-MM-dd HH:mm:ss.SSS
 *   <li> 毫秒数字
 *   <li> 毫秒数字字符串
 *   <li> .NET JSON日期格式
 *   <li> new Date(198293238)
 *
 *
 * @author he_pe 2018-01-24
 */
@Component
public class DateConverter implements Converter<String, Date> {
    @Override
    public Date convert(String source) {
        String dateStr = JSON.toJSONString(source);
        Date date = JSON.parseObject(dateStr, Date.class);
        return date;
    }
}