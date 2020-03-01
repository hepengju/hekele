package com.hepengju.hekele.data.param;

import com.hepengju.hekele.data.meta.GeneratorMeta;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("接口参数的封装") @Data
public class GeneratorParam {

    @ApiModelProperty("生成器元数据数组-必传")
    private List<GeneratorMeta> metaList;

    @ApiModelProperty(value = "样例数量-可选", example = "10")
    private int sampleSize = 10;

    @ApiModelProperty(value = "文件格式-可选", example = "excel")
    private String fileFormat = "csv";

    @ApiModelProperty(value = "文件名称-可选", example = "样例数据")
    private String fileName = "样例数据";

    @ApiModelProperty(value = "表名称-可选", example = "data")
    private String tableName = "data";

}
