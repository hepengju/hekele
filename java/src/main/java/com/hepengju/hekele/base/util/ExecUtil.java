package com.hepengju.hekele.base.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 执行系统命令
 * 
 * 
 * <br> 注意死锁问题
 * <br> 解决: 只要主进程在waitfor之前，能不断处理缓冲区中的数据就可以
 * 
 * @see <a href="https://blog.csdn.net/seapeak007/article/details/69668600">调用Process.waitfor导致的进程挂起</a>
 * 
 * @author he_pe
 *
 */
@Slf4j
public class ExecUtil {

	public static void execCommand(String command) {
		log.info("begin exec os command: " + command);
		Process process;
		try {
			process = Runtime.getRuntime().exec(command);
			
			//开启新线程处理正常输出 和 错误输出
			newThreadLogInputStream(process.getInputStream());
			newThreadLogInputStream(process.getErrorStream());

			// 阻塞当前进程,直到命令结束
			process.waitFor();

			// 不会阻塞进程,但是调用时如果没有完成会报错
			if (process.exitValue() != 0) {
				log.error("exec os command failure: " + command);
			} else {
				log.info("exec os command success: " + command);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private static void newThreadLogInputStream(InputStream is){
		new Thread(() ->  {
			try(InputStream inputStream = is;
				BufferedReader bufr = new BufferedReader(new InputStreamReader(inputStream))){
				String out = null;
				while ((out = bufr.readLine()) != null) {
					log.info(out);
				}
			} catch (IOException e) {
			}
		}).start();
	}

}
