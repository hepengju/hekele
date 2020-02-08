package com.hepengju.hekele.data.generator.custom.password;

import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;
import com.hepengju.hekele.data.generator.constant.DataConst;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.RandomStringUtils;

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
@ApiModel("密码生成器")
public class PasswordGenerator implements StringGenerator {

    @Override
    public String generate() {
        String part01 = RandomStringUtils.randomAlphanumeric(6, 16);// 随机的字母数字(6-15位)
        String part02 = RandomUtil.randomNum(DataConst.sepecialChar, 0, 2);
        return RandomUtil.randomNum(6, 17, part01, part02);
    }
}
