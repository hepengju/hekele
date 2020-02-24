package com.hepengju.hekele.data.client;

import com.hepengju.hekele.base.cloud.FeignFallbackUtil;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class DataFeignClientFallbackFactory implements FallbackFactory<DataFeignClient> {
    @Override
    public DataFeignClient create(Throwable cause) {
        return FeignFallbackUtil.get(DataFeignClient.class, cause);
    }
}
