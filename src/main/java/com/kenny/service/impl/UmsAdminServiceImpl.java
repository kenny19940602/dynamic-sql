package com.kenny.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.github.pagehelper.PageHelper;
import com.kenny.dao.UmsAdminDao;
import com.kenny.domain.AdminRoleDto;
import com.kenny.domain.RoleStatDto;
import com.kenny.mbg.mapper.UmsAdminDynamicSqlSupport;
import com.kenny.mbg.mapper.UmsAdminMapper;
import com.kenny.mbg.mapper.UmsAdminRoleRelationDynamicSqlSupport;
import com.kenny.mbg.mapper.UmsRoleDynamicSqlSupport;
import com.kenny.mbg.model.UmsAdmin;
import com.kenny.service.UmsAdminService;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.render.RenderingStrategies;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mybatis.dynamic.sql.SqlBuilder.*;

/**
 * 后台用户管理Service实现类
 */
@Service
public class UmsAdminServiceImpl implements UmsAdminService {

    @Autowired
    private UmsAdminMapper adminMapper;
    @Autowired
    private UmsAdminDao adminDao;

    @Override
    public void create(UmsAdmin entity) {
        adminMapper.insert(entity);
    }

    @Override
    public void updateById(UmsAdmin entity) {
        adminMapper.updateByPrimaryKeySelective(entity);
    }

    @Override
    public void delete(Long id) {
        adminMapper.deleteByPrimaryKey(id);
    }

    @Override
    public UmsAdmin selectById(Long id) {
        Optional<UmsAdmin> umsAdmin = adminMapper.selectByPrimaryKey(id);
        return umsAdmin.orElse(null);
    }

    @Override
    public List<UmsAdmin> listAll(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return adminMapper.select(SelectDSLCompleter.allRows());
    }

    @Override
    public List<UmsAdmin> list(Integer pageNum, Integer pageSize, String username, List<Integer> statusList) {
        PageHelper.startPage(pageNum, pageSize);
        SelectStatementProvider selectStatementProvider = select(UmsAdminMapper.selectList)
                .from(UmsAdminDynamicSqlSupport.umsAdmin)
                .where(UmsAdminDynamicSqlSupport.username, isEqualTo(username))
                .and(UmsAdminDynamicSqlSupport.status, isIn(statusList))
                .orderBy(UmsAdminDynamicSqlSupport.createTime.descending())
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return adminMapper.selectMany(selectStatementProvider);
    }

    @Override
    public List<UmsAdmin> lambdalist(Integer pageNum, Integer pageSize, String username, List<Integer> statusList) {
        PageHelper.startPage(pageNum, pageSize);
        return adminMapper.select(c -> c.where(UmsAdminDynamicSqlSupport.username, isEqualToWhenPresent(username))
                .and(UmsAdminDynamicSqlSupport.status, isIn(statusList))
                .orderBy(UmsAdminDynamicSqlSupport.createTime.descending()));
    }

    @Override
    public List<UmsAdmin> subList(Long roleId) {
        SelectStatementProvider selectStatementProvider = select(UmsAdminMapper.selectList)
                .from(UmsAdminDynamicSqlSupport.umsAdmin)
                .where(UmsAdminDynamicSqlSupport.id, isIn(select(UmsAdminRoleRelationDynamicSqlSupport.adminId)
                        .from(UmsAdminRoleRelationDynamicSqlSupport.umsAdminRoleRelation)
                        .where(UmsAdminRoleRelationDynamicSqlSupport.roleId, isEqualTo(roleId))))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return adminMapper.selectMany(selectStatementProvider);
    }

    @Override
    public List<UmsAdmin> lambdaSubList(Long roleId) {
        return adminMapper.select(c -> c.where(UmsAdminDynamicSqlSupport.id
                , isIn(select(UmsAdminRoleRelationDynamicSqlSupport.adminId)
                        .from(UmsAdminRoleRelationDynamicSqlSupport.umsAdminRoleRelation)
                        .where(UmsAdminRoleRelationDynamicSqlSupport.roleId, isEqualTo(roleId)))));

    }

