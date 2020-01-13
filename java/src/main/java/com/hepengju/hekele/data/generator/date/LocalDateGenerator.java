package com.hepengju.hekele.data.generator.date;

import com.hepengju.hekele.data.generator.Generator;

import java.time.LocalDate;
import java.time.ZoneId;

public class LocalDateGenerator implements Generator<LocalDate> {

    private String min = "1900-01-01";
    private String max = "2100-12-31";
    private DateTimeGenerator dateTimeGenerator;

    public LocalDate generate() {
        return dateTimeGenerator.generate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public LocalDateGenerator() {
        this.dateTimeGenerator = new DateTimeGenerator();
    }

    public LocalDateGenerator(String min, String max) {
        this.dateTimeGenerator = new DateTimeGenerator(min, max);
    }
}