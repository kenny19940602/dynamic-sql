package com.kenny.service;

import com.kenny.mbg.model.UmsAdmin;

import java.util.List;

/**
 * 后台用户管理Service
 */
public interface UmsAdminService {

    void create(UmsAdmin entity);

    void update(UmsAdmin entity);

    void delete(Long id);

    UmsAdmin selectById(Long id);

    List<UmsAdmin> listAll(Integer pageNum, Integer pageSize);


    List<UmsAdmin> list(Integer pageNum, Integer pageSize, String username, List<Integer> statusList);
}
