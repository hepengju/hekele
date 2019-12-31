package com.hepengju.hekele.admin.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hepengju.hekele.base.constant.HeConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Auto Created By ExcelVBA
 *
 * @author 何鹏举 2019-12-27
 */
@Data
@ApiModel("序列")
@TableName("Z00_SEQ")
public class Seq {

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("主键")
    private String seqId;

    @NotBlank @Length(max = 64)
    @ApiModelProperty("序列名称")
    private String seqName;

    @ApiModelProperty("序列日期")
    private String seqDate;

    @ApiModelProperty("序列值")
    private int seqValue;

    @ApiModelProperty("序列描述")
    private String seqDesc;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = HeConst.DATE_TIME_FORMAT, timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = HeConst.DATE_TIME_FORMAT, timezone = "GMT+8")
    private Date updateTime;

}