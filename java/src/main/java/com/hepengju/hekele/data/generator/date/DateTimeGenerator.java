package com.hepengju.hekele.data.generator.date;

import com.hepengju.hekele.base.util.DateUtil;
import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.Generator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 日期时间数据生成器
 * 
 * @author hepengju
 *
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class DateTimeGenerator implements Generator<Date> {

	private Date min = DateUtil.stringToDate("1900-01-01 00:00:00");
	private Date max = DateUtil.stringToDate("2100-12-31 23:59:59");

	@Override
	public Date generate() {
		return RandomUtil.randomDate(min.getTime(), max.getTime());
	}

}
