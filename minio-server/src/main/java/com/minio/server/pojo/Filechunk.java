package com.minio.server.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author bin
 * @since 2022-04-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_filechunk")
@ApiModel(value="Filechunk对象", description="")
public class Filechunk implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "文件名")
    private String file_name;

    @ApiModelProperty(value = "文件路径")
    private String file_url;

    @ApiModelProperty(value = "文件大小")
    private Long file_size;

    @ApiModelProperty(value = "文件类型")
    private String file_type;

    @ApiModelProperty(value = "文件MD5")
    private String file_MD5;

    @ApiModelProperty(value = "切片数量")
    private Integer chunkNumber;

    @ApiModelProperty(value = "上传时间")
    private LocalDateTime upload_time;

    @ApiModelProperty(value = "上传桶名")
    private String upbucker_name;

    @ApiModelProperty(value = "Idwork文件名")
    private String Idworker_name;

    @ApiModelProperty(value = "uploadId")
    private String uploadId;


}
