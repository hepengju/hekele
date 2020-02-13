package com.hepengju.hekele.base.cloud;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页查询
 *
 * <br> 在某些情况下，feign接口中只能传输一个参数，此时可以使用PageQuery (@RequestBody)
 *
 * @author hepengju
 */
@ApiModel("分页查询") @Data
public class PageQuery<T> {

    @ApiModelProperty("当前页")   private Long pageNum;
    @ApiModelProperty("每页大小") private Long pageSize;
    @ApiModelProperty("排序字段") private String orderBy;
    @ApiModelProperty("查询对象") private T queryObject;

    public T getQueryInstance(Class<T> queryObjectClass){
        // 已经实例化直接返回
        if (queryObject != null) return queryObject;

        // 否则进行获取fan'hse反射获取
        try {
            return (T) queryObjectClass.newInstance();  } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
