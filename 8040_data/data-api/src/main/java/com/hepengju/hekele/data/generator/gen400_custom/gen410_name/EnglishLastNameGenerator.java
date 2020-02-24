package com.hepengju.hekele.data.generator.gen400_custom.gen410_name;


import com.hepengju.hekele.data.constant.DataConst;
import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.RandomUtils;

@ApiModel("英文名生成器") @Data
public class EnglishLastNameGenerator extends AbstractStringGenerator {

    private String[] lastNameArray = DataConst.lastNames;

    @Override
    public String generate() {
        return lastNameArray[RandomUtils.nextInt(0, lastNameArray.length)];
    }


}
