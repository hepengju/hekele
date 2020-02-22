package com.hepengju.hekele.data.param;

import com.hepengju.hekele.data.meta.GeneratorMeta;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@ApiModel("接口参数的封装") @Data
public class GeneratorParam {

    @ApiModelProperty("生成器元数据数组-必传")
    private List<GeneratorMeta> generatorMetaList;

    @ApiModelProperty("样例数量-可选")
    private int sampleSize = 10;

    @ApiModelProperty("文件格式-可选")
    private String fileFormat = "csv";

    @ApiModelProperty("表名称-可选")
    private String tableName = "data";

}
