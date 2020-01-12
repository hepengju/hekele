package com.hepengju.hekele.data.generator.string.password;

import com.hepengju.hekele.data.generator.StringGenerator;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * md5密码生成器
 *
 * @author hepengju
 */
public class Sha256Generator implements StringGenerator {

    @Override
    public String generate() {
        String password = RandomStringUtils.randomAlphabetic(6, 10);
        return DigestUtils.sha256Hex(password);
    }

}