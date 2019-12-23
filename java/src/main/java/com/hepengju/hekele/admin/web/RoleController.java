package com.hepengju.hekele.admin.web;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hepengju.hekele.admin.bo.Menu;
import com.hepengju.hekele.admin.bo.Role;
import com.hepengju.hekele.admin.bo.User;
import com.hepengju.hekele.admin.dao.RoleMapper;
import com.hepengju.hekele.admin.service.RoleService;
import com.hepengju.hekele.base.core.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Api(tags = "角色管理")
@RestController
@RequestMapping("/admin/role/")
public class RoleController {

    @Autowired private RoleService roleService;
    @Autowired private RoleMapper  roleMapper;

    @ApiOperation("查询所有")
    @GetMapping("list")
    public R list(Long pageNum, Long pageSize, Role role){
        QueryWrapper<Role> wrapper = new QueryWrapper<Role>();
        wrapper.lambda()
                .like(StringUtils.hasText(role.getRoleCodeOrName()), Role::getRoleCode, role.getRoleCodeOrName())
                .or()
                .like(StringUtils.hasText(role.getRoleCodeOrName()), Role::getRoleName, role.getRoleCodeOrName())
                .orderByDesc(Role::getCreateTime);
        return roleService.listR(pageNum, pageSize, wrapper);
    }

    @ApiOperation("新增角色")
    @PostMapping("add")
    public R add(@Valid Role role) {
        roleService.validUnique("role_code", role.getRoleCode(), null, "role.roleCode.unique");
        roleService.validUnique("role_name", role.getRoleCode(), null, "role.roleName.unique");
        return roleService.addR(role);
    }

    @ApiOperation("新增角色")
    @PostMapping("add2")
    public R add2(@Valid @RequestBody Role role) {
        roleService.validUnique("role_code", role.getRoleCode(), null, "role.roleCode.unique");
        roleService.validUnique("role_name", role.getRoleCode(), null, "role.roleName.unique");
        return roleService.addR(role);
    }

    @ApiOperation("编辑角色")
    @PostMapping("edit")
    public R edit(Role role) {
        roleService.validUnique("role_code", role.getRoleCode(), null, "role.roleCode.unique");
        roleService.validUnique("role_name", role.getRoleCode(), null, "role.roleName.unique");
        return roleService.editR(role);
    }

    @ApiOperation("删除角色")
    @PostMapping("delete")
    public R delete(@RequestParam("roleId") List<String> idList) { return roleService.deleteRole(idList); }

    @ApiOperation("启用角色")
    @PostMapping("enable")
    public R enable(@RequestParam("roleId") List<String> idList) { return roleService.enableBatchByIds(idList); }

    @ApiOperation("禁用角色")
    @PostMapping("disable")
    public R disable(@RequestParam("roleId") List<String> idList) { return roleService.deleteRole(idList); }

    @ApiOperation("根据主键查询")
    @GetMapping("getById")
    public R getById(String roleId){ return roleService.getRById(roleId);}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @ApiOperation("根据主键查询关联用户")
    @GetMapping("listUser")
    public R listUser(String roleId) {
        List<User> userList = roleMapper.listUser(roleId);
        return R.ok().addData(userList);
    }

    @ApiOperation("根据主键查询关联菜单")
    @GetMapping("listMenu")
    public R listMenu(String roleId) {
        List<Menu> menuList = roleMapper.listMenu(roleId);
        return R.ok().addData(menuList);
    }

    @ApiOperation("角色添加用户")
    @PostMapping("addUser")
    public R addUser(@RequestParam("roleId") List<String> roleIdList, @RequestParam("userId") List<String> userIdList) {
        return roleService.addUser(roleIdList, userIdList);
    }

    @ApiOperation("角色分配菜单")
    @PostMapping("assignMenu")
    public R assignMenu(String roleId, @RequestParam("menuId")List<String> menuIdList) {
        return roleService.assignMenu(roleId, menuIdList);
    }
}
