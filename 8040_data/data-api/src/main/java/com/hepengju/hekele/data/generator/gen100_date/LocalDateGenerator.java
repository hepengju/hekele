package com.hepengju.hekele.data.generator.gen100_date;

import com.hepengju.hekele.base.constant.HeConst;
import com.hepengju.hekele.data.generator.AbstractTemporalAccessorGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * 由dateTimeGenerator生成即可
 *
 * @author hepengju
 */
@ApiModel("LocalDate生成器") @Data
public class LocalDateGenerator extends AbstractTemporalAccessorGenerator<LocalDate> {

    private String min = "1900-01-01";
    private String max = "2100-12-31";
    private String format = HeConst.DATE_FORMAT;

    private DateTimeGenerator dateTimeGenerator;

    public LocalDate generate() {
        return dateTimeGenerator.generate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public LocalDateGenerator() {
        this.dateTimeGenerator = new DateTimeGenerator(this.min, this.max);
    }

    public LocalDateGenerator(String min, String max) {
    	this.min = min;
    	this.max = max;
        this.dateTimeGenerator = new DateTimeGenerator(min, max);
    }
    
}
