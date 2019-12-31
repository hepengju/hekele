package com.hepengju.hekele.base.util;

import com.hepengju.hekele.base.core.exception.HeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.commons.net.ftp.FTPSClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * FTP工具类
 * 
 * <pre>
 * 需求分析: 简要实现简单的FTP上传下载功能
 *   1.上传文件到FTP指定路径
 *   2.下载FTP指定文件到本地
 *   3.检查FTP指定文件是否存在
 * 
 * 做法: 参考FTP本身的命令
 * 
 *   get  [remote-file]         从远端主机中传送至本地主机中
 *   put  [local-file]          将本地一个文件传送至远端主机中
 *   delete [remote-file]       删除远端主机中的文件
 * 
 * 其他说明:
 *   FTP支持两种模式: Standard(PORT方式，主动方式),Passive(PASV，被动方式). 本工具采用<b>主动</b>方式!
 *   FTP的传输有两种方式: ASCII,二进制. 本工具采用<b>二进制</b>方式.
 * </pre>
 * 
 * @author he_pe 2018-03-12
 */
@Slf4j
public class FtpUtil {

	private static FTPClient ftpClient = null;
	
	/**
	 * 初始化ftpClient
	 */
	public static synchronized void initFtpClient(String ftpType,String host,int port,String username,String password,int timeout) {
		String loginfo = "M.get(\"job.ftpconfig.connection\", host)";
		
		ftpClient = "ftps".equalsIgnoreCase(ftpType) ? new FTPSClient() : new FTPClient();
		try {
			//连接,登录,设置编码,设置传输类型
			ftpClient.connect(host, port);
			if(StringUtils.isNoneBlank(username,password)) ftpClient.login(username, password);
			ftpClient.setConnectTimeout(timeout);
			ftpClient.setAutodetectUTF8(true);
			ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
			
			//响应检测
			int replyCode = ftpClient.getReplyCode();
			if(FTPReply.isPositiveCompletion(replyCode)) {
				log.info(loginfo + "成功");
			}else {
				throw new HeException("job.ftpconfig.connection");
			}
		} catch (Exception e) {
			log.info(loginfo + "异常");
			throw new HeException("job.ftpconfig.connectionFailure");
		}
		
	}
	
	/**
	 * 上传文件到指定路径
	 */
	public static void put(String localFile,String remotePath) {
		String loginfo = "M.get(\"job.ftpconfig.put\", localFile,remotePath)";
		
		File file = new File(localFile);
		try (FileInputStream fis = new FileInputStream(file)){
			//切换工作目录,切换失败则尝试创建目录后再次进行切换
			boolean cwd = ftpClient.changeWorkingDirectory(remotePath);
			if(!cwd) {
				ftpClient.mkd(remotePath); //已测试,支持创建多级目录
			}
			cwd	= ftpClient.changeWorkingDirectory(remotePath);
			
			//取得和上传文件一致的文件名,存储完成后,登出
			String remote = file.getName();
			boolean storeFile = ftpClient.storeFile(remote, fis);
			if(storeFile) {
				log.info(loginfo + "成功");
			}else {
				log.info(loginfo + "失败");
			}
		} catch (Exception e) {
			log.info(loginfo + "异常");
			throw new HeException("job.ftpconfig.putFailure");
		} finally {
			logout();
		}
	}
	
	
	/**
	 * 下载文件到指定路径
	 */
	public static void get(String remoteFile,String localPath) {
		String loginfo = "M.get(\"job.ftpconfig.get\", remoteFile,localPath)";
		String name = new File(remoteFile).getName();
		try(FileOutputStream fos = new FileOutputStream(new File(localPath, name))) {
			boolean retrieveFile = ftpClient.retrieveFile(remoteFile, fos);
			if(retrieveFile) {
				log.info(loginfo + "成功");
			}else {
				log.info(loginfo + "失败");
			}
		} catch (Exception e) {
			log.info(loginfo + "异常");
			throw new HeException("job.ftpconfig.getFailure");
		} finally {
			logout();
		}
	}
	
	/**
	 * 删除文件
	 */
	public static void delete(String remoteFile) {
		String loginfo = "M.get(\"job.ftpconfig.delete\", remoteFile)";
		try {
			boolean deleteFile = ftpClient.deleteFile(remoteFile);
			if(deleteFile) {
				log.info(loginfo + "成功");
			}else {
				log.info(loginfo + "失败");
			}
		} catch (IOException e) {
			log.info(loginfo + "异常");
			throw new HeException("job.ftpconfig.deleteFailure");
		} finally {
			logout();
		}
	}
	
	/**
	 * 退出Ftp连接
	 */
	public static void logout() {
		String loginfo = "M.get(\"job.ftpconfig.logout\")";
		try {
			boolean logout = ftpClient.logout();
			if(logout) {
				log.info(loginfo + "成功");
			}else {
				log.info(loginfo + "失败");
			}
		} catch (IOException e) {
			log.info(loginfo + "异常");
			throw new HeException("job.ftpconfig.logoutFailure");
		}
	}
}
