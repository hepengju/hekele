package com.hepengju.hekele.admin.service;

import com.hepengju.hekele.admin.bo.User;
import com.hepengju.hekele.admin.dao.UserMapper;
import com.hepengju.hekele.base.core.HeService;
import org.springframework.stereotype.Service;

@Service
public class UserService extends HeService<UserMapper, User> {
}
