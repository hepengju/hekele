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
        this.dateTimeGenerator = new DateTimeGenerator();
    }

    public LocalDateTimeGenerator(String min, String max) {
        this.dateTimeGenerator = new DateTimeGenerator(min, max);
    }
}
