package com.hepengju.hekele.admin.dao;

import com.hepengju.hekele.admin.bo.Code;
import com.hepengju.hekele.base.core.HeMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CodeMapper extends HeMapper<Code> {

    void truncate();
}
