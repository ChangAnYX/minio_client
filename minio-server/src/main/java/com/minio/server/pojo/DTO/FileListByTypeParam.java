package com.minio.server.pojo.DTO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: minio_sever
 * @description：文件分类查询接口
 * @author: bin
 * @create: 2022-04-11 15:00
 **/
@Data
public class FileListByTypeParam {
    @ApiModelProperty(value = "文件类型")
    private String fileType;
    @ApiModelProperty(value = "当前页码")
    private Long currentPage;
    @ApiModelProperty(value = "一页显示数量")
    private Long pageCount;
}
