package com.hepengju.hekele.data.generator.date;

import com.hepengju.hekele.base.util.DateUtil;
import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.Generator;

import java.util.Date;

/**
 * 日期时间数据生成器
 * 
 * @author hepengju
 *
 */
public class DateTimeGenerator implements Generator<Date> {

	private String min = "1900-01-01 00:00:00";
	private String max = "2100-12-31 23:59:59";

	private Date minDate;
	private Date maxDate;

	@Override
	public Date generate() {
		return RandomUtil.randomDate(minDate.getTime(), maxDate.getTime());
	}

	public DateTimeGenerator() {
		this.setMin(min);
		this.setMax(max);
	}
	public DateTimeGenerator(String min, String max) {
		this.setMin(min);
		this.setMax(max);
	}

	public void setMin(String min) {
		this.min = min;
		this.minDate = DateUtil.stringToDate(min);
	}

	public void setMax(String max) {
		this.max = max;
		maxDate = DateUtil.stringToDate(max);
	}
}
