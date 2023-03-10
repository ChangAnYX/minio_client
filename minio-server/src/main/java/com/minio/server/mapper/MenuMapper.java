package com.minio.server.mapper;

import com.minio.server.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author bin
 * @since 2022-03-20
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户ID获取菜单
     * @param adminId
     * @return
     */
    List<Menu> getMenusByAdminId(Integer adminId);
}
