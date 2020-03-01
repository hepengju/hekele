package com.hepengju.hekele.data.meta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hepengju.hekele.base.util.BeanUtil;
import com.hepengju.hekele.data.annotation.GeneratorAnno;
import com.hepengju.hekele.data.generator.Generator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 前端传入版配置
 *
 * @see GeneratorAnno 注解版配置
 */
@Data @ApiModel("生成器元数据")
public class GeneratorMeta {

    public static int SAMPLE_SIZE = 10;

    @JsonIgnore // 不暴露全类名, 接口只需要提供: chineseName 或 中文姓名 即可
    @ApiModelProperty(value = "全类名", hidden = true)
    private String className;

    @ApiModelProperty(value = "分类", example = "string")
    private String type;

    @ApiModelProperty(value = "名称: 取指定的生成器", example = "chineseName")
    private String name;

    @ApiModelProperty("样例")
    private List<String> sampleData;

    /**
     * columnKey: iview的Table组件, 或其他前端的数据表格组件的key, 用于组装返回数据
     */
    @ApiModelProperty(value = "列唯一键: iview的Table组件的Columns中的key, 前端生成, 默认为空", example = "col0001")
    private String columnKey;

    @ApiModelProperty(value = "列中文标题: iview的Table组件的Columns中的Name, 用于前端显示和Excel文件的标题, 默认为后端生成器的中文名称"
            , example = "出生日期")
    private String columnTitle;

    @ApiModelProperty(value = "列英文代码: 用于生成Insert语句的列名称, 默认为后端生成器的英文名称(同name)", example = "birth")
    private String columnName;
    //-----------------------------------------------------------
    @ApiModelProperty(value = "最小值", example = "60")
    private String  min;

    @ApiModelProperty(value = "最大值", example = "100")
    private String  max;

    @ApiModelProperty(value = "代码枚举", example = "高,中,低")
    private String  code;

    @ApiModelProperty(value = "代码多选", example = "N")      // 20200210 代码值, 可以选择是否多选 (Y/N)  Boolean类型的前后端传输容易错误
    private boolean codeMulti;

    @ApiModelProperty(value = "格式", example = "yyyyMMdd")  // 20200222 格式化日期和数字
    private String  format;

    @ApiModelProperty(value = "前缀", example = "ORDER-")    // 20200222 前缀
    private String  prefix;

    @ApiModelProperty(value = "后缀", example = "S00")       // 20200222 后缀
    private String  suffix;
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
