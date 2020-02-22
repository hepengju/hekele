package com.hepengju.hekele.data.meta;

import com.hepengju.hekele.base.util.BeanUtil;
import com.hepengju.hekele.data.annotation.GeneratorAnno;
import com.hepengju.hekele.data.generator.Generator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 前端传入版配置
 *
 * @see GeneratorAnno 注解版配置
 */
@Data @ApiModel("生成器元数据")
public class GeneratorMeta {

    public static int SAMPLE_SIZE = 10;

    @NotNull
    @ApiModelProperty("全类名") private String className;
    @ApiModelProperty("分类") private String type;
    @ApiModelProperty("描述") private String desc;
    @ApiModelProperty("样例") private List<String> sampleData;

    /**
     * columnKey: iview的Table组件, 或其他前端的数据表格组件的key, 用于组装返回数据
     * columnName: 下载Excel表头名称或Insert语句的列名
     */
    @ApiModelProperty("columnKey")  private String columnKey;
    @ApiModelProperty("columnName") private String columnName;
    //-----------------------------------------------------------
    @ApiModelProperty("最小值")   private String  min;       // 最小值, 个人情况下表示字符串的最小长度
    @ApiModelProperty("最大值")   private String  max;       // 最大值, 个别情况下表示字符串的最大长度
    @ApiModelProperty("代码枚举") private String  code;      // 代码值, 逗号分隔
    @ApiModelProperty("代码多选") private String  codeMulti; // 20200210 代码值, 可以选择是否多选 (Y/N)  Boolean类型的前后端传输容易错误
    @ApiModelProperty("格式")    private String  format;    // 20200222 格式化日期和数字
    @ApiModelProperty("前缀")    private String  prefix;    // 20200222 前缀
    @ApiModelProperty("后缀")    private String  suffix;    // 20200222 后缀
    //-----------------------------------------------------------

    public Generator toGenerator(){
        try {
            Generator generator = (Generator)Class.forName(className).newInstance();
            BeanUtil.copyNotBlankProperties(this, generator);
            return generator;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
