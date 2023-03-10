package com.minio.server.service;

import com.minio.server.pojo.AdminRole;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author bin
 * @since 2022-03-20
 */
public interface IAdminRoleService extends IService<AdminRole> {

    void register(int adminId, int i);
}
