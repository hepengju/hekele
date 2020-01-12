package com.hepengju.hekele.data.generator.date;

import com.hepengju.hekele.base.util.DateUtil;
import com.hepengju.hekele.data.generator.Generator;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class LocalDateTimeGenerator implements Generator<LocalDateTime> {

    @Getter @Setter private Date min = DateUtil.stringToDate("1900-01-01 00:00:00");
    @Getter @Setter private Date max = DateUtil.stringToDate("2100-12-31 23:59:59");

    private DateTimeGenerator dateTimeGenerator;

    @Override
    public LocalDateTime generate() {
        if (dateTimeGenerator == null) {
            dateTimeGenerator = new DateTimeGenerator(min, max);
        }
        return dateTimeGenerator.generate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
