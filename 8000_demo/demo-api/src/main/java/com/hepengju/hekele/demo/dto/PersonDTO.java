package com.hepengju.hekele.demo.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * DTO对象
 *
 * <pre>
 *     1. 封装不存在的查询条件字段，比如"用户账号或名称","开始时间","结束时间"等;
 *          - 如果查询条件很少
 *              方法1）直接编写查询类
 *              方法2）BO类追加字段，并指示其在表中不存在。（查询列表的结果会多出这个字段）
 *          - 如果查询条件很多
 *              * 这些条件很多和BO类的属性一致时，继承BO类，添加查询条件
 *     2. 封装需要多表关联显示的一些字段
 *          - BO类中追加字段，并指示其在表中不存在
 * </pre>
 *
 * @author hepengju
 */
@Data
@ApiModel("人员传输对象")
public class PersonDTO /*extends Person*/ {

    @ApiModelProperty("用户账号或姓名")
    private String userCodeOrName;

    @ApiModelProperty("出生日期开始时间")
    private Date birthStart;

    @ApiModelProperty("出生日期结束时间")
    private Date birthEnd;

}
