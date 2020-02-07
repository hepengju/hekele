package com.hepengju.hekele.base.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 文件编码检测
 * 
 * <br> 说明: 查询java开源的关于文件编码检测的项目,如下:
 * <br>   * juniversalchardet
 * <br>   * jChardet
 * <br>   * cpdetector: http://cpdetector.sourceforge.net/
 * <br>   * chardet: org.webjars.npm.chardet js版本的文件编码检测
 * <br>   * ICU4J: 成熟的广泛使用的提供应用软件支持Unicode和全球化,一直更新,但体积较大(11M)
 * <br>   * 简单代码编写
 * <br>  
 * <br> 测试: http://fredeaker.blogspot.com/2007/01/character-encoding-detection.html
 * <br> 根据其测试结果显示cpdetector的正确率最高,因此采用cpdetector作为此处检测依据. 
 * <br> 注意: cpdetector的maven依赖实测无效,因此自己上传到nexus私服
 * <br>      cpdetector依赖chardet.jar(中央仓库无,需自己上传)和antlr.jar(中央仓库有,但和依赖版本不同),因此也上传到nexus私服
 * <br>
 * <br>  cpdetector的GAV --> net.sourceforge.cpdetector : cpdetector : 1.0.10
 * <br>  chardet的GAV --> org.mozilla.intl : chardet : 1.0
 * <br>  antlr的GAV --> antlr : antlr : 2.7.4
 * 
 * @author he_pe 2018-02-23
 *
 */
public class CharDecUtil {

	/**
	 * 简单判断文本文件的编码, GBK/UTF-8/UTF-16BE/UTF-16LE
	 */
	public static String getFileCharset(String fileName) {

		String charset = "GBK";
		byte[] first3Bytes = new byte[3];

		BufferedInputStream bis = null;

		try {
			boolean checked = false;
			bis = new BufferedInputStream(new FileInputStream(fileName));

			bis.mark(0);
			int read = bis.read(first3Bytes, 0, 3);
			if (read == -1) {
				return charset; // 文件编码为 ANSI
			} else if (first3Bytes[0] == (byte) 0xFF && first3Bytes[1] == (byte) 0xFE) {
				charset = "UTF-16LE"; // 文件编码为 Unicode
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xFE && first3Bytes[1] == (byte) 0xFF) {
				charset = "UTF-16BE"; // 文件编码为 Unicode big endian
				checked = true;
			} else if (first3Bytes[0] == (byte) 0xEF && first3Bytes[1] == (byte) 0xBB
					&& first3Bytes[2] == (byte) 0xBF) {
				charset = "UTF-8"; // 文件编码为 UTF-8
				checked = true;
			}

			bis.reset();
			if (!checked) {
				while ((read = bis.read()) != -1) {
					if (read >= 0xF0)
						break;
					if (0x80 <= read && read <= 0xBF) // 单独出现BF以下的，也算是GBK
						break;
					if (0xC0 <= read && read <= 0xDF) { // 双字节 (0xC0 - 0xDF)
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) // (0x80 - 0xBF),也可能在GB编码内
							continue;
						else
							break;
					} else if (0xE0 <= read && read <= 0xEF) { // 也有可能出错，但是几率较小
						read = bis.read();
						if (0x80 <= read && read <= 0xBF) {
							read = bis.read();
							if (0x80 <= read && read <= 0xBF) {
								charset = "UTF-8";
								break;
							} else
								break;
						} else
							break;
					}
				}
			}

		} catch (Exception e) {
			//e.printStackTrace();
		} finally {
			try {
				bis.close();
			} catch (IOException e) {
			}
		}
		return charset;
	}

//
//  利用cpdetector精准获得文件编码
//
//	/**
//	 * 利用第三方开源包cpdetector获取文件编码格式
//	 * 
//	 * @param path 要判断文件编码格式的源文件的路径
//	 * @author huanglei
//	 * @version 2012-7-12 14:05
//	 */
//	public static String getFileEncode(String path) {
//		/*
//		 * detector是探测器，它把探测任务交给具体的探测实现类的实例完成。
//		 * cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法 加进来，如ParsingDetector、
//		 * JChardetFacade、ASCIIDetector、UnicodeDetector。
//		 * detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的
//		 * 字符集编码。使用需要用到三个第三方JAR包：antlr.jar、chardet.jar和cpdetector.jar
//		 * cpDetector是基于统计学原理的，不保证完全正确。
//		 */
//		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
//		/*
//		 * ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于 指示是否显示探测过程的详细信息，为false不显示。
//		 */
//		detector.add(new ParsingDetector(false));
//		/*
//		 * JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码
//		 * 测定。所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以
//		 * 再多加几个探测器，比如下面的ASCIIDetector、UnicodeDetector等。
//		 */
//		detector.add(JChardetFacade.getInstance());// 用到antlr.jar、chardet.jar
//		// ASCIIDetector用于ASCII编码测定
//		detector.add(ASCIIDetector.getInstance());
//		// UnicodeDetector用于Unicode家族编码的测定
//		detector.add(UnicodeDetector.getInstance());
//		java.nio.charset.Charset charset = null;
//		File f = new File(path);
//		try {
//			charset = detector.detectCodepage(f.toURI().toURL());
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		if (charset != null)
//			return charset.name();
//		else
//			return null;
//	}
//
//	/**
//	 * 利用第三方开源包cpdetector获取URL对应的文件编码
//	 * 
//	 * @param path 要判断文件编码格式的源文件的URL
//	 * @author huanglei
//	 * @version 2012-7-12 14:05
//	 */
//	public static String getFileEncode(URL url) {
//		/*
//		 * detector是探测器，它把探测任务交给具体的探测实现类的实例完成。
//		 * cpDetector内置了一些常用的探测实现类，这些探测实现类的实例可以通过add方法 加进来，如ParsingDetector、
//		 * JChardetFacade、ASCIIDetector、UnicodeDetector。
//		 * detector按照“谁最先返回非空的探测结果，就以该结果为准”的原则返回探测到的
//		 * 字符集编码。使用需要用到三个第三方JAR包：antlr.jar、chardet.jar和cpdetector.jar
//		 * cpDetector是基于统计学原理的，不保证完全正确。
//		 */
//		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
//		/*
//		 * ParsingDetector可用于检查HTML、XML等文件或字符流的编码,构造方法中的参数用于 指示是否显示探测过程的详细信息，为false不显示。
//		 */
//		detector.add(new ParsingDetector(false));
//		/*
//		 * JChardetFacade封装了由Mozilla组织提供的JChardet，它可以完成大多数文件的编码
//		 * 测定。所以，一般有了这个探测器就可满足大多数项目的要求，如果你还不放心，可以
//		 * 再多加几个探测器，比如下面的ASCIIDetector、UnicodeDetector等。
//		 */
//		detector.add(JChardetFacade.getInstance());// 用到antlr.jar、chardet.jar
//		// ASCIIDetector用于ASCII编码测定
//		detector.add(ASCIIDetector.getInstance());
//		// UnicodeDetector用于Unicode家族编码的测定
//		detector.add(UnicodeDetector.getInstance());
//		java.nio.charset.Charset charset = null;
//		try {
//			charset = detector.detectCodepage(url);
//		} catch (Exception ex) {
//			ex.printStackTrace();
//		}
//		if (charset != null)
//			return charset.name();
//		else
//			return null;
//	}
}
