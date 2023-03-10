package com.minio.server.controller;

import com.minio.server.pojo.DTO.AdminRegisterParam;
import com.minio.server.pojo.RespBean;
import com.minio.server.service.IFileService;
import com.minio.server.utils.MinioUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @program: minio_sever
 * @description：分享文件接口类
 * @author: bin
 * @create: 2022-05-20 11:03
 **/

@RestController
@RequestMapping("/api/share")
public class ShareController {

    @Resource
    private IFileService fileService;
    @Resource
    private MinioUtil minioUtil;

    @PostMapping("/")
    @ApiOperation(value = "分享文件")
    @ResponseBody
    public RespBean ShareFile(@RequestBody AdminRegisterParam adminRegisterParam) {


        return null;
    }
}