    @Override
    public List<RoleStatDto> groupList() {
        SelectStatementProvider selectStatementProvider = select(UmsRoleDynamicSqlSupport.id.as("roleId")
                , UmsRoleDynamicSqlSupport.name.as("roleName")
                , count(UmsAdminDynamicSqlSupport.id).as("count"))
                .from(UmsRoleDynamicSqlSupport.umsRole)
                .leftJoin(UmsAdminRoleRelationDynamicSqlSupport.umsAdminRoleRelation)
                .on(UmsRoleDynamicSqlSupport.id, equalTo(UmsAdminRoleRelationDynamicSqlSupport.roleId))
                .leftJoin(UmsAdminDynamicSqlSupport.umsAdmin)
                .on(UmsAdminRoleRelationDynamicSqlSupport.adminId, equalTo(UmsAdminDynamicSqlSupport.id))
                .groupBy(UmsRoleDynamicSqlSupport.id)
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return adminDao.groupList(selectStatementProvider);
    }

//    @Override
//    public List<RoleStatDto> lambdaGroupLis1t() {
//        return adminDao.groupList(c -> select(UmsRoleDynamicSqlSupport.id.as("roleId")
//                , UmsRoleDynamicSqlSupport.name.as("roleName")
//                , count(UmsAdminDynamicSqlSupport.id).as("count"))
//                .from(UmsRoleDynamicSqlSupport.umsRole)
//                .leftJoin(UmsAdminRoleRelationDynamicSqlSupport.umsAdminRoleRelation)
//                .on(UmsRoleDynamicSqlSupport.id, equalTo(UmsAdminRoleRelationDynamicSqlSupport.roleId))
//                .leftJoin(UmsAdminDynamicSqlSupport.umsAdmin)
//                .on(UmsAdminRoleRelationDynamicSqlSupport.adminId, equalTo(UmsAdminDynamicSqlSupport.id))
//                .groupBy(UmsRoleDynamicSqlSupport.id)
//        );
//    }

    @Override
    public void deleteByUsername(String username) {
        DeleteStatementProvider deleteStatementProvider = deleteFrom(UmsAdminDynamicSqlSupport.umsAdmin)
                .where(UmsAdminDynamicSqlSupport.username, isEqualToWhenPresent(username))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        adminMapper.delete(deleteStatementProvider);
    }

    @Override
    public void lambdaDeleteByUsername(String username) {
        adminMapper.delete(c->c.where(UmsAdminDynamicSqlSupport.username,isEqualToWhenPresent(username)));
    }


    @Override
    public void updateByIds(List<Long> ids, Integer status) {
        UpdateStatementProvider updateStatementProvider = update(UmsAdminDynamicSqlSupport.umsAdmin)
                .set(UmsAdminDynamicSqlSupport.status)
                .equalToWhenPresent(status)
                .where(UmsAdminDynamicSqlSupport.id, isIn(ids))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        adminMapper.update(updateStatementProvider);
    }

    @Override
    public void lambdaUpdateByIds(List<Long> ids, Integer status) {
        adminMapper.update(c->c.set(UmsAdminDynamicSqlSupport.status)
                .equalToWhenPresent(status)
                .where(UmsAdminDynamicSqlSupport.id, isIn(ids)));
    }

    @Override
    public AdminRoleDto selectWithRoleList(Long id) {
        ArrayList<BasicColumn> columnList = new ArrayList<>(CollUtil.toList(UmsAdminMapper.selectList));
        columnList.add(UmsRoleDynamicSqlSupport.id.as("role_id"));
        columnList.add(UmsRoleDynamicSqlSupport.name.as("role_name"));
        columnList.add(UmsRoleDynamicSqlSupport.description.as("role_description"));
        columnList.add(UmsRoleDynamicSqlSupport.createTime.as("role_create_time"));
        columnList.add(UmsRoleDynamicSqlSupport.status.as("role_status"));
        columnList.add(UmsRoleDynamicSqlSupport.sort.as("role_sort"));
        SelectStatementProvider selectStatementProvider = select(columnList)
                .from(UmsAdminDynamicSqlSupport.umsAdmin)
                .leftJoin(UmsAdminRoleRelationDynamicSqlSupport.umsAdminRoleRelation)
                .on(UmsAdminDynamicSqlSupport.id, equalTo(UmsAdminRoleRelationDynamicSqlSupport.adminId))
                .leftJoin(UmsRoleDynamicSqlSupport.umsRole)
                .on(UmsAdminRoleRelationDynamicSqlSupport.roleId, equalTo(UmsRoleDynamicSqlSupport.id))
                .where(UmsAdminDynamicSqlSupport.id, isEqualToWhenPresent(id))
                .build()
                .render(RenderingStrategies.MYBATIS3);
        return adminDao.selectWithRoleList(selectStatementProvider);
    }

}
