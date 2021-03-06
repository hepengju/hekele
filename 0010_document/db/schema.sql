use hekele;

-- Create By HeTools
-- 共计[ 9 ]个表

DROP TABLE IF EXISTS `Z00_SEQ`;
CREATE TABLE `Z00_SEQ` (
                           `SEQ_ID`      VARCHAR(32)                                      COMMENT '主键'
    ,`SEQ_NAME`    VARCHAR(64)  NOT NULL                            COMMENT '序列名称'
    ,`SEQ_DATE`    VARCHAR(8)                                       COMMENT '序列日期'
    ,`SEQ_VALUE`   INT                                              COMMENT '序列值'
    ,`SEQ_DESC`    VARCHAR(512)                                     COMMENT '序列描述'
    ,`CREATE_TIME` DATETIME     NOT NULL  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    ,`UPDATE_TIME` DATETIME                                         COMMENT '修改时间'
    ,PRIMARY KEY(`SEQ_ID`)
) COMMENT ='序列表';

DROP TABLE IF EXISTS `Z00_CODE`;
CREATE TABLE `Z00_CODE` (
                            `CODE_ID`          VARCHAR(32)                                      COMMENT '主键'
    ,`SYS_CODE`         VARCHAR(64)  NOT NULL                            COMMENT '系统代码(ADMIN/FILE…, 展示用)'
    ,`TYPE_CODE`        VARCHAR(64)  NOT NULL                            COMMENT '类型代码'
    ,`TYPE_NAME`        VARCHAR(64)  NOT NULL                            COMMENT '类型名称'
    ,`ITEM_CODE`        VARCHAR(64)  NOT NULL                            COMMENT '条目代码'
    ,`ITEM_NAME`        VARCHAR(64)  NOT NULL                            COMMENT '条目名称'
    ,`ITEM_SORT`        INT          NOT NULL  DEFAULT 0                 COMMENT '条目排序'
    ,`ATTR1`            VARCHAR(512)                                     COMMENT '扩展属性1'
    ,`ATTR2`            VARCHAR(512)                                     COMMENT '扩展属性2'
    ,`ATTR3`            VARCHAR(512)                                     COMMENT '扩展属性3'
    ,`ATTR4`            VARCHAR(512)                                     COMMENT '扩展属性4'
    ,`ATTR5`            VARCHAR(512)                                     COMMENT '扩展属性5'
    ,`CREATE_TIME`      DATETIME     NOT NULL  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    ,`CREATE_USER_CODE` VARCHAR(64)  NOT NULL                            COMMENT '创建人员账号'
    ,`CREATE_USER_NAME` VARCHAR(64)  NOT NULL                            COMMENT '创建人员名称'
    ,`UPDATE_TIME`      DATETIME                                         COMMENT '更新时间'
    ,`UPDATE_USER_CODE` VARCHAR(64)                                      COMMENT '更新人员账号'
    ,`UPDATE_USER_NAME` VARCHAR(64)                                      COMMENT '更新人员名称'
    ,`ENABLE_FLAG`      VARCHAR(2)   NOT NULL  DEFAULT '1'               COMMENT '启用状态(0-停用, 1-启用)'
    ,`DELETE_FLAG`      VARCHAR(1)   NOT NULL  DEFAULT '1'               COMMENT '删除标志(0-已删除,1-未删除)'
    ,PRIMARY KEY(`CODE_ID`)
) COMMENT ='代码表';

