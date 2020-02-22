package com.hepengju.hekele.data.generator.gen100_date;

import com.hepengju.hekele.base.constant.HeConst;
import com.hepengju.hekele.base.util.DateUtil;
import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.AbstractDateGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.core.annotation.Order;

import java.util.Date;

@ApiModel("日期时间生成器") @Data @Order(102)
public class DateTimeGenerator extends AbstractDateGenerator<Date> {

	private String min = "1900-01-01 00:00:00";
	private String max = "2100-12-31 23:59:59";
	private String format = HeConst.DATE_TIME_FORMAT;

	private Date minDate;
	private Date maxDate;

	@Override
	public Date generate() {
		return RandomUtil.randomDate(minDate.getTime(), maxDate.getTime());
	}

	public void setMin(String min) {
		this.min = min;
		this.minDate = DateUtil.stringToDate(min);
	}

	public void setMax(String max) {
		this.max = max;
		maxDate = DateUtil.stringToDate(max);
	}

	public DateTimeGenerator() {
		this.setMin(min);
		this.setMax(max);
	}
	public DateTimeGenerator(String min, String max) {
		this.setMin(min);
		this.setMax(max);
	}

}
