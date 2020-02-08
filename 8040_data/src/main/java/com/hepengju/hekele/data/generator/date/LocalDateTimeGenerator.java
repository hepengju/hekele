package com.hepengju.hekele.data.generator.date;

import com.hepengju.hekele.data.generator.Generator;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class LocalDateTimeGenerator implements Generator<LocalDateTime> {

    private String min = "1900-01-01 00:00:00";
    private String max = "2100-12-31 23:59:59";

    private DateTimeGenerator dateTimeGenerator;

    @Override
    public LocalDateTime generate() {
        return dateTimeGenerator.generate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public LocalDateTimeGenerator() {
        this.dateTimeGenerator = new DateTimeGenerator(this.min, this.max);
    }

    public LocalDateTimeGenerator(String min, String max) {
    	this.min = min;
    	this.max = max;
        this.dateTimeGenerator = new DateTimeGenerator(min, max);
    }
    
    public void setMin(String min) {
		this.min = min;
	}
    
    public void setMax(String max) {
		this.max = max;
	}
}
