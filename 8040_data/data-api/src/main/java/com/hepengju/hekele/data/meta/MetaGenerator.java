package com.hepengju.hekele.data.meta;

import com.hepengju.hekele.data.generator.Generator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 生成器的配置值
 */
@Data @ApiModel("生成器元数据")
public class MetaGenerator {

    public static int SAMPLE_SIZE = 10;

    @NotNull
    @ApiModelProperty("全类名") private String className;
    @ApiModelProperty("描述") private String desc;
    @ApiModelProperty("分类") private String type;
    @ApiModelProperty("样例") private List<Object> sampleData;
    //-----------------------------------------------------------
    @ApiModelProperty("最小值")   private String  min;       // 最小值, 个人情况下表示字符串的最小长度
    @ApiModelProperty("最大值")   private String  max;       // 最大值, 个别情况下表示字符串的最大长度
    @ApiModelProperty("小数位数") private Integer scale;     // 保留小数位数
    @ApiModelProperty("代码枚举") private String  code;      // 代码值, 逗号分隔
    @ApiModelProperty("代码多选") private String  codeMulti; // 20200210 代码值, 可以选择是否多选 (Y/N)  Boolean类型的前后端传输容易错误
    //-----------------------------------------------------------

    public Generator toGenerator(){
        try {
            Generator generator = (Generator)Class.forName(className).newInstance();
            if(min       != null) try {BeanUtils.copyProperty(generator, "min"      , min      );} catch (Exception e){};
            if(max       != null) try {BeanUtils.copyProperty(generator, "max"      , max      );} catch (Exception e){}
            if(code      != null) try {BeanUtils.copyProperty(generator, "code"     , code     );} catch (Exception e){}
            if(scale     != null) try {BeanUtils.copyProperty(generator, "scale"    , scale    );} catch (Exception e){}
            if(codeMulti != null) try {BeanUtils.copyProperty(generator, "codeMulti", codeMulti);} catch (Exception e){}
            return generator;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
