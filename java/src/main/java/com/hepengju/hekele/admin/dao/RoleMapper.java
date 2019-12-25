package com.hepengju.hekele.admin.dao;

import com.hepengju.hekele.admin.bo.Menu;
import com.hepengju.hekele.admin.bo.Role;
import com.hepengju.hekele.admin.bo.User;
import com.hepengju.hekele.base.core.HeMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper extends HeMapper<Role> {

    @Select("SELECT * FROM z10_user WHERE delete_flag = '1' AND user_id IN (SELECT user_id FROM z11_user_role WHERE role_id = #{id}")
    List<User> listUser(String id);

    @Select("SELECT * FROM z10_menu WHERE delete_flag = '1' AND menu_id IN (SELECT menu_id FROM z11_role_menu WHERE role_id = #{id}")
    List<Menu> listMenu(String id);
}
