package com.minio.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.minio.server.mapper.AdminRoleMapper;
import com.minio.server.pojo.AdminRole;
import com.minio.server.service.IAdminRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bin
 * @since 2022-02-25
 */
@Service
public class AdminRoleServiceImpl extends ServiceImpl<AdminRoleMapper, AdminRole> implements IAdminRoleService {

    @Resource
    AdminRoleMapper adminRoleMapper;

    @Override
    public void register(int adminId, int i) {
        AdminRole userRole =new AdminRole();
        userRole.setAdminId(adminId);
        userRole.setRid(i);
        adminRoleMapper.insert(userRole);
    }


}
