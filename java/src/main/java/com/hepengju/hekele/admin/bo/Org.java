package com.hepengju.hekele.admin.bo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hepengju.hekele.base.annotation.Code;
import com.hepengju.hekele.base.constant.HeConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Auto Created By ExcelVBA
 *
 * @author 何鹏举 2019-12-22
 */
@Data
@ApiModel("部门表")
@TableName("Z10_ORG")
public class Org {

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("部门主键")
    private String orgId;

    @ApiModelProperty("父部门主键")
    private String parentId;

    @ApiModelProperty("部门代码")
    private String orgCode;

    @ApiModelProperty("部门名称")
    private String orgName;

    @Code
    @ApiModelProperty("部门类型(0-公司,1-一级部门, 2-二级部门)")
    private String orgType;

    @ApiModelProperty("负责人主键")
    private String leaderId;

    @ApiModelProperty("负责人账号")
    private String leaderCode;

    @ApiModelProperty("负责人名称")
    private String leaderName;

    @ApiModelProperty("部门描述")
    private String orgDesc;

    @ApiModelProperty("排序顺序")
    private Integer orgSort;

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

    @Code
    @ApiModelProperty("启用状态(0-停用, 1-启用)")
    private String enableFlag;

    @TableLogic  @JsonIgnore
    @ApiModelProperty("删除标志(0-已删除,1-未删除)")
    private String deleteFlag;

}