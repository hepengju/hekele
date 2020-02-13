package com.hepengju.hekele.data.client;

import com.hepengju.hekele.base.cloud.FeignFallbackUtil;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class DataClientFallbackFactory implements FallbackFactory<DataClient> {
    @Override
    public DataClient create(Throwable cause) {
        return FeignFallbackUtil.get(DataClient.class, cause);
    }
}
