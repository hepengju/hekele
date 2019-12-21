package com.hepengju.hekele.admin.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hepengju.hekele.admin.bo.User;
import com.hepengju.hekele.admin.service.UserService;
import com.hepengju.hekele.base.core.R;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "用户管理")
@RestController
@RequestMapping("/admin/user")
public class UserController {

    @Autowired private UserService userService;

    @GetMapping("/list")
    public R list(Long pageNum, Long pageSize, User user) {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.lambda()
                .eq(StringUtils.hasText(user.getUserCode()) , User::getUserCode, user.getUserCode());
        return userService.listR(pageNum, pageSize, wrapper);
    }
}
