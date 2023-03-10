package com.minio.server.pojo.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: minio_sever
 * @description：新建文件接口
 * @author: bin
 * @create: 2022-04-11 17:14
 **/

@Data
public class CreateFileParam {
    @ApiModelProperty(value = "文件名")
    private String fileName;
    @ApiModelProperty(value = "文件路径")
    private String filePath;
}
