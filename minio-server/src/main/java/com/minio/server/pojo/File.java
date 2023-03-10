package com.minio.server.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文件信息表
 * </p>
 *
 * @author bin
 * @since 2022-02-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_file")
@ApiModel(value="File对象", description="文件信息表")
public class File implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private int id;

    @ApiModelProperty(value = "文件名称")
    private String file_name;

    @ApiModelProperty(value = "文件类型")
    private String file_type;

    @ApiModelProperty(value = "上传日期")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime file_date;

    @ApiModelProperty(value = "删除日期")
    private String file_del_date;

    @ApiModelProperty(value = "文件大小")
    private Long file_size;

    @ApiModelProperty(value = "文件路径")
    private String file_url;

    @ApiModelProperty(value = "MD5唯一标识")
    private String file_MD5;

    @ApiModelProperty(value = "状态 0未删除，1已删除")
    private boolean is_delete;

    @ApiModelProperty(value = "状态 0不是文件夹，1是文件夹")
    private boolean is_directory;

    @ApiModelProperty(value = "存储桶")
    private String file_bucket_name;

    @ApiModelProperty(value = "文件存储IDwork名")
    private String file_inbuck_name;

    @ApiModelProperty(value = "分享时间")
    @TableField(exist = false)
    private List<Menu> children;
}