DROP TABLE IF EXISTS `Z10_MENU`;
CREATE TABLE `Z10_MENU` (
                            `MENU_ID`          VARCHAR(64)  NOT NULL                            COMMENT '菜单主键'
    ,`PARENT_ID`        VARCHAR(64)  NOT NULL                            COMMENT '父菜单'
    ,`MENU_CODE`        VARCHAR(64)                                      COMMENT '菜单代码'
    ,`MENU_NAME`        VARCHAR(64)  NOT NULL                            COMMENT '菜单名称'
    ,`MENU_URI`         VARCHAR(512)                                     COMMENT '菜单URI'
    ,`MENU_TYPE`        VARCHAR(1)   NOT NULL                            COMMENT '菜单类型(G-菜单组, M-菜单, B-按钮)'
    ,`DEVICE_TYPE`      VARCHAR(1)   NOT NULL  DEFAULT 'B'               COMMENT '设备类型(B-浏览器, M-手机)'
    ,`MENU_ICON`        VARCHAR(64)                                      COMMENT '菜单图标'
    ,`MENU_SORT`        INT          NOT NULL  DEFAULT 0                 COMMENT '菜单顺序'
    ,`CREATE_TIME`      DATETIME     NOT NULL  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    ,`CREATE_USER_CODE` VARCHAR(64)  NOT NULL                            COMMENT '创建人员账号'
    ,`CREATE_USER_NAME` VARCHAR(64)  NOT NULL                            COMMENT '创建人员名称'
    ,`UPDATE_TIME`      DATETIME                                         COMMENT '更新时间'
    ,`UPDATE_USER_CODE` VARCHAR(64)                                      COMMENT '更新人员账号'
    ,`UPDATE_USER_NAME` VARCHAR(64)                                      COMMENT '更新人员名称'
    ,`ENABLE_FLAG`      VARCHAR(2)   NOT NULL  DEFAULT '1'               COMMENT '启用状态(0-停用, 1-启用)'
    ,`DELETE_FLAG`      VARCHAR(1)   NOT NULL  DEFAULT '1'               COMMENT '删除标志(0-已删除,1-未删除)'
    ,PRIMARY KEY(`MENU_ID`)
) COMMENT ='菜单表';

DROP TABLE IF EXISTS `Z10_ORG`;
CREATE TABLE `Z10_ORG` (
                           `ORG_ID`           VARCHAR(64)  NOT NULL                            COMMENT '部门主键'
    ,`PARENT_ID`        VARCHAR(64)  NOT NULL                            COMMENT '父部门主键'
    ,`ORG_CODE`         VARCHAR(64)  NOT NULL                            COMMENT '部门代码'
    ,`ORG_NAME`         VARCHAR(64)  NOT NULL                            COMMENT '部门名称'
    ,`ORG_TYPE`         VARCHAR(1)   NOT NULL                            COMMENT '部门类型(0-公司,1-一级部门, 2-二级部门)'
    ,`LEADER_ID`        VARCHAR(64)  NOT NULL                            COMMENT '负责人主键'
    ,`LEADER_CODE`      VARCHAR(64)  NOT NULL                            COMMENT '负责人账号'
    ,`LEADER_NAME`      VARCHAR(64)  NOT NULL                            COMMENT '负责人名称'
    ,`ORG_DESC`         VARCHAR(512)                                     COMMENT '部门描述'
    ,`ORG_SORT`         INT          NOT NULL  DEFAULT 0                 COMMENT '排序顺序'
    ,`CREATE_TIME`      DATETIME     NOT NULL  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    ,`CREATE_USER_CODE` VARCHAR(64)  NOT NULL                            COMMENT '创建人员账号'
    ,`CREATE_USER_NAME` VARCHAR(64)  NOT NULL                            COMMENT '创建人员名称'
    ,`UPDATE_TIME`      DATETIME                                         COMMENT '更新时间'
    ,`UPDATE_USER_CODE` VARCHAR(64)                                      COMMENT '更新人员账号'
    ,`UPDATE_USER_NAME` VARCHAR(64)                                      COMMENT '更新人员名称'
    ,`ENABLE_FLAG`      VARCHAR(2)   NOT NULL  DEFAULT '1'               COMMENT '启用状态(0-停用, 1-启用)'
    ,`DELETE_FLAG`      VARCHAR(1)   NOT NULL  DEFAULT '1'               COMMENT '删除标志(0-已删除,1-未删除)'
    ,PRIMARY KEY(`ORG_ID`)
) COMMENT ='部门表';

