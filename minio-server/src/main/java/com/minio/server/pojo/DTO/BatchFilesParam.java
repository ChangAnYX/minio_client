package com.minio.server.pojo.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: minio_sever
 * @description：批量操作文件接口
 * @author: bin
 * @create: 2022-04-25 19:09
 **/
@Data
public class BatchFilesParam {
    @ApiModelProperty(value = "文件集合")
    private String files;
    @ApiModelProperty(value = "文件要修改路径")
    private String filePath;
}
