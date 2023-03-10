package com.minio.server.controller;

import com.minio.server.pojo.Admin;
import com.minio.server.pojo.RespBean;
import com.minio.server.service.IAdminRoleService;
import com.minio.server.service.IAdminService;
import com.minio.server.utils.MailServiceUtil;
import com.minio.server.pojo.DTO.AdminRegisterParam;
import com.minio.server.utils.MinioUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * @program: minio
 * @description：注册接口
 * @author: bin
 * @create: 2022-03-09 17:38
 **/

@RestController
@RequestMapping("/api")
public class RegisterController {
    @Resource
    MailServiceUtil mailServiceUtil;
    @Resource
    IAdminService adminService;
    @Resource
    IAdminRoleService adminRoleService;
    @Resource
    MinioUtil minioUtil;


    @PostMapping("/register")
    @ApiOperation(value = "注册用户")
    @ResponseBody
    public RespBean registerUser(@RequestBody AdminRegisterParam adminRegisterParam) {
        int bol = mailServiceUtil.yanZheng(adminRegisterParam.getEmail(), adminRegisterParam.getEmailCode());
        if (bol==1) {
            //加密用户密码
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            try {
                Admin user = new Admin();
                user.setUsername(adminRegisterParam.getUsername());
                user.setPassword(bCryptPasswordEncoder.encode(adminRegisterParam.getPassword()));
                user.setEmail(adminRegisterParam.getEmail());
                user.setName("MinIO用户");
                user.setEnabled(true);
                int adminId = adminService.register(user);
                System.out.print(adminId);
                adminRoleService.register(adminId,3);
                minioUtil.createBucket(adminRegisterParam.getUsername());
                return RespBean.success("注册成功");
            }
            catch (Exception e){
                return RespBean.error("注册失败，没道理失败");
            }
        }
        if(bol==2){
            return RespBean.error("验证码已过期");
        }
        else{
            return RespBean.error("验证码错误");
        }
    }
}
