package com.hepengju.hekele.data.bo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hepengju.hekele.base.constant.HeConst;
import com.hepengju.hekele.data.annotation.GeneratorAnno;
import com.hepengju.hekele.data.generator.gen100_date.DateGenerator;
import com.hepengju.hekele.data.generator.gen200_number.DoubleGenerator;
import com.hepengju.hekele.data.generator.gen200_number.IntegerGenerator;
import com.hepengju.hekele.data.generator.gen300_string.CodeGenerator;
import com.hepengju.hekele.data.generator.gen300_string.RandomStringNumberGenerator;
import com.hepengju.hekele.data.generator.gen400_custom.gen410_name.ChineseNameGenerator;
import com.hepengju.hekele.data.generator.gen400_custom.gen430_phone.MobileGenerator;
import com.hepengju.hekele.data.generator.gen400_custom.gen440_computer.UUIDGenerator;
import com.hepengju.hekele.data.generator.gen400_custom.gen460_address.ChinaAddressGenerator;
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
    @GeneratorAnno(UUIDGenerator.class)
    private String userId;

    @ApiModelProperty("账号")
    @GeneratorAnno(value = RandomStringNumberGenerator.class, min = "5", max = "5")
    private String userCode;

    @ApiModelProperty("姓名")
    @GeneratorAnno(ChineseNameGenerator.class)
    private String userName;

    @ApiModelProperty("性别")
    @GeneratorAnno(value = CodeGenerator.class, code = "M,F")
    private String gender;

    @ApiModelProperty("手机号")
    @GeneratorAnno(MobileGenerator.class)
    private String phone;

    @ApiModelProperty("出生日期")
    @GeneratorAnno(value = DateGenerator.class, min = "1949-01-01", max = "2020-01-01")
    @JsonFormat(pattern = HeConst.DATE_FORMAT, timezone = "GMT+8")
    private Date birth;

    @ApiModelProperty("家庭人口")
    @GeneratorAnno(value = IntegerGenerator.class, min = "1", max = "30")
    private Integer familyPopulation;

    @ApiModelProperty("年收入")
    @GeneratorAnno(value = DoubleGenerator.class, min = "0", max = "999999999")
    private BigDecimal salaryYear;

    @ApiModelProperty("经理编号")
    @GeneratorAnno(value = CodeGenerator.class, code = "M001,M002,M003,M004,M005")
    private String managerNo;

    @ApiModelProperty("机构编号")
    @GeneratorAnno(value = CodeGenerator.class, code = "ORG0101,ORG0102")
    private String orgNo;

    @ApiModelProperty("家庭地址")
    @GeneratorAnno(ChinaAddressGenerator.class)
    private String familyAddr;

}