DROP TABLE IF EXISTS `Z10_ROLE`;
CREATE TABLE `Z10_ROLE` (
                            `ROLE_ID`          VARCHAR(64)  NOT NULL                            COMMENT '角色主键'
    ,`ROLE_CODE`        VARCHAR(64)  NOT NULL                            COMMENT '角色代码'
    ,`ROLE_NAME`        VARCHAR(64)  NOT NULL                            COMMENT '角色名称'
    ,`ROLE_TYPE`        VARCHAR(1)   NOT NULL  DEFAULT 'N'               COMMENT '角色类型(N-正常, P-职位)'
    ,`ROLE_DESC`        VARCHAR(512)                                     COMMENT '角色描述'
    ,`CREATE_TIME`      DATETIME     NOT NULL  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    ,`CREATE_USER_CODE` VARCHAR(64)  NOT NULL                            COMMENT '创建人员账号'
    ,`CREATE_USER_NAME` VARCHAR(64)  NOT NULL                            COMMENT '创建人员名称'
    ,`UPDATE_TIME`      DATETIME                                         COMMENT '更新时间'
    ,`UPDATE_USER_CODE` VARCHAR(64)                                      COMMENT '更新人员账号'
    ,`UPDATE_USER_NAME` VARCHAR(64)                                      COMMENT '更新人员名称'
    ,`ENABLE_FLAG`      VARCHAR(2)   NOT NULL  DEFAULT '1'               COMMENT '启用状态(0-停用, 1-启用)'
    ,`DELETE_FLAG`      VARCHAR(1)   NOT NULL  DEFAULT '1'               COMMENT '删除标志(0-已删除,1-未删除)'
    ,PRIMARY KEY(`ROLE_ID`)
) COMMENT ='角色表';

DROP TABLE IF EXISTS `Z10_USER`;
CREATE TABLE `Z10_USER` (
                            `USER_ID`                VARCHAR(64)   NOT NULL                            COMMENT '用户主键'
    ,`USER_CODE`              VARCHAR(64)   NOT NULL                            COMMENT '用户账号(供登录, 唯一索引)'
    ,`USER_NAME`              VARCHAR(64)   NOT NULL                            COMMENT '用户名称'
    ,`GENDER`                 VARCHAR(1)                                        COMMENT '性别(M-男 F-女)'
    ,`BIRTHDAY`               DATE                                              COMMENT '出生日期'
    ,`PHONE_NO`               VARCHAR(32)                                       COMMENT '手机号码'
    ,`IDENTITY_NO`            VARCHAR(64)                                       COMMENT '身份证号'
    ,`BANK_CARD_NO`           VARCHAR(64)                                       COMMENT '银行卡号'
    ,`EMAIL`                  VARCHAR(64)                                       COMMENT '电子邮件(供登录, 唯一索引)'
    ,`USER_PIC`               VARCHAR(512)                                      COMMENT '用户头像'
    ,`ROLE_ID`                VARCHAR(32)   NOT NULL                            COMMENT '职位(特殊角色)'
    ,`ORG_ID`                 VARCHAR(32)   NOT NULL                            COMMENT '部门'
    ,`DATA_PERM_TYPE`         VARCHAR(1)    NOT NULL  DEFAULT 'P'               COMMENT '数据权限类型(P-个人, O-本部门, M-多部门)'
    ,`DATA_PERM_LIST`         VARCHAR(2048)                                     COMMENT '数据权限部门代号列表'
    ,`USER_TOKEN`             VARCHAR(64)                                       COMMENT '用户令牌(API访问)'
    ,`LOGIN_LAST_TIME`        DATETIME                                          COMMENT '最后登录时间'
    ,`PASSWORD_HASH`          VARCHAR(64)   NOT NULL                            COMMENT '密码哈希值'
    ,`PASSWORD_STATUS`        VARCHAR(1)    NOT NULL  DEFAULT '1'               COMMENT '密码状态(0-临时 1-永久)'
    ,`PASSWORD_TRY_COUNT`     INT                     DEFAULT 0                 COMMENT '密码尝试次数(错误累计,成功登录清零)'
    ,`PASSWORD_TRY_LAST_TIME` DATETIME                                          COMMENT '密码最后尝试时间'
    ,`PASSWORD_EXPIRY_DATE`   DATE                                              COMMENT '密码过期日期(密码即将过期提示, 已经过期强制修改密码)'
    ,`LOGIN_SUCCESS_COUNT`    INT                     DEFAULT 0                 COMMENT '登录成功次数(成功登录时+1)'
    ,`USER_STATUS`            VARCHAR(1)              DEFAULT '1'               COMMENT '在职状态(0-离职, 1-在职)'
    ,`CREATE_TIME`            DATETIME      NOT NULL  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    ,`CREATE_USER_CODE`       VARCHAR(64)   NOT NULL                            COMMENT '创建人员账号'
    ,`CREATE_USER_NAME`       VARCHAR(64)   NOT NULL                            COMMENT '创建人员名称'
    ,`UPDATE_TIME`            DATETIME                                          COMMENT '更新时间'
    ,`UPDATE_USER_CODE`       VARCHAR(64)                                       COMMENT '更新人员账号'
    ,`UPDATE_USER_NAME`       VARCHAR(64)                                       COMMENT '更新人员名称'
    ,`ENABLE_FLAG`            VARCHAR(2)    NOT NULL  DEFAULT '1'               COMMENT '启用状态(0-停用, 1-启用)'
    ,`DELETE_FLAG`            VARCHAR(1)    NOT NULL  DEFAULT '1'               COMMENT '删除标志(0-已删除,1-未删除)'
    ,PRIMARY KEY(`USER_ID`)
) COMMENT ='用户表';

