package com.hepengju.hekele.data.meta;

import com.hepengju.hekele.data.generator.Generator;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 生成器的配置值
 */
@Data
public class MetaGenerator {

    public static int SAMPLE_SIZE = 10;

    @NotNull
    private String className;
    private String desc;
    private String type;
    private List<Object> sampleData;
    //-----------------------------------------------------------
    private String  min;       // 最小值, 个人情况下表示字符串的最小长度
    private String  max;       // 最大值, 个别情况下表示字符串的最大长度
    private Integer scale;     // 保留小数位数
    private String  code;      // 代码值, 逗号分隔
    private String  codeMulti; // 20200210 代码值, 可以选择是否多选 (Y/N)  Boolean类型的前后端传输容易错误
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
