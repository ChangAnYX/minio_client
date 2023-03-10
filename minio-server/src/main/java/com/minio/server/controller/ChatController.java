//package com.xxxx.server.controller;
//
//import Admin;
//import com.xxxx.server.vo.AdminVo;
//import IAdminService;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * @author mornd
// * @date 2022/2/28 - 15:04
// * 在线聊天
// */
//@RestController
//@RequestMapping("/chat")
//public class ChatController {
//    @Resource
//    private IAdminService adminService;
//
//    @ApiOperation("获取所有用戶")
//    @GetMapping("/admin")
//    public List<Admin> getAdmins(String keywords){
//        AdminVo adminVo = new AdminVo();
//        adminVo.setPage(1);
//        adminVo.setSize(1000);
//        return adminService.getAllAdmins(adminVo).getRecords();
//    }
//
//}
