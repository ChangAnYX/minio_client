package com.minio.server.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共返回对象
 *
 * @author bin
 * @since 1.0.0
 */
@Data
@NoArgsConstructor
@ApiModel(value="公共返回对象", description="")
public class RespBean {

    private Integer code;
    private String message;
    private Object data;

    public static RespBean build() {
        return new RespBean();
    }


    public Integer getCode() {
        return code;
    }

    public RespBean setCode(Integer code) {
        this.code = code;
        return this;
    }

    public RespBean(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 成功，带信息
     * @param message
     * @return
     */
    public static RespBean success(String message){
        return new RespBean(200,message,null);
    }


    /**
     * 成功，带信息和对象
     * @param message
     * @return
     */
    public static RespBean success(String message,Object data){
        return new RespBean(200,message,data);
    }


    /**
     * 失败，带信息
     * @param message
     * @return
     */
    public static RespBean error(String message){
        return new RespBean(500,message,null);
    }


    /**
     * 失败，带信息和对象
     * @param message
     * @return
     */
    public static RespBean error(String message,Object data){
        return new RespBean(500,message,data);
    }
}
