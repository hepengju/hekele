package com.hepengju.hekele.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hepengju.hekele.base.util.SSHUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.leftPad;
import static org.apache.commons.lang3.StringUtils.rightPad;

public class SpringMonitorTest {
    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");
    private RestTemplate restTemplate;

    {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(2000);
        restTemplate = new RestTemplate(requestFactory);
    }

    // private String eurekaHost = "http://10.10.128.101:8763";  // 开发
    // private String eurekaHost = "http://10.10.128.121:8763";  // 类生产
    private String eurekaHost = "http://10.10.121.136:8763";  // 生产

    //  private String host01 = "10.10.128.121";
    //  private String host02 = "10.10.128.122";
    private String host01 = "10.10.121.136";
    private String host02 = "10.10.121.137";

    @Test
    public void printAll(){
        printLinux();
        printDocker();
        printlnJava();
    }


    @Test
    public void printDocker(){
        List<String> lineList = new ArrayList<>();
        SSHUtil.execCommand(host02,22,"root","CT!QAZ2wsx", "docker stats --no-stream --format \"table {{.Name}}\\t{{.CPUPerc}}\\t{{.MemUsage}}\"", lineList::add);
        lineList.remove(0); // 去掉标题行
        for (String line : lineList) {
            List<String> eleList = Arrays.stream(line.split(" ")).filter(StringUtils::isNoneBlank).collect(Collectors.toList());
            System.err.println(rightPad(eleList.get(0), 30) + rightPad("内存占用: ", 46) + eleList.get(2) + "M");
        }
    }

    @Test
    public void printLinux(){
        LinuxMonitor lm01 = getLinuxMonitor(host01);
        LinuxMonitor lm02 = getLinuxMonitor(host02);
        System.err.println(rightPad(lm01.getHost(), 30) + rightPad("磁盘剩余: ", 46) + lm01.getDiskRemain());
        System.err.println(rightPad(lm02.getHost(), 30) + rightPad("磁盘剩余: ", 46) + lm02.getDiskRemain());
        System.err.println(rightPad(lm01.getHost(), 30) + rightPad("内存Free: ", 48) + lm01.getMemFree());
        System.err.println(rightPad(lm01.getHost(), 30) + rightPad("内存Cache: ", 48) + lm01.getMemCache());
        System.err.println(rightPad(lm02.getHost(), 30) + rightPad("内存Free: ", 48) + lm02.getMemFree());
        System.err.println(rightPad(lm02.getHost(), 30) + rightPad("内存Cache: ", 48) + lm02.getMemCache());
    }

    @Test
    public void printlnJava(){

        // 1. 通过eureka拿到所有实例信息
        List<InstanceMonitor> imList = getInstanceList(eurekaHost);

        // 2. 每个实例信息调用actuator的接口获取到jvm的使用信息, 过滤到取不到的, 并进行排序
        imList.forEach(this::setJvmMonitor);
        List<InstanceMonitor> nonNullImList = imList.stream().filter(m -> m.getJvmUsed() != null).collect(Collectors.toList());
        nonNullImList.sort(Comparator.comparing(InstanceMonitor::getName).thenComparing(InstanceMonitor::getInstanceId));

        // 按照合理的格式输出
        for (InstanceMonitor monitor : nonNullImList) {
            System.err.println(rightPad(monitor.getName(), 30)
                    + rightPad(monitor.getInstanceId(), 50)
                    + leftPad(monitor.getThreadsLive().toString(),3)
                    + " # "
                    + "" + (monitor.getJvmUsed() + " / " + monitor.getJvmSize())
            );
        }
    }

