package com.hepengju.hekele.base.util;

import com.hepengju.hekele.base.core.exception.HeException;
import com.jcraft.jsch.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

/**
 * SSH远程连接并执行脚本
 *
 * <pre>
 *     技术:
 *          - jsch: http://www.jcraft.com/jsch/
 *          - ganymed-ssh2: http://www.ganymed.ethz.ch/ssh2/
 *     说明: 由于ganymed-ssh2在2006年10月之后不再维护, 因此不予考虑了
 *     参考:  -exec:https://www.cnblogs.com/winniejohn/p/10275277.html
 *            -shell: https://www.jianshu.com/p/5e9b53cea739
 *            -sftp:https://www.cnblogs.com/lfyu/p/8532781.html
 *
 *     <p>
 *         jsch 的channel共有三种模式:
 *          -exec:执行命令的模式，执行完毕后将控制台结果一次性返回
 *          -shell:执行交互模式，边执行边打印，类似于ssh2客户端
 *          -sftp:文件上传下载
 *
 *     </p>
 * </pre>
 *
 * @author chen.nan / he.pj
 *
 */
public class SSHUtil {

    private static Logger logger = LoggerFactory.getLogger(SSHUtil.class);

    /**
     * 回车指令: 以shell交互形式执行，输入回车符号才会执行
     */
    private static final String ENTER = "\n";

    /**
     * 退出指令: 避免消费读取消息完成后会一直阻塞
     */
    private static final String EXIT = "exit\n";

    /**
     * 验证连接信息是否正确
     *
     * @param host       主机, 比如 app.topcheer.xyz
     * @param port       端口: 默认 22
     * @param username   用户: 比如 deployer
     * @param password   密码: 比如 Top#123
     * @return  <p>true：正确</p>
     *          <p>false：错误</p>
     */
    public static boolean testConnection(String host, Integer port, String username, String password){
        Session session = null;
        // 获取session
        try {
            session = connect(host, port, username, password);
            return session.isConnected();
        } catch (JSchException e) {
            return false;
        }finally {
            if(session != null) session.disconnect();
        }
    }

    /**
     * 连接并获取session
     *
     * @param host       主机, 比如 app.topcheer.xyz
     * @param port       端口: 默认 22
     * @param username   用户: 比如 deployer
     * @param password   密码: 比如 Top#123
     * @return 返回一个会话session
     * @throw JSchException
     */
    public static Session connect(String host, Integer port, String username, String password) throws JSchException{
        Session session = null;
        // 获取session
        session = new JSch().getSession(username, host, port == null ? 22 : port);
        session.setPassword(password);
        session.setConfig("StrictHostKeyChecking","no");
        // 启动连接
        session.connect();
        return session;
    }

    /**
     * 执行系统命令, 并给定输出内容的处理
     *
     * @param host          主机, 比如 app.topcheer.xyz
     * @param port          端口: 默认 22
     * @param username      用户: 比如 deployer
     * @param password      密码: 比如 Top#123
     * @param command       系统命令: 支持多条, 比如 "pwd;ls -l;echo `env`"
     */
    public static void execCommand(String host, Integer port, String username, String password, String command) {
        execCommand(host, port, username, password, command, logger::info);
    }

    /**
     * 执行系统命令, 并给定输出内容的处理
     *
     * @param host          主机, 比如 app.topcheer.xyz
     * @param port          端口: 默认 22
     * @param username      用户: 比如 deployer
     * @param password      密码: 比如 Top#123
     * @param command       系统命令: 支持多条, 比如 "pwd;ls -l;echo `env`"
     * @param consumer      消费：输出内容的处理方式和错误输出内容的处理方式
     */
    public static void execCommand(String host, Integer port, String username, String password, String command, Consumer<String> consumer) {
        Session session = null;
        ChannelExec exec = null;
        InputStream in = null;
        InputStream err = null;
        try {
            // 根据基本信息来获得SESSION会话
            session = connect(host, port, username, password);
            // 创建一次执行，并把结果一次性返回的Channel
            exec = (ChannelExec)session.openChannel("exec");
            exec.setCommand(command);//设置执行的linux指令
            exec.setInputStream(null);

            // 此处获取输入流与错误输入流必须在ChannelExec建立连接之前获取
            in = exec.getInputStream();
            err = exec.getErrStream();
            exec.connect();

            // 消费输入流
            try(BufferedReader bufr = new BufferedReader(new InputStreamReader(in,StandardCharsets.UTF_8))){
                String out = null;
                while ((out = bufr.readLine()) != null) {
                    logger.info(out);
                    consumer.accept(out);
                }
            } catch (IOException e) {
                logger.error(StackUtil.getStackTrace(e));
            }

            try(BufferedReader bufr = new BufferedReader(new InputStreamReader(err,StandardCharsets.UTF_8))){
                String out = null;
                while ((out = bufr.readLine()) != null) {
                    logger.error(out);
                    consumer.accept(out);
                }
            } catch (IOException e) {
                logger.error(StackUtil.getStackTrace(e));
            }

            //关闭流
            err.close();
            in.close();

        } catch (JSchException|IOException e) {
            throw new HeException(e);
        } finally {
            if (exec != null) exec.disconnect();
            if (session != null) session.disconnect();
        }
    }

    /**
     * 执行系统命令, 并实时输出命令结果。
     * 内部采用默认的输入流消费，实时执行结果，由日志记录。
     *
     * @param host          主机, 比如 app.topcheer.xyz
     * @param port          端口: 默认 22
     * @param username      用户: 比如 deployer
     * @param password      密码: 比如 Top#123
     * @param command       系统命令: 支持多条, 比如 "pwd;ls -l;echo `env`"
     */
    public static void execShell(String host, Integer port, String username, String password, String command){
        execShell(host, port, username, password, command, logger::info);
    }

