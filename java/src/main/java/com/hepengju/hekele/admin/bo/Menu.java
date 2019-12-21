package com.hepengju.hekele.admin.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
@ApiModel("菜单表")
@TableName("Z10_MENU")
public class Menu {

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("菜单主键")
    private String menuId;

    @ApiModelProperty("父菜单")
    private String parentId;

    @ApiModelProperty("菜单代码")
    private String menuCode;

    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单URI")
    private String menuUri;

    @Code
    @ApiModelProperty("菜单类型(G-菜单组, M-菜单, B-按钮)")
    private String menuType;

    @Code
    @ApiModelProperty("设备类型(B-浏览器, M-手机, P-PAD)")
    private String terminalType;

    @ApiModelProperty("菜单图标")
    private String menuIcon;

    @ApiModelProperty("菜单顺序")
    private Integer menuSort;

    @ApiModelProperty("创建时间")
    @JSONField(format = HeConst.DATE_TIME_FORMAT)
    private Date createTime;

    @ApiModelProperty("创建人员账号")
    private String createUserCode;

    @ApiModelProperty("创建人员名称")
    private String createUserName;

    @ApiModelProperty("更新时间")
    @JSONField(format = HeConst.DATE_TIME_FORMAT)
    private Date updateTime;

    @ApiModelProperty("更新人员账号")
    private String updateUserCode;

    @ApiModelProperty("更新人员名称")
    private String updateUserName;

    @Code
    @ApiModelProperty("启用状态(0-停用, 1-启用)")
    private String enableFlag;

    @TableLogic  @JSONField(serialize=false)
    @ApiModelProperty("删除标志(0-已删除,1-未删除)")
    private String deleteFlag;

}