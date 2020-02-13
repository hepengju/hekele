package com.hepengju.hekele.admin.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@ApiModel("用户角色表")
@TableName("Z11_USER_ROLE")
public class UserRole {

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("用户主键")
    private String userId;

    @ApiModelProperty("角色主键")
    private String roleId;

    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = HeConst.DATE_TIME_FORMAT, timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("创建人员账号")
    private String createUserCode;

    @ApiModelProperty("创建人员名称")
    private String createUserName;

}