package com.hepengju.hekele.demo;

import com.hepengju.hekele.base.core.HeMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PersonMapper extends HeMapper<Person> {
}
