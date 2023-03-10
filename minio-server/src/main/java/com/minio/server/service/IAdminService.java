package com.minio.server.service;

import com.minio.server.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.minio.server.pojo.RespBean;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 用户信息 服务类
 * </p>
 *
 * @author bin
 * @since 2022-02-08
 */
public interface IAdminService extends IService<Admin> {
    /**
     * 登陆后返回token
     * @param username
     * @param password
     * @param request
     * @return
     */
    RespBean login(String username, String password, String code, HttpServletRequest request);


    /**
     * 根据用户名获取用户对象
     * @param username
     * @return
     */
    Admin getAdminByUserName(String username);


    /**
     * 注册用户
     * @param user
     * @return
     */
    int register(Admin user);




}
