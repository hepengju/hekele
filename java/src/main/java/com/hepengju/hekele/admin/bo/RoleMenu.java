package com.hepengju.hekele.admin.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@ApiModel("角色菜单表")
@TableName("Z11_ROLE_MENU")
public class RoleMenu {

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("角色主键")
    private String roleId;

    @ApiModelProperty("菜单主键")
    private String menuId;

    @ApiModelProperty("创建时间")
    @JSONField(format = HeConst.DATE_TIME_FORMAT)
    private Date createTime;

    @ApiModelProperty("创建人员账号")
    private String createUserCode;

    @ApiModelProperty("创建人员名称")
    private String createUserName;

}