package com.hepengju.hekele.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.hepengju.hekele.admin.bo.Role;
import com.hepengju.hekele.admin.bo.RoleMenu;
import com.hepengju.hekele.admin.bo.UserRole;
import com.hepengju.hekele.admin.dao.RoleMapper;
import com.hepengju.hekele.admin.dao.RoleMenuMapper;
import com.hepengju.hekele.admin.dao.UserRoleMapper;
import com.hepengju.hekele.base.core.HeService;
import com.hepengju.hekele.base.core.JsonR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService extends HeService<RoleMapper, Role> {

    @Autowired private UserRoleMapper userRoleMapper;
    @Autowired private RoleMenuMapper roleMenuMapper;

    /**
     * 删除角色, 同时删除角色菜单表记录
     */
    @Transactional
    public JsonR deleteRole(List<String> idList) {
        boolean success = this.removeByIds(idList);
        if (success) {
            roleMenuMapper.delete(new UpdateWrapper<RoleMenu>().lambda().in(RoleMenu::getRoleId, idList));
        }
        return JsonR.ok();
    }

    /**
     * 角色添加用户: 查询角色已经拥有的用户, 避免重复添加
     */
    @Transactional
    public JsonR addUser(List<String> roleIdList, List<String> userIdList) {
        if (CollectionUtils.isEmpty(roleIdList) || CollectionUtils.isEmpty(userIdList)) return JsonR.ok();

        List<UserRole> userRoleList = userRoleMapper.selectList(new QueryWrapper<UserRole>().lambda().in(UserRole::getRoleId, roleIdList));

        for (String roleId : roleIdList) {
            List<String> readyUserIdList = userRoleList.stream()
                    .filter(ur -> roleId.equals(ur.getRoleId()))
                    .map(UserRole::getUserId).collect(Collectors.toList());
            userIdList.removeAll(readyUserIdList);
            for (String userId : userIdList) {
                UserRole userRole = new UserRole();
                userRole.setRoleId(roleId);
                userRole.setUserId(userId);
                userRoleMapper.addWithCreate(userRole);
            }
        }
        return JsonR.ok();
    }

    /**
     * 角色分配菜单: 先删除角色之前拥有的菜单, 再添加
     */
    @Transactional
    public JsonR assignMenu(String roleId, List<String> menuIdList) {
        roleMenuMapper.delete(new UpdateWrapper<RoleMenu>().lambda().eq(RoleMenu::getRoleId, roleId));
        for (String menuId : menuIdList) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(menuId);
            roleMenuMapper.addWithCreate(roleMenu);
        }
        return JsonR.ok();
    }
}
