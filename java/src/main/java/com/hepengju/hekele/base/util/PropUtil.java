package com.hepengju.hekele.base.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 全局配置文件工具类: 获取本项目配置的属性
 * 
 * <br><br>
 * 
 * <br> 获取字符串
 * <br> get                
 * <br>                    
 * <br> 获取转换好的类型,转换失败返回空   
 * <br> getInteger         
 * <br> getDouble          
 * <br> getBoolean         
 * <br>                    
 * <br> 获取List,配置文件可以用逗号(,)分割
 * <br> getList
 * 
 * <br> 2018-02-05 hepengju
 * <br> 1.追加获取默认值的方法
 * <br> 2.动态reload的功能
 * <br>
 * 
 * @author he_pe 2018-01-11
 *
 */
public class PropUtil {

	private static final Logger logger = LoggerFactory.getLogger(PropUtil.class);
	
	//20190505 支持系统的环境变量
	private static final PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${","}");
	private static final Properties osProp = System.getProperties();
	
	//属性配置文件的名称与编码
	private static final String PROPERTIES_NAME = "classpath:topcheer.properties";
	//private static final String CHARSET_NAME = "UTF-8";
	
	//静态代码块取得配置信息
	private static Properties prop;
	static {
		loadProperties();
		reloadProperties();
	}
	
	//重新加载属性
	private static boolean reload;
	private static int reloadSeconds;
	private static Long lastModifyTime;
	
	//定时器
	private static Timer timer;

	/**
	 * 取得配置信息
	 */
	private static synchronized void loadProperties() {
		Properties properties = new Properties();
		// 此种加载方式支持属性文件放在以下路径: Webapp/WEB-INF/lib,Webapp/WEB-INF/classes,Appserver/lib,JRE/lib
		//try (InputStreamReader isr = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(PROPERTIES_NAME), CHARSET_NAME);) {
		try(FileInputStream fis = new FileInputStream(ResourceUtils.getFile(PROPERTIES_NAME)))	{
			properties.load(fis);
			prop = properties;
			
			//由于要自动生成BO类时,总是打印这个比较烦,因此注释掉
			//logger.info("{}load properties: {}",Objects.isNull(lastModifyTime) ? "" : "re",PROPERTIES_NAME);
			
			//这种方式对于路径含有空格的会转换为%20,导致下面Files.readAllLines方法报错无法找到这个文件
			//lastModifyTime = new File(Thread.currentThread().getContextClassLoader().getResource(PROPERTIES_NAME).getFile()).lastModified();
			lastModifyTime = ResourceUtils.getFile(PROPERTIES_NAME).lastModified();
		} catch (IOException e) {
			logger.error("load properties: {} failure",PROPERTIES_NAME);
		}
	}

	/**
	 * 定时任务重载配置: 配置为重载,且文件的最后修改时间与保存的不一致,则重载属性
	 */
	private static void reloadProperties() {
		reload = getBoolean("topcheer.properties.reload", true);
		if(reload) {
			reloadSeconds = getInteger("topcheer.properties.reloadSeconds", 300);
			timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run() {
					try {
						if(lastModifyTime != ResourceUtils.getFile(PROPERTIES_NAME).lastModified()) {
							loadProperties();
						}
					} catch (FileNotFoundException e) {
						logger.error(StackUtil.getStackTrace(e));
					}
				}
			}, reloadSeconds * 1000L,reloadSeconds * 1000L);
		}
	}
	
	/**
	 * 取消Timer
	 */
	public static void cancelTimer() {
		if(timer != null) timer.cancel();
	}	
	
	////////////////////////////////--get--////////////////////////////////
	public static String get(String key) {
	  //return StringUtils.isBlank(key) ? "" : prop.getProperty(key, "").trim();
	    osProp.putAll(prop);
	    return StringUtils.isBlank(key) ? "" : helper.replacePlaceholders(prop.getProperty(key, ""), osProp).trim();
	}
	public static String getString(String key) {
		return get(key);
	}
	public static Integer getInteger(String key) {
		try {return Integer.parseInt(get(key));} catch (NumberFormatException e) {return null;}
	}
	public static Double getDouble(String key) {
		try {return Double.parseDouble(get(key));} catch (NumberFormatException e) {return null;}
	}
	public static Boolean getBoolean(String key) {
		String value = get(key);
		if ("true".equalsIgnoreCase(value))  return true;
		if ("false".equalsIgnoreCase(value)) return false;
		return null;
	}
	public static List<String> getList(String key) {
		Objects.requireNonNull(key);
		return Arrays.asList(key.split(",")).stream().map(PropUtil::get).collect(Collectors.toList());
	}
	////////////////////////////////--get-default-////////////////////////////////
	public static String get(String key,String defaultValue) {
	    String value = get(key);
		return StringUtils.isBlank(value) ? defaultValue : value;
	}
	public static Integer getInteger(String key,Integer defaultValue) {
	    Integer value = getInteger(key);
        return value == null ? defaultValue : value;
	}
	public static Double getDouble(String key,Double defaultValue) {
	    Double value = getDouble(key);
		return value == null ? defaultValue : value;
	}
	public static Boolean getBoolean(String key,Boolean defaultValue) {
	    Boolean value = getBoolean(key);
		return value == null ? defaultValue : value;
	}


	/**
	 * 克隆一份
	 */
	public static Properties cloneProperties(Properties properties){
		Properties newProperties = new Properties();
		properties.forEach(newProperties::put);
		return newProperties;
	}

	/**
	 * 向某个属性中添加一些属性
	 */
	public static void addProperties(Properties targetProperties, Properties someProperties){
		someProperties.forEach(targetProperties::put);
	}
}
