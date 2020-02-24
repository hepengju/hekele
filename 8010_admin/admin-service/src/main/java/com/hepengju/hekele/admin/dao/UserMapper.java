package com.hepengju.hekele.admin.dao;

import com.hepengju.hekele.admin.bo.User;
import com.hepengju.hekele.base.core.HeMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends HeMapper<User> {
}
