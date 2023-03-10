package com.minio.server.pojo.VO;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: minio_sever
 * @description：遍历目录树
 * @author: bin
 * @create: 2022-04-13 18:02
 **/
@Data
public class TreeNodeVO {

    @ApiModelProperty(value = "节点id")
    private Long id;

    @ApiModelProperty(value = "节点名")
    private String label;

    @ApiModelProperty(value = "深度")
    private Long depth;

    @ApiModelProperty(value = "是否被关闭")
    private String state = "closed";

    @ApiModelProperty(value = "属性集合")
    private Map<String, String> attributes = new HashMap<>();

    @ApiModelProperty(value = "子节点列表")
    private List<TreeNodeVO> children = new ArrayList<>();

}
