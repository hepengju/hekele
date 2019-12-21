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
 * @author 何鹏举 2019-12-21
 */
@Data
@ApiModel("角色表")
@TableName("Z10_ROLE")
public class Role {

    // 查询条件
    @TableField(exist = false)
    @ApiModelProperty("角色代码或名称")
    private String roleCodeOrName;

    // ---------------------------------------
    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("角色主键")
    private String roleId;

    @ApiModelProperty("角色代码")
    private String roleCode;

    @ApiModelProperty("角色名称")
    private String roleName;

    @Code
    @ApiModelProperty("角色类型(N-正常, P-职位)")
    private String roleType;

    @ApiModelProperty("角色描述")
    private String roleDesc;

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