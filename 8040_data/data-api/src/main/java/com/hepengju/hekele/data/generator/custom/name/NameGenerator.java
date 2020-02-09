package com.hepengju.hekele.data.generator.custom.name;

import com.hepengju.hekele.data.generator.StringGenerator;
import io.swagger.annotations.ApiModel;

@ApiModel("姓名生成器")
public class NameGenerator implements StringGenerator {

    private ChineseNameGenerator cnName = new ChineseNameGenerator();
    private EnglishNameGenerator enName  = new EnglishNameGenerator();

    @Override
    public String generate() {
        double chance01 = Math.random();
        return chance01 < 0.2 ? enName.generate() : cnName.generate();
    }
}
