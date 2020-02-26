package com.hepengju.hekele.data.generator.gen400_custom.gen420_password;

import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.constant.DataConst;
import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.core.annotation.Order;

/**
 * 密码生成器
 *
 * <pre>
 *     1. 英文大写字母 A-Z
 *     2. 英文小写字母 a-z
 *     3. 数字 0-9
 *     4. 特殊字符: ~`!@#$%^&*()_-+=\|]}[{'";:?/<>,.
 *     5. 长度: 6位-16位
 *
 * </pre>
 * 常规要求: 字母(有大小写), 数字, 特殊符号
 */
@ApiModel("密码生成器") @Data @Order(421)
public class PasswordGenerator extends AbstractStringGenerator {

    private int min = 16;
    private int max = 16;

    @Override
    public String generate() {
        String part01 = RandomStringUtils.randomAlphanumeric(min, max);// 随机的字母数字(6-15位)
        String part02 = RandomUtil.randomNum(DataConst.sepecialChar, 0, 2);
        return RandomUtil.randomNum(min, max + 1, part01, part02);
    }
}
