package com.hepengju.hekele.data.generator.date;

import com.hepengju.hekele.base.util.DateUtil;
import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.Generator;
import io.swagger.annotations.ApiModel;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

@ApiModel("日期生成器")
public class DateGenerator implements Generator<Date> {

	private String min = "1900-01-01";
	private String max = "2100-12-31";

	private Date minDate;
	private Date maxDate;

	@Override
	public Date generate() {
		return DateUtils.truncate(RandomUtil.randomDate(minDate.getTime(), maxDate.getTime()), Calendar.DATE);
	}

	public DateGenerator() {
		this.setMin(this.min);
		this.setMax(this.max);
	}

	public DateGenerator(String min, String max) {
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

