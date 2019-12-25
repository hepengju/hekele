package com.hepengju.hekele.base.core;

import com.hepengju.hekele.base.constant.HeConst;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 中文提示信息均提取到文件
 *
 * @author hepengju 2018-02-05
 */
@Slf4j
public class M {

    private static final String MESSAGE_NAME = "classpath:he.message";

    // 由于SpringBoot项目, 配置文件在jar包里面, 不能动态修改文件了, 此处加入get/set用于前端界面动态修改(重启后失效)
    @Getter @Setter
    private static Map<String, HeMessage> keyHeMessageMap;

    static {
        try {
            List<Integer>   codeList = new ArrayList<>();
            List<String>    keyList  = new ArrayList<>();
            List<HeMessage> hmList   = new ArrayList<>();

            File file = ResourceUtils.getFile(MESSAGE_NAME);
            List<String> lineList = Files.readAllLines(file.toPath());
            for (String line : lineList) {
                if (line.startsWith("#") || "".equals(line.trim())) continue;     //#号注释处理
                try {
                    String[] arr = line.split(":");
                    Integer code = Integer.parseInt(arr[0].trim());
                    String key = arr[1].trim();
                    String value = arr[2].trim();

                    if (codeList.contains(code)) throw new RuntimeException("file [" + MESSAGE_NAME + "] code repeat: " + line);
                    if (keyList .contains(code)) throw new RuntimeException("file [" + MESSAGE_NAME + "] key  repeat: " + line);

                    codeList.add(code);
                    keyList.add(key);
                    hmList.add(new HeMessage(code, key, value));
                } catch (Exception e) {
                    throw new RuntimeException("file [" + MESSAGE_NAME + "] line format error: " + line);
                }
            }

            keyHeMessageMap = hmList.stream().collect(Collectors.toMap(HeMessage::getKey, Function.identity(), (e1, e2) -> e1, LinkedHashMap::new));
        } catch (IOException e) {
            throw new RuntimeException("load file [" + MESSAGE_NAME + "] exception: ", e);
        }
    }

    /**
     * 获取: 参数传入多少都不能报错
     */
    public static String get(String key, Object... args) {
        String value = Optional.ofNullable(keyHeMessageMap.get(key))
                .map(HeMessage::getValue)
                .orElse(null);
        if (value == null) return "";

        //配置文本
        if (args != null) {
            int length = args.length;         //参数个数
            while (length > 0) {              //参数个数大于零时,逐个减少参数直到成功
                try {
                    return String.format(value, args);
                } catch (Exception e) {
                    length--;
                    if (length == 0) return value;
                    args = Arrays.copyOf(args, length);
                }
            }
        }
        return value;
    }

    public static R getErrR(String key, Object... args) {
        String value = get(key, args);
        Integer code = Optional.ofNullable(keyHeMessageMap.get(key)).map(HeMessage::getCode).orElse(HeConst.MCode.UNKNOWN_ERROR);
        return R.err(value).setErrcode(code);
    }

    /**
     * 打印输出给客户方(CSV)
     */
    public static void printCSVFormat() {
        keyHeMessageMap.forEach((key, hm) -> {
            System.out.printf("%d,%s,%s\n", hm.getCode(), hm.getKey(), hm.getValue());
        });
    }

    @Data @AllArgsConstructor @NoArgsConstructor
    static class HeMessage {
        private Integer code;
        private String  key;
        private String  value;
    }

}

