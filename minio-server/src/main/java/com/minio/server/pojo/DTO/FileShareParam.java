package com.minio.server.pojo.DTO;

import io.swagger.annotations.ApiModelProperty;

/**
 * @program: minio_sever
 * @description：分享文件DTO
 * @author: bin
 * @create: 2022-05-23 11:06
 **/
public class FileShareParam {

    @ApiModelProperty(value = "文件集合")
    private String files;

    @ApiModelProperty(value = "过期日期")
    private String endTime;




}
