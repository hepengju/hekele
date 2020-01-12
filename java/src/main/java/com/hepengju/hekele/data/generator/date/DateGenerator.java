package com.hepengju.hekele.data.generator.date;

import com.hepengju.hekele.base.util.DateUtil;
import com.hepengju.hekele.base.util.RandomUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期生成器
 * 
 * @author hepengju
 */
@Data @NoArgsConstructor @AllArgsConstructor
public class DateGenerator implements com.hepengju.hekele.data.generator.DateGenerator  {

	private Date min = DateUtil.stringToDate("1900-01-01");
	private Date max = DateUtil.stringToDate("2100-12-31");

	@Override
	public Date generate() {
		return DateUtils.truncate(RandomUtil.randomDate(min.getTime(), max.getTime()), Calendar.DATE);
	}

}

