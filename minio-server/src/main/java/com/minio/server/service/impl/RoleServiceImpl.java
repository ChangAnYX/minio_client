package com.minio.server.service.impl;

import com.minio.server.pojo.Role;
import com.minio.server.mapper.RoleMapper;
import com.minio.server.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bin
 * @since 2022-03-20
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
