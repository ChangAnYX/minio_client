package com.minio.server.pojo.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @program: minio_sever
 * @description：文件名模糊查询DTO
 * @author: bin
 * @create: 2022-05-20 14:35
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="按名模糊查对象", description="")
public class SelectFileParam {
    @ApiModelProperty(value = "文件名")
    private String fileName;
    @ApiModelProperty(value = "当前页码")
    private Long currentPage;
    @ApiModelProperty(value = "一页显示数量")
    private Long pageCount;
}
