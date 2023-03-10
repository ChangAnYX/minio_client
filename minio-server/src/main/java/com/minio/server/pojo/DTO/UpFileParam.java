package com.minio.server.pojo.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: minio_sever
 * @description：上传文件接口
 * @author: bin
 * @create: 2022-04-21 08:34
 **/

@Data
public class UpFileParam {

    @ApiModelProperty(value = "文件路径")
    private String filePath;

    @ApiModelProperty(value = "上传时间")
    private String uploadTime;

    @ApiModelProperty(value = "文件名")
    private String filename;

    @ApiModelProperty(value = "文件大小")
    private Long fileSize;

    @ApiModelProperty(value = "切片数量")
    private int chunkNumber;

    @ApiModelProperty(value = "md5码")
    private String identifier;

}