// 旧版本
///**
// * 中文提示信息均提取到文件
// *
// * <br>
// * <br> 需求
// * <br> 1. 可以方便的修改提示信息
// * <br> 2. 需要实现通过配置可以热加载(不用重启系统)
// * <br> 3. 启动校验key值的重复,不停机,记录错误日志
// * <br> 4. 集成String.format功能,并保证不传参数时不报错
// * <br>
// * <br> 文件格式采用message,而没有采用properties的考虑
// * <br> 1. properties文件中的中文编译后为\\uXXXX, 修改某个提示, 需要先开发环境修改好文件, 再替换到生产环境, 比较麻烦
// * <br> 2. properties文件中key值重复时,默认会自动覆盖之前的,不便于校验
// * <br>
// * <br>
// * <br> 日期: 20180507
// * <br> 变更: 讨论考虑追加错误代码,同时修正此类
// * <br>
// *
// * @author hepengju 2018-02-05
// */
//public class M {
//
//    private static Logger logger = LoggerFactory.getLogger(M.class);
//
//    //属性配置文件的名称
//    private static final String PROPERTIES_NAME = "classpath:topcheer.message";
//    private static File file;
//    private static Long lastModifyTime;
//
//    //静态代码块取得配置信息 --> 20190616 加入负号的正则, 错误代码前面加入负号表示前端提示用信息提示而不是错误提示
//    //private static final String REG_ERRCODE = "\\{\\{(\\d+)\\}\\}";
//    private static final String REG_ERRCODE = "\\{\\{(-?\\d+)}}";  // Idea提示对于-和}前面加的\\是冗余的转义,实测去掉也可以
//
//    private static Map<String,Integer> errcodeMap;
//    private static Map<String,String> errmsgMap;
//    static {
//        loadProperties();
//        reloadProperties();
//    }
//
//    //重新加载属性
//    private static boolean reload;
//    private static int reloadSeconds;
//
//    //定时器
//    private static Timer timer;
//
//    /**
//     * 取得配置信息
//     */
//    private static synchronized void loadProperties() {
//        //注意: 这种方式对于路径含有空格的会转换为%20,导致下面Files.readAllLines方法报错无法找到这个文件
//        //file = new File(Thread.currentThread().getContextClassLoader().getResource(PROPERTIES_NAME).getFile());
//        try {
//            file = ResourceUtils.getFile(PROPERTIES_NAME);
//            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
//            Map<String,Integer> tempErrcodeMap = new HashMap<>();
//            Map<String,String> tempErrmsgMap = new HashMap<>();
//
//            lines.forEach(line -> {
//                if(line.startsWith("#")) return;     //#号注释处理
//                int position = line.indexOf('=');
//                if (position < 1) return;            //没有等号的行忽略
//
//                //20190516 加入trim
//                String key = line.substring(0, position).trim();
//                String value = line.substring(position + 1).trim();
//
//                //20180507 value值中追加错误代码 {{errcode}}errmsg
//                int errcode = -1;
//                Pattern pattern = Pattern.compile(REG_ERRCODE);
//                Matcher matcher = pattern.matcher(value);
//                if(matcher.find()) {
//                    String errcodeStr = matcher.group(1).trim();            //20190516 加入trim
//                    errcode = NumberUtils.toInt(errcodeStr,-1);
//                }
//
//                String errmsg = value.replaceFirst(REG_ERRCODE, "").trim(); //20190516 加入trim
//                //判断是否已经存在,存在的打印错误日志
//                if (tempErrcodeMap.containsKey(key)) {
//                    logger.error("{}: preValue [{}] of key [{}] already exists, this value [{}] will be ingored ",
//                            PROPERTIES_NAME, tempErrcodeMap.get(key), key, value);
//                } else {
//                    tempErrcodeMap.put(key, errcode);
//                    tempErrmsgMap.put(key, errmsg);
//                }
//
//            });
//
//            //取得所有属性值后,修改map指向新的map
//            errcodeMap = tempErrcodeMap;
//            errmsgMap = tempErrmsgMap;
//            logger.info("{}load properties: {}", Objects.isNull(lastModifyTime) ? "" : "re",PROPERTIES_NAME);
//            lastModifyTime = file.lastModified();
//        } catch (IOException e) {
//            logger.error("load properties: {} failure ",PROPERTIES_NAME);
//        }
//    }
//
//
//
//    /**
//     * 定时任务重载配置: 配置为重载,且文件的最后修改时间与保存的不一致,则重载属性
//     */
//    private static void reloadProperties() {
//        reload = PropUtil.getBoolean("topcheer.message.reload", true);
//        if(reload) {
//            reloadSeconds = PropUtil.getInteger("topcheer.message.reloadSeconds", 300);
//            timer = new Timer();
//            timer.schedule(new TimerTask() {
//                public void run() {
//                    if (file.lastModified() != lastModifyTime) loadProperties();
//                }
//            }, reloadSeconds * 1000L, reloadSeconds * 1000L);
//        }
//    }
//
//    /**
//     * 取消Timer
//     */
//    public static void cancelTimer() {
//        if(timer != null) timer.cancel();
//    }
//
//    /**
//     * 获取: 参数传入多少都不能报错
//     */
//    public static String get(String key,Object... args) {
//        String confMsg = errmsgMap.get(key);    //配置文本
//        if(args != null) {
//            int length = args.length;         //参数个数
//            while(length > 0) {               //参数个数大于零时,逐个减少参数直到成功
//                try {
//                    return String.format(confMsg, args);
//                }catch(Exception e) {
//                    length--;
//                    if(length == 0) return confMsg;
//                    args = Arrays.copyOf(args, length);
//                }
//            }
//        }
//        return confMsg;
//    }
//
//    /**
//     * 获取(不带变参): 因为带变参的key,每次写代码提示都需要删除后面的args,非常烦人
//     */
//    public static String key(String key) {
//        return get(key);
//    }
//
//    /**
//     * 取得错误代码
//     */
//    public static int getCode(String key) {
//        return errcodeMap.get(key) == null ? -1 : errcodeMap.get(key);
//    }
//
//    public static R getErrR(String key,Object... args) {
//        return R.err(get(key, args)).setErrCode(getCode(key));
//    }
//
//    public static R keyErrR(String key) {
//        return getErrR(key);
//    }
//
//    public static R getOkR(String key,Object... args) {
//        return R.ok().setErrMsg(get(key, args));
//    }
//
//    public static R keyOkR(String key) {
//        return getOkR(key);
//    }
//
//    public static Map<String, Integer> getErrcodeMap() {
//        return errcodeMap;
//    }
//
//    public static Map<String, String> getErrmsgMap() {
//        return errmsgMap;
//    }
//}