    /**
     * 执行系统命令, 并实时输出命令结果
     * 提供一个消费，由于采用的是shell的交互模式，正确与错误输出都会通过@param consumer 来支持输入流消费
     *
     * @param host          主机, 比如 app.topcheer.xyz
     * @param port          端口: 默认 22
     * @param username      用户: 比如 deployer
     * @param password      密码: 比如 Top#123
     * @param command       系统命令: 支持多条, 比如 "pwd;ls -l;echo `env`"
     * @param consumer      消费者：输出内容的处理方式
     */
    public static void execShell(String host, Integer port, String username, String password, String command, Consumer<String> consumer){
        Session session    = null;
        ChannelShell shell = null;
        InputStream instream = null;
        OutputStream outstream = null;
        try {
            // 根据基本信息来获得SESSION会话
            session = connect(host, port, username, password);
            // 创建一次执行，并把结果实时返回的Channel
            shell = (ChannelShell) session.openChannel("shell");

            // 此处获取输入流与输出流必须在channelShell建立连接之前获取
            instream = shell.getInputStream();
            outstream = shell.getOutputStream();//shell模式通过输出流将linux指令写入到远程执行终端，是一种交互模式
            shell.connect();

            // 命令写入到shell交互中执行
            outstream.write((command + ENTER + EXIT).getBytes(StandardCharsets.UTF_8));
            outstream.flush();

            // 消费输入流
            try(BufferedReader bufr = new BufferedReader(new InputStreamReader(instream,StandardCharsets.UTF_8))){
                String out = null;
                while ((out = bufr.readLine()) != null) {
                    // 如果因为返回信息太多，返回的是more，没有返回全部，则发送一个空格过去即可
                    if (out.contains("--More--")) {
                        outstream.write((" ").getBytes(StandardCharsets.UTF_8));
                        outstream.flush();
                    }
                    consumer.accept(out);
                }
            } catch (IOException e) {
                logger.error(StackUtil.getStackTrace(e));
            }

            // 关闭流
            instream.close();
            outstream.close();
        } catch (JSchException|IOException e) {
            throw new HeException(e);
        } finally {
            if (shell != null) shell.disconnect();
            if (session != null) session.disconnect();
        }
    }

    /**
     *
     * 往远程服务器上传文件,不修改文件名称
     *
     * @param host              主机, 比如 app.topcheer.xyz
     * @param port              端口: 默认 22
     * @param username          用户: 比如 deployer
     * @param password          密码: 比如 Top#123
     * @param file              文件对象
     * @param targetPath        上传目标路径
     */
    public static void uploadFile(String host, Integer port, String username, String password, File file, String targetPath){
        uploadFile(host, port, username, password, file, file.getName(), targetPath);
    }

    /**
     *
     * 往远程服务器上传文件，修改文件名称
     *
     * @param host              主机, 比如 app.topcheer.xyz
     * @param port              端口: 默认 22
     * @param username          用户: 比如 deployer
     * @param password          密码: 比如 Top#123
     * @param file              文件
     * @param targetFileName    上传后文件命名
     * @param targetPath        上传目标路径
     */
    public static void uploadFile(String host, Integer port, String username, String password, File file, String targetFileName, String targetPath){
        try {
            uploadFile(host, port, username, password, new FileInputStream(file), targetFileName, targetPath);
        } catch (FileNotFoundException e) {
            throw new HeException(e);
        }
    }

    /**
     *
     * 往远程服务器上传文件流，并且重新命名文件
     *
     * @param host              主机, 比如 app.topcheer.xyz
     * @param port              端口: 默认 22
     * @param username          用户: 比如 deployer
     * @param password          密码: 比如 Top#123
     * @param inputStream       文件流
     * @param targetFileName    上传后文件命名
     * @param targetPath        上传目标路径
     */
    public static void uploadFile(String host, Integer port, String username, String password, InputStream inputStream, String targetFileName, String targetPath){
        Session session     = null;
        ChannelSftp sftp    = null;
        try (InputStream inStream = inputStream){
            session = connect(host, port, username, password);          // 获取登录的session会话
            sftp    = (ChannelSftp)session.openChannel("sftp");   // 获取文件传输Channel
            sftp.connect();                                             // 连接
            sftp.cd(targetPath);                                        // 进入到指定的路径位置
            sftp.put(inStream,targetFileName);                          // 上传
        } catch (JSchException | SftpException | IOException e) {
            throw new HeException(e);
        }finally {
            if (sftp != null) sftp.disconnect();
            if (session != null) session.disconnect();
        }
    }

    /**
     * 下载文件
     * @param host              主机, 比如 app.topcheer.xyz
     * @param port              端口: 默认 22
     * @param username          用户: 比如 deployer
     * @param password          密码: 比如 Top#123
     * @param srcFileName       源文件绝对路径
     * @param dstFileName       下载到本地的路径
     */
    public static void downloadFile(String host, Integer port, String username, String password,String srcFileName,String dstFileName){
        Session session     = null;
        ChannelSftp sftp    = null;
        try{
            session = connect(host, port, username, password);                  // 获取登录的session会话
            sftp    = (ChannelSftp)session.openChannel("sftp");           // 获取文件传输Channel
            sftp.connect();                                                     // 连接
            sftp.get(srcFileName,dstFileName);                                  // 下载文件到本地
        } catch (JSchException | SftpException e) {
            throw new HeException(e);
        }finally {
            if (sftp != null) sftp.disconnect();
            if (session != null) session.disconnect();
        }
    }

}
