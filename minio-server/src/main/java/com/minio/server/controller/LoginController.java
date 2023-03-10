package com.minio.server.controller;

import com.minio.server.pojo.Admin;
import com.minio.server.pojo.DTO.AdminLoginParam;
import com.minio.server.pojo.RespBean;
import com.minio.server.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

/**
 * 登录接口
 *
 * @author bin
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api")
public class LoginController {

	@Resource
	private IAdminService adminService;

	@PostMapping("/login")
	@ApiOperation(value = "登录")
	@ResponseBody
	public RespBean login(@RequestBody AdminLoginParam adminLoginParam,HttpServletRequest request) {
		return adminService.login(adminLoginParam.getUsername(), adminLoginParam.getPassword(), adminLoginParam.getCode(), request);
	}

	@GetMapping("/admin/info")
	@ApiOperation(value = "获取当前登录用户信息")
	public Admin getAdminInfo(Principal principal) {
		if (null == principal) {
			return null;
		}
		Admin admin = adminService.getAdminByUserName(principal.getName());
		admin.setPassword(null);
		return admin;
	}


	@PostMapping("/logout")
	@ApiOperation(value = "退出登录")
	public RespBean logout() {
		return RespBean.success("注销成功");
	}

}
