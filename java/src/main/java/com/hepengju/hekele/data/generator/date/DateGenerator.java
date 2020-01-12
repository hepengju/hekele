package com.hepengju.hekele.data.generator.date;

import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.Generator;
import lombok.Data;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期生成器
 * 
 * @author hepengju
 */
@Data
public class DateGenerator implements Generator<Date> {

	private long startInclusive = -2209017600000l;    //1900-01-01 00:00:00
	private long endInclusive   = 4133952000000l;     //2101-01-01 00:00:00

	@Override
	public Date generate() {
		return DateUtils.truncate(RandomUtil.randomDate(startInclusive, endInclusive), Calendar.DATE);
	}

}

