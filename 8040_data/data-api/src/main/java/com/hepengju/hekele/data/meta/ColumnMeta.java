package com.hepengju.hekele.data.meta;

import com.hepengju.hekele.data.generator.Generator;
import lombok.Data;

import java.util.List;

/**
 * 
 * @author hepengju
 *
 */
@Data
public class ColumnMeta {

	private String comment   = ""                ;  //列注释
	private String field                         ;  //列字段
	private ColumnType type  = ColumnType.STRING ;  //列类型
	
	private Integer size           = 10          ;  //列大小(对于字符串类型是长度,对于整数和小数是精度)
	private Integer scale          = 0           ;  //列精度
	
	private Boolean autoIncrement  = false       ;  //是否是自增
	private Boolean unique         = false       ;  //是否唯一
	private Boolean nullable       = true        ;  //是否可空
	
	private String defaultValue                  ;  //默认值
	private List<String> codeList                ;  //码值列表
	
	private Generator<?> generator               ;  //生成器

}
