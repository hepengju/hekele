package com.hepengju.hekele.data.generator.gen400_custom.gen410_name;

import com.hepengju.hekele.base.util.PinyinUtil;
import com.hepengju.hekele.data.generator.AbstractStringGenerator;
import io.swagger.annotations.ApiModel;
import org.springframework.core.annotation.Order;

@ApiModel("中文姓名生成器") @Order(412)
public class ChineseNamePinyinGenerator extends AbstractStringGenerator {

    private ChineseFirstNameGenerator firstName = new ChineseFirstNameGenerator();
    private ChineseLastNameGenerator  lastName  = new ChineseLastNameGenerator();
    @Override
    public String generate() {
        return PinyinUtil.toPinyin(firstName.generate()) + " " + PinyinUtil.toPinyin(lastName.generate());
    }
}
