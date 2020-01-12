package com.hepengju.hekele.data.generator.date;

import com.hepengju.hekele.base.util.RandomUtil;
import com.hepengju.hekele.data.Generator;

import java.util.Date;

/**
 * 日期时间数据生成器
 * 
 * @author hepengju
 *
 */
public class DateTimeGenerator implements Generator<Date> {

	private long startInclusive = -2209017600000l;    //1900-01-01 00:00:00
	private long endInclusive   = 4133952000000l;     //2101-01-01 00:00:00
	
	
	public DateTimeGenerator() {
		super();
	}

	public DateTimeGenerator(long startInclusive, long endInclusive) {
		super();
		this.startInclusive = startInclusive;
		this.endInclusive = endInclusive;
	}


	@Override
	public Date generate() {
		return RandomUtil.randomDate(startInclusive, endInclusive);
	}

}
