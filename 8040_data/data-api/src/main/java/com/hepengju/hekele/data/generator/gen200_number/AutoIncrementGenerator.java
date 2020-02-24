package com.hepengju.hekele.data.generator.gen200_number;


import com.hepengju.hekele.data.generator.AbstractNumberGenerator;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.core.annotation.Order;

import java.util.concurrent.atomic.AtomicInteger;

@ApiModel("自增生成器") @Data @Order(203)
public class AutoIncrementGenerator extends AbstractNumberGenerator<Integer> {

    private int min = 0;
    private AtomicInteger atomic;
    private String format = "#";

    @Override
    public Integer generate() {
        return atomic.incrementAndGet();
    }

    public void setMin(int min) {
        this.min = min;
        this.atomic = new AtomicInteger(min);
    }

    public AutoIncrementGenerator() {
        this.setMin(min);
    }

    public AutoIncrementGenerator(int min) {
        this.setMin(min);
    }
}
