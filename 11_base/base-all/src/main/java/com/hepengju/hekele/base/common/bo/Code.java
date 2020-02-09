package com.hepengju.hekele.base.common.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@ApiModel("代码")
@TableName("Z00_CODE")
public class Code {

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("主键")
    private String codeId;

    @NotBlank @Length(max = 64)
    @ApiModelProperty("系统代码(admin/file…, 展示用)")
    private String sysCode;

    @NotBlank @Length(max = 64)
    @ApiModelProperty("类型代码")
    private String typeCode;

    @NotBlank @Length(max = 64)
    @ApiModelProperty("类型名称")
    private String typeName;

    @NotBlank @Length(max = 64)
    @ApiModelProperty("条目代码")
    private String itemCode;

    @NotBlank @Length(max = 64)
    @ApiModelProperty("条目名称")
    private String itemName;

    @ApiModelProperty("条目排序")
    private Integer itemSort;

    @ApiModelProperty("扩展属性1")
    private String attr1;

    @ApiModelProperty("扩展属性2")
    private String attr2;

    @ApiModelProperty("扩展属性3")
    private String attr3;

    @ApiModelProperty("扩展属性4")
    private String attr4;

    @ApiModelProperty("扩展属性5")
    private String attr5;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = HeConst.DATE_TIME_FORMAT, timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("创建人员账号")
    private String createUserCode;

    @ApiModelProperty("创建人员名称")
    private String createUserName;

    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = HeConst.DATE_TIME_FORMAT, timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty("更新人员账号")
    private String updateUserCode;

    @ApiModelProperty("更新人员名称")
    private String updateUserName;

    @com.hepengju.hekele.base.annotation.Code
    @ApiModelProperty("启用状态(0-停用, 1-启用)")
    private String enableFlag;

    @TableLogic  @JsonIgnore
    @ApiModelProperty("删除标志(0-已删除,1-未删除)")
    private String deleteFlag;

}