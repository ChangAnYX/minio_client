package com.minio.server.pojo.DTO;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @program: minio_sever
 * @description：文件修改接口参数
 * @author: bin
 * @create: 2022-04-10 10:51
 **/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="文件修改对象", description="")
public class FileUpdateParam {
    @ApiModelProperty(value = "主键")
    private int id;

    @ApiModelProperty(value = "文件名称")
    private String file_name;

    @ApiModelProperty(value = "文件路径/要移动路径")
    private String file_url;

    @ApiModelProperty(value = "状态 0未删除，1已删除")
    private boolean is_delete;

    @ApiModelProperty(value = "状态 0不是文件夹，1是文件夹")
    private boolean is_directory;

}
