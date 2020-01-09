package com.hepengju.hekele.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class EurekaTest {

    private RestTemplate restTemplate;
    private String eurekaHost = "http://10.10.128.101:8763";
    private DecimalFormat decimalFormat = new DecimalFormat("#0.00");

    {
        // 设置1s超时
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(2000);
        restTemplate=new RestTemplate(requestFactory);
    }

    @Test
    public void testApps(){

        // 1. 通过eureka拿到所有实例信息
        List<InstanceMonitor> imList = getInstanceList(eurekaHost);

        // 2. 每个实例信息调用actuator的接口获取到jvm的使用信息, 过滤到取不到的, 并进行排序
        imList.forEach(this::setJvmMonitor);
        List<InstanceMonitor> nonNullImList = imList.stream().filter(m -> m.getJvmUsed() != null).collect(Collectors.toList());
        nonNullImList.sort(Comparator.comparing(InstanceMonitor::getName).thenComparing(InstanceMonitor::getInstanceId));

        // 按照合理的格式输出
        for (InstanceMonitor monitor : nonNullImList) {
            System.out.println(StringUtils.rightPad(monitor.getName(), 30)
                    + StringUtils.rightPad(monitor.getInstanceId(), 50)
                    + monitor.getJvmUsed() + " / " + monitor.getJvmSize());
        }
    }

    @Test
    public void testNumber(){
        long size = 2790014976L;
        long sizeM  = size/1024/1024;
        System.out.println(sizeM);

        if(sizeM > 1000){
            double sizeG = sizeM / 1024.0;
            System.out.println(decimalFormat.format(sizeG) + "G");
        }
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

            im.setJvmUsed(getFormatSize(jvmUsed));
            im.setJvmSize(getFormatSize(jvmSize));
        } catch (Exception e) {
            log.error(im.instanceId + " 获取JVM内存失败, 原因是: " + e.getMessage());
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
    static class InstanceMonitor {
        private String name;
        private String instanceId;
        private String jvmUsed;
        private String jvmSize;
    }
}
