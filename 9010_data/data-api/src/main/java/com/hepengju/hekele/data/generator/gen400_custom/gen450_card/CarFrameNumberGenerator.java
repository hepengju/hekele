package com.hepengju.hekele.data.generator.gen400_custom.gen450_card;

import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.core.annotation.Order;

import java.util.*;

/**
 * 车架号生成器
 *
 * <pre>
 *     参考: 于克磊提供的python脚本的生成器
 *      8位随机 + 校验位 + 8位随机
 *
 *      百度百科: 为避免与数字的1、0、9混淆，英文字母“I”、“O”、“Q”不使用，第10位生产型年不使用“I”、“O”、“Q”、“U”、“Z”、“0”。
 * </pre>
 */
@Data @ApiModel("车架号生成器") @Order(452)
public class CarFrameNumberGenerator extends AbstractStringGenerator {

    // 映射 和 加权
    private static Map<String, Integer> carFrameNumIntMap = new HashMap<>();
    private static int[] carFrameWeight = {8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2};

    private static List<String> noUseList01 = Arrays.asList("I", "O", "Q"); // carFrameNumIntMap中直接去掉了
    private static List<String> noUseList02 = Arrays.asList("I", "O", "Q", "U", "Z", "0");

    @Override
    public String generate() {
        // String carFrameNumber = RandomStringUtils.randomAlphanumeric(17).toUpperCase();
        Set<String> keySet = carFrameNumIntMap.keySet();
        String carFrameNumber = RandomUtil.randomNum(keySet.toArray(new String[keySet.size()]), 17);
        keySet.removeAll(noUseList02);

        // 去掉不使用的
        char[] chars = carFrameNumber.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (noUseList02.contains(String.valueOf(chars[i]))) {
                chars[i] = RandomUtil.randomNum(keySet.toArray(new String[keySet.size()]), 1).toCharArray()[0];
            }
        }

        // 计算校验位
        int weightTotal = 0;
        for (int i = 0; i < chars.length; i++) {
            int intMatch = carFrameNumIntMap.get(String.valueOf(chars[i]));
            int intWeight = carFrameWeight[i];
            weightTotal += intMatch * intWeight;
        }
        int remainder = weightTotal % 11;
        String checkChar = (remainder == 10 ? "X" : String.valueOf(remainder));

        // 拼接
        StringBuilder sb = new StringBuilder(17);
        for (int i = 0; i < chars.length; i++) {
            sb.append(i == 8 ? checkChar : chars[i]);
        }
        return sb.toString();
    }

    static {

        carFrameNumIntMap.put("A", 1);
        carFrameNumIntMap.put("B", 2);
        carFrameNumIntMap.put("C", 3);
        carFrameNumIntMap.put("D", 4);
        carFrameNumIntMap.put("E", 5);
        carFrameNumIntMap.put("F", 6);
        carFrameNumIntMap.put("G", 7);
        carFrameNumIntMap.put("H", 8);
        //carFrameNumIntMap.put("I", 0);
        carFrameNumIntMap.put("J", 1);
        carFrameNumIntMap.put("K", 2);
        carFrameNumIntMap.put("L", 3);
        carFrameNumIntMap.put("M", 4);
        carFrameNumIntMap.put("N", 5);
        //carFrameNumIntMap.put("O", 0);
        carFrameNumIntMap.put("P", 7);
        //carFrameNumIntMap.put("Q", 0);
        carFrameNumIntMap.put("R", 9);
        carFrameNumIntMap.put("S", 2);
        carFrameNumIntMap.put("T", 3);
        carFrameNumIntMap.put("U", 4);
        carFrameNumIntMap.put("V", 5);
        carFrameNumIntMap.put("W", 6);
        carFrameNumIntMap.put("X", 7);
        carFrameNumIntMap.put("Y", 8);
        carFrameNumIntMap.put("Z", 9);

        carFrameNumIntMap.put("0", 0);
        carFrameNumIntMap.put("1", 1);
        carFrameNumIntMap.put("2", 2);
        carFrameNumIntMap.put("3", 3);
        carFrameNumIntMap.put("4", 4);
        carFrameNumIntMap.put("5", 5);
        carFrameNumIntMap.put("6", 6);
        carFrameNumIntMap.put("7", 7);
        carFrameNumIntMap.put("8", 8);
        carFrameNumIntMap.put("9", 9);
    }

}
