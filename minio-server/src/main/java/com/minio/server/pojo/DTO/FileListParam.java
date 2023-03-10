package com.minio.server.pojo.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: minio_sever
 * @description：文件遍历接口
 * @author: bin
 * @create: 2022-04-10 18:40
 **/
@Data
public class FileListParam {
    @ApiModelProperty(value = "文件路径")
    private String filePath;
    @ApiModelProperty(value = "当前页码")
    private Long currentPage;
    @ApiModelProperty(value = "一页显示数量")
    private Long pageCount;
}
