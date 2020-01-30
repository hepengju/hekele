package com.hepengju.hekele.admin.web;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hepengju.hekele.admin.bo.Menu;
import com.hepengju.hekele.admin.bo.Role;
import com.hepengju.hekele.admin.bo.User;
import com.hepengju.hekele.admin.dao.RoleMapper;
import com.hepengju.hekele.admin.service.RoleService;
import com.hepengju.hekele.base.core.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 角色管理: 本项目的示例规范CRUD
 *
 * @author he_pe 2019-12-25
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/admin/role/")
public class RoleController {

    @Autowired private RoleService roleService;
    @Autowired private RoleMapper  roleMapper;

    @ApiOperation("查询所有")
    @GetMapping("list")
    public R<Role> list(Long pageNum, Long pageSize, Role role){
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<Role>()
            .eq(StringUtils.isNotBlank(role.getRoleType()), Role::getRoleType, role.getRoleType())
            .eq(StringUtils.isNotBlank(role.getEnableFlag()), Role::getEnableFlag, role.getEnableFlag())
            .and(w -> w.like(StringUtils.isNotBlank(role.getRoleCodeOrName()), Role::getRoleCode, role.getRoleCodeOrName())
                       .or()
                       .like(StringUtils.isNotBlank(role.getRoleCodeOrName()), Role::getRoleName, role.getRoleCodeOrName())
            )
            .orderByDesc(Role::getCreateTime);
        return roleService.listR(pageNum, pageSize, wrapper);
    }

    // 追加 RequestParam 注解, 目的是请求参数为必传项
    @ApiOperation("根据主键查询")
    @GetMapping("getById")
    public R<Role> getById(@RequestParam String roleId) { return roleService.getRById(roleId);}

    @ApiOperation("新增角色")
    @PostMapping("add")
    public R add(@Valid Role role) {
        role.setRoleId(null);
        validRole(role);
        return roleService.addR(role);
    }

    @ApiOperation("编辑角色")
    @PostMapping("edit")
    public R edit(@Valid Role role) {
        validRole(role);
        return roleService.editR(role);
    }

    /**
     * 验证角色代码和名称的唯一性
     */
    private void validRole(Role role){
        roleService.validUnique("role_code", role.getRoleCode(), role.getRoleId(), "role.roleCode.unique");
        roleService.validUnique("role_name", role.getRoleCode(), role.getRoleId(), "role.roleName.unique");
    }

    @ApiOperation("删除角色")
    @PostMapping("delete")
    public R delete(@RequestParam("roleId") List<String> idList) { return roleService.deleteRole(idList); }

    @ApiOperation("启用角色")
    @PostMapping("enable")
    public R enable(@RequestParam("roleId") List<String> idList) { return roleService.enableBatchByIds(idList); }

    @ApiOperation("禁用角色")
    @PostMapping("disable")
    public R disable(@RequestParam("roleId") List<String> idList) { return roleService.disableBatchByIds(idList); }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @ApiOperation("根据主键查询关联用户")
    @GetMapping("listUser")
    public R<User> listUser(@RequestParam String roleId) {
        List<User> userList = roleMapper.listUser(roleId);
        return R.ok().addData(userList);
    }

    @ApiOperation("根据主键查询关联菜单")
    @GetMapping("listMenu")
    public R<Menu> listMenu(@RequestParam String roleId) {
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
    public R assignMenu(@RequestParam String roleId, @RequestParam("menuId")List<String> menuIdList) {
        return roleService.assignMenu(roleId, menuIdList);
    }
}
