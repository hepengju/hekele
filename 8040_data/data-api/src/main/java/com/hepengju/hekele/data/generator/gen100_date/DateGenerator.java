package com.hepengju.hekele.data.generator.gen100_date;

import com.hepengju.hekele.base.constant.HeConst;
import com.hepengju.hekele.base.util.DateUtil;
import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.generator.AbstractDateGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.core.annotation.Order;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期生成器
 *
 * <br> 需要显示在前端界面的, 请加入Order注解, 并指定顺序
 *
 * @author hepengju
 */
@ApiModel("日期生成器") @Data @Order(101)
public class DateGenerator extends AbstractDateGenerator<Date> {

	private String min = "1900-01-01";
	private String max = "2100-12-31";
	private String format = HeConst.DATE_FORMAT;

	private Date minDate;
	private Date maxDate;

	@Override
	public Date generate() {
		return DateUtils.truncate(RandomUtil.randomDate(minDate.getTime(), maxDate.getTime()), Calendar.DATE);
	}

	/**
	 * 设置字符串最小值的时候, 同时设置日期的最值
	 */
	public void setMin(String min) {
		this.min = min;
		this.minDate = DateUtil.stringToDate(min);
	}

	public void setMax(String max) {
		this.max = max;
		maxDate = DateUtil.stringToDate(max);
	}

	public DateGenerator() {
		this.setMin(this.min);
		this.setMax(this.max);
	}

	public DateGenerator(String min, String max) {
		this.setMin(min);
		this.setMax(max);
	}

}

