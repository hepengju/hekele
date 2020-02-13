package com.hepengju.hekele.demo.dao;

import com.hepengju.hekele.base.core.HeMapper;
import com.hepengju.hekele.demo.bo.Person;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PersonMapper extends HeMapper<Person> {
}
