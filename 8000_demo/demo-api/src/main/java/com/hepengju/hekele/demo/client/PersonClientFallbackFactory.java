package com.hepengju.hekele.demo.client;

import com.hepengju.hekele.base.cloud.FeignFallbackUtil;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class PersonClientFallbackFactory implements FallbackFactory<PersonClient> {
    @Override
    public PersonClient create(Throwable cause) {
        return FeignFallbackUtil.get(PersonClient.class, cause);
    }
}
