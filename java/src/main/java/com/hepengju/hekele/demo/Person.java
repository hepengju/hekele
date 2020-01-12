package com.hepengju.hekele.demo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hepengju.hekele.base.constant.HeConst;
import com.hepengju.hekele.data.annotation.Generator;
import com.hepengju.hekele.data.generator.date.DateGenerator;
import com.hepengju.hekele.data.generator.number.DoubleGenerator;
import com.hepengju.hekele.data.generator.number.IntegerGenerator;
import com.hepengju.hekele.data.generator.string.CodeGenerator;
import com.hepengju.hekele.data.generator.string.address.AddressGenerator;
import com.hepengju.hekele.data.generator.string.computer.UUIDGenerator;
import com.hepengju.hekele.data.generator.string.name.NameGenerator;
import com.hepengju.hekele.data.generator.string.phone.MobileGenerator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Auto Created By ExcelVBA
 *
 * @author 何鹏举 2020-01-12
 */
@Data
@ApiModel("人员")
@TableName("PERSON")
public class Person {

    @TableId(type = IdType.ASSIGN_UUID)
    @ApiModelProperty("主键")
    @Generator(UUIDGenerator.class)
    private String userId;

    @ApiModelProperty("姓名")
    @Generator(NameGenerator.class)
    private String userName;

    @ApiModelProperty("性别")
    @Generator(value = CodeGenerator.class, code = "M,F")
    private String gender;

    @ApiModelProperty("手机号")
    @Generator(MobileGenerator.class)
    private String phone;

    @ApiModelProperty("出生日期")
    @Generator(value = DateGenerator.class, min = "1949-01-01", max = "2020-01-01")
    @JsonFormat(pattern = HeConst.DATE_FORMAT, timezone = "GMT+8")
    private Date birth;

    @ApiModelProperty("家庭人口")
    @Generator(value = IntegerGenerator.class, min = "1", max = "30")
    private Integer familyPopulation;

    @ApiModelProperty("年收入")
    @Generator(value = DoubleGenerator.class, min = "0", max = "999999999", scale = 3)
    private BigDecimal salaryYear;

    @ApiModelProperty("经理编号")
    @Generator(value = CodeGenerator.class, code = "M001,M002,M003,M004,M005")
    private String managerNo;

    @ApiModelProperty("机构编号")
    @Generator(value = CodeGenerator.class, code = "ORG0101,ORG0102")
    private String orgNo;

    @ApiModelProperty("家庭地址")
    @Generator(AddressGenerator.class)
    private String familyAddr;

}
