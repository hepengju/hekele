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
@ApiModel("用户表")
@TableName("Z10_USER")
public class User {

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("用户主键")
    private String userId;

    @ApiModelProperty("用户账号(供登录, 唯一索引)")
    private String userCode;

    @ApiModelProperty("用户名称")
    private String userName;

    @Code
    @ApiModelProperty("性别(M-男 F-女)")
    private String gender;

    @ApiModelProperty("出生日期")
    @JSONField(format = HeConst.DATE_FORMAT)
    private Date birthday;

    @ApiModelProperty("手机号码")
    private String phoneNo;

    @ApiModelProperty("身份证号")
    private String identityNo;

    @ApiModelProperty("银行卡号")
    private String bankCardNo;

    @ApiModelProperty("电子邮件(供登录, 唯一索引)")
    private String email;

    @ApiModelProperty("用户头像")
    private String userPic;

    @ApiModelProperty("职位(特殊角色)")
    private String roleId;

    @ApiModelProperty("部门")
    private String orgId;

    @Code
    @ApiModelProperty("数据权限类型(P-个人, O-本部门, M-多部门)")
    private String dataPermType;

    @ApiModelProperty("数据权限部门代号列表")
    private String dataPermList;

    @ApiModelProperty("用户令牌(API访问)")
    private String userToken;

    @ApiModelProperty("最后登录时间")
    @JSONField(format = HeConst.DATE_TIME_FORMAT)
    private Date loginLastTime;

    @ApiModelProperty("密码哈希值")
    private String passwordHash;

    @Code
    @ApiModelProperty("密码状态(0-临时 1-永久)")
    private String passwordStatus;

    @ApiModelProperty("密码尝试次数(错误累计,成功登录清零)")
    private Integer passwordTryCount;

    @ApiModelProperty("密码最后尝试时间")
    @JSONField(format = HeConst.DATE_TIME_FORMAT)
    private Date passwordTryLastTime;

    @ApiModelProperty("密码过期日期(密码即将过期提示, 已经过期强制修改密码)")
    @JSONField(format = HeConst.DATE_FORMAT)
    private Date passwordExpiryDate;

    @ApiModelProperty("登录成功次数(成功登录时+1)")
    private Integer loginSuccessCount;

    @Code
    @ApiModelProperty("在职状态(0-离职, 1-在职)")
    private String userStatus;

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