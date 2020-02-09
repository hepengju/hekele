package com.hepengju.hekele.data.generator.custom.name;


import com.hepengju.hekele.data.generator.StringGenerator;
import com.hepengju.hekele.data.generator.constant.DataConst;
import lombok.Data;
import org.apache.commons.lang3.RandomUtils;

@Data
public class EnglishLastNameGenerator implements StringGenerator {

    private String[] lastNameArray = DataConst.lastNames;

    @Override
    public String generate() {
        return lastNameArray[RandomUtils.nextInt(0, lastNameArray.length)];
    }


}
