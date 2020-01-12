package com.hepengju.hekele.data.generator.string.name;

import com.hepengju.hekele.data.generator.StringGenerator;

/**
 * 姓名生成器
 */
public class NameGenerator implements StringGenerator {

    private ChineseNameGenerator cnName = new ChineseNameGenerator();
    private EnglishNameGenerator enName  = new EnglishNameGenerator();

    @Override
    public String generate() {
        double chance01 = Math.random();
        return chance01 < 0.2 ? enName.generate() : cnName.generate();
    }
}
