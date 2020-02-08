package com.hepengju.hekele.data.generator.string;

import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.StringGenerator;
import com.hepengju.hekele.data.generator.constant.DataConst;
import io.swagger.annotations.ApiModel;

@ApiModel("随机数字字符生成器")
public class RandomNumberGenerator  implements StringGenerator {

    private int min = 6;
    private int max = 10;

    @Override
    public String generate() {
        return RandomUtil.randomNum(DataConst.numbers, min, max + 1);
    }

}
