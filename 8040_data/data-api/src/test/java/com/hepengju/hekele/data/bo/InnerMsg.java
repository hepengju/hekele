package com.hepengju.hekele.data.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hepengju.hekele.data.annotation.GeneratorAnno;
import com.hepengju.hekele.data.generator.gen400_custom.gen440_computer.UUIDGenerator;
import com.hepengju.hekele.data.generator.gen400_custom.gen410_name.ChineseNameGenerator;
import com.hepengju.hekele.data.generator.gen300_string.CodeGenerator;
import com.hepengju.hekele.data.generator.gen300_string.RandomChineseGenerator;
import com.hepengju.hekele.data.generator.gen300_string.RandomStringNumberGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Auto Created By ExcelVBA
 *
 * @author 何鹏举 2020-02-10
 */
@Data
@ApiModel("站内用户消息")
@TableName("CTC_INNER_MSG")
public class InnerMsg {

    @GeneratorAnno(UUIDGenerator.class)
    @TableId(type = IdType.UUID)
    @ApiModelProperty("主键")
    private String msgId;

    @GeneratorAnno(value = RandomChineseGenerator.class, min = "10", max = "10")
    @ApiModelProperty("消息标题")
    private String msgTitle;

    @GeneratorAnno(value = RandomChineseGenerator.class, min = "40", max = "40")
    @ApiModelProperty("消息内容")
    private String msgContent;

    @GeneratorAnno(value = CodeGenerator.class, code = "S,B")
    @ApiModelProperty("消息大类(S-系统, B-业务)")
    private String msgCategory;

    @ApiModelProperty("消息子类(订单受理,订单办理,我的待办…)")
    private String msgClass;

    @GeneratorAnno(value = CodeGenerator.class, code = "H,M,L")
    @ApiModelProperty("消息优先级(H-高, M-中, L-低)")
    private String msgPriority;

    @GeneratorAnno(value = CodeGenerator.class, code = "sysdate")
    //@Generator(DateTimeGenerator.class)
    @ApiModelProperty("消息发送时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date msgSendTime;

    @GeneratorAnno(value = RandomStringNumberGenerator.class, min = "5",max = "5")
    @ApiModelProperty("消息发送人账号")
    private String msgSenderCode;

    @GeneratorAnno(ChineseNameGenerator.class)
    @ApiModelProperty("消息发送人名称")
    private String msgSenderName;

    @GeneratorAnno(value = RandomStringNumberGenerator.class, min = "5",max = "5")
    @ApiModelProperty("消息接收人账号")
    private String msgReceiverCode;

    @GeneratorAnno(ChineseNameGenerator.class)
    @ApiModelProperty("消息接收人名称")
    private String msgReceiverName;

    @GeneratorAnno(value = CodeGenerator.class, code = "0,1")
    @ApiModelProperty("消息状态(0-未读 1-已读)")
    private String msgReadStatus;

    @GeneratorAnno(value = CodeGenerator.class, code = "sysdate")
    //@Generator(LocalDateTimeGenerator.class)
    @ApiModelProperty("消息读取时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date msgReadTime;

    @GeneratorAnno(value = CodeGenerator.class, code = "0,1")
    @TableLogic(value = "0", delval = "1")
    @ApiModelProperty("删除标记(0-未删除 1-已删除)")
    private String delFlag;

}