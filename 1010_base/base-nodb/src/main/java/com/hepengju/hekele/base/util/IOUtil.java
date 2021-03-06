package com.hepengju.hekele.base.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * IO工具
 * 
 * @author hepengju
 */
@Slf4j
public class IOUtil {

	/**
	 * 读取文件最后N行
	 */
	public static List<String> readLastLines(File file, int numRead) {
		List<String> result = new ArrayList<String>();
		if(numRead <= 0) return result;
		
		try (ReversedLinesFileReader rlf = new ReversedLinesFileReader(file,StandardCharsets.UTF_8)){
			int i = 0;
			while(i < numRead) {
				try {
					String readLine = rlf.readLine();
					if(readLine == null) break;
					result.add(rlf.readLine());
					i++;
				} catch (Exception e) {
					break;
				}
			}
		} catch (IOException e) {
			log.error(StackUtil.getStackTrace(e));
		}
		
		//正序返回
		Collections.reverse(result);
		return result;
	}
}