DROP TABLE IF EXISTS `Z11_ROLE_MENU`;
CREATE TABLE `Z11_ROLE_MENU` (
                                 `ID`               VARCHAR(64) NOT NULL                            COMMENT '主键'
    ,`ROLE_ID`          VARCHAR(64) NOT NULL                            COMMENT '角色主键'
    ,`MENU_ID`          VARCHAR(64) NOT NULL                            COMMENT '菜单主键'
    ,`CREATE_TIME`      DATETIME    NOT NULL  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    ,`CREATE_USER_CODE` VARCHAR(64) NOT NULL                            COMMENT '创建人员账号'
    ,`CREATE_USER_NAME` VARCHAR(64) NOT NULL                            COMMENT '创建人员名称'
    ,PRIMARY KEY(`ID`)
) COMMENT ='角色菜单表';

DROP TABLE IF EXISTS `Z11_USER_ROLE`;
CREATE TABLE `Z11_USER_ROLE` (
                                 `ID`               VARCHAR(64) NOT NULL                            COMMENT '主键'
    ,`USER_ID`          VARCHAR(64) NOT NULL                            COMMENT '用户主键'
    ,`ROLE_ID`          VARCHAR(64) NOT NULL                            COMMENT '角色主键'
    ,`CREATE_TIME`      DATETIME    NOT NULL  DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
    ,`CREATE_USER_CODE` VARCHAR(64) NOT NULL                            COMMENT '创建人员账号'
    ,`CREATE_USER_NAME` VARCHAR(64) NOT NULL                            COMMENT '创建人员名称'
    ,PRIMARY KEY(`ID`)
) COMMENT ='用户角色表';

DROP TABLE IF EXISTS `PERSON`;
CREATE TABLE `PERSON` (
                          `USER_ID`           INT           NOT NULL  AUTO_INCREMENT COMMENT '主键'
    ,`USER_NAME`         VARCHAR(256)                           COMMENT '姓名'
    ,`GENDER`            VARCHAR(1)                             COMMENT '性别'
    ,`PHONE`             VARCHAR(64)                            COMMENT '手机号'
    ,`BIRTH`             DATETIME                               COMMENT '出生日期'
    ,`FAMILY_POPULATION` INT                                    COMMENT '家庭人口'
    ,`SALARY_YEAR`       DECIMAL(20,2)                          COMMENT '年收入'
    ,`MANAGER_NO`        VARCHAR(64)                            COMMENT '经理编号'
    ,`ORG_NO`            VARCHAR(64)                            COMMENT '机构编号'
    ,`FAMILY_ADDR`       VARCHAR(256)                           COMMENT '家庭地址'
    ,PRIMARY KEY(`USER_ID`)
) COMMENT ='人员表';

