package com.kenny.service;

import com.kenny.domain.AdminRoleDto;
import com.kenny.domain.RoleStatDto;
import com.kenny.mbg.model.UmsAdmin;

import java.util.List;

import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * 后台用户管理Service
 */
public interface UmsAdminService {

    void create(UmsAdmin entity);

    void updateById(UmsAdmin entity);

    void delete(Long id);

    UmsAdmin selectById(Long id);

    List<UmsAdmin> listAll(Integer pageNum, Integer pageSize);


    List<UmsAdmin> list(Integer pageNum, Integer pageSize, String username, List<Integer> statusList);

    List<UmsAdmin> lambdalist(Integer pageNum, Integer pageSize, String username, List<Integer> statusList);

    List<UmsAdmin> subList(Long roleId);

    List<UmsAdmin> lambdaSubList(Long roleId);

    List<RoleStatDto> groupList();

    void deleteByUsername(String username);

    void lambdaDeleteByUsername(String username);

    void updateByIds(List<Long> ids, Integer status);

    void lambdaUpdateByIds(List<Long> ids, Integer status);

    AdminRoleDto selectWithRoleList(Long id);

//    List<RoleStatDto> lambdaGroupLis1t();
}
