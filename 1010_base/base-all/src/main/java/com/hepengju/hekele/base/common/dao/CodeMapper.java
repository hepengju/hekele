package com.hepengju.hekele.base.common.dao;

import com.hepengju.hekele.base.common.bo.Code;
import com.hepengju.hekele.base.core.HeMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CodeMapper extends HeMapper<Code> {
    // 清空表, 开发批量插入使用
    @Update("truncate table z00_code")
    void truncate();
}