    @Test
    public LinuxMonitor getLinuxMonitor(String host){
        List<String> lineList = new ArrayList<>();
        SSHUtil.execCommand(host,22,"root","CT!QAZ2wsx", "df -h;free -h", lineList::add);

        String diskRemain = Arrays.stream(lineList.get(1).split(" "))
                .filter(StringUtils::isNoneBlank).collect(Collectors.toList()).get(3); // 第二行为挂载目录为/的容量大小

        List<String> memList = Arrays.stream(lineList.get(lineList.size() - 2).split(" ")).filter(StringUtils::isNoneBlank).collect(Collectors.toList());
        String free = memList.get(3);
        String buffCache = memList.get(5);

        LinuxMonitor lm = new LinuxMonitor();
        lm.setHost(host);
        lm.setDiskRemain(diskRemain);
        lm.setMemFree(free);
        lm.setMemCache(buffCache);

        return lm;
    }

    private String getFormatSize(Long jvmBytes) {
        Long sizeM = jvmBytes / 1024 / 1024;
        if (sizeM < 1000) return sizeM.toString() + "M";

        double sizeG = sizeM / 1024.0;
        return decimalFormat.format(sizeG) + "G";
    }

    private void setJvmMonitor(InstanceMonitor im){
        try {
            ResponseEntity<String> entity1 = restTemplate.getForEntity("http://" + im.getInstanceId() + "/actuator/metrics/jvm.memory.used", String.class);
            Long jvmUsed = JSON.parseObject(entity1.getBody()).getJSONArray("measurements").getJSONObject(0).getLong("value");

            ResponseEntity<String> entity2 = restTemplate.getForEntity("http://" + im.getInstanceId() + "/actuator/metrics/jvm.memory.committed", String.class);
            Long jvmSize = JSON.parseObject(entity2.getBody()).getJSONArray("measurements").getJSONObject(0).getLong("value");

            ResponseEntity<String> entity3 = restTemplate.getForEntity("http://" + im.getInstanceId() + "/actuator/metrics/jvm.threads.live", String.class);
            Integer threadsLive = JSON.parseObject(entity3.getBody()).getJSONArray("measurements").getJSONObject(0).getInteger("value");

            im.setJvmUsed(getFormatSize(jvmUsed));
            im.setJvmSize(getFormatSize(jvmSize));
            im.setThreadsLive(threadsLive);

        } catch (Exception e) {
            System.err.println(im.instanceId + " 获取JVM内存失败, 原因是: " + e.getMessage());
        }
    }

    private List<InstanceMonitor> getInstanceList(String eurekaHost) {
        List<InstanceMonitor> imList = new ArrayList<>();
        ResponseEntity<String> entity = restTemplate.getForEntity(eurekaHost + "/eureka/apps", String.class);
        JSONObject resJsonObject = JSON.parseObject(entity.getBody());
        JSONArray appArray = resJsonObject.getJSONObject("applications").getJSONArray("application");

        for (int i = 0; i < appArray.size(); i++) {
            JSONObject app = appArray.getJSONObject(i);
            JSONArray instanceArr = app.getJSONArray("instance");

            for (int j = 0; j < instanceArr.size(); j++) {
                JSONObject instance = instanceArr.getJSONObject(j);
                InstanceMonitor monitor = new InstanceMonitor();
                monitor.setName(instance.getString("app"));

                String instanceId = instance.getString("instanceId");
                // 注意context-path
                try {
                    String metadataContextPath = instance.getJSONObject("metadata").getString("management.context-path");
                    String contextPath = metadataContextPath.replace("actuator", "").replace("/", "");
                    instanceId = instanceId + "/" + contextPath;
                } catch (Exception e) {
                }

                monitor.setInstanceId(instanceId.endsWith("/") ? instanceId.substring(0, instanceId.length() - 1) : instanceId);

                imList.add(monitor);
            }
        }

        return imList;
    }

    @Data
    static class LinuxMonitor {
        private String host;
        private String diskRemain;
        private String memFree;
        private String memCache;
    }

    @Data
    static class InstanceMonitor {
        private String name;
        private String instanceId;
        private String jvmUsed;
        private String jvmSize;

        // 20200309 追加活动线程数
        private Integer threadsLive;
    }
}
