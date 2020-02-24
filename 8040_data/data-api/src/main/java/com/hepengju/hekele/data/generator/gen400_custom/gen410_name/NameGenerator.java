package com.hepengju.hekele.data.generator.gen400_custom.gen410_name;

import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

@ApiModel("姓名生成器") @Order(411)
public class NameGenerator extends AbstractStringGenerator {

    private ChineseNameGenerator cnName = new ChineseNameGenerator();
    private EnglishNameGenerator enName  = new EnglishNameGenerator();

    @Override
    public String generate() {
        double chance01 = Math.random();
        return chance01 < 0.2 ? enName.generate() : cnName.generate();
    }
}
