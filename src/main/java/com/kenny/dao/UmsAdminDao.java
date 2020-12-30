package com.kenny.dao;

import com.kenny.domain.AdminRoleDto;
import com.kenny.domain.RoleStatDto;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;

import java.util.List;

@Mapper
public interface UmsAdminDao {

    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @Results(id = "RoleStatResult", value = {
            @Result(column = "roleId", property = "roleId", jdbcType = JdbcType.BIGINT, id = true),
            @Result(column = "roleName", property = "roleName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "count", property = "count", jdbcType = JdbcType.INTEGER)
    })
    List<RoleStatDto> groupList(SelectStatementProvider selectStatementProvider);
//    @Results(id = "RoleStatResult", value = {
//            @Result(column = "roleId", property = "roleId", jdbcType = JdbcType.BIGINT, id = true),
//            @Result(column = "roleName", property = "roleName", jdbcType = JdbcType.VARCHAR),
//            @Result(column = "count", property = "count", jdbcType = JdbcType.INTEGER)
//    })
//    default List<RoleStatDto> groupList(SelectDSLCompleter completer);

    @SelectProvider(type = SqlProviderAdapter.class, method = "select")
    @ResultMap("AdminRoleResult")
    AdminRoleDto selectWithRoleList(SelectStatementProvider selectStatement);


}
