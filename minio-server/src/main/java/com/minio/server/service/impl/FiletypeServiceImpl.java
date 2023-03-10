package com.minio.server.service.impl;

import com.minio.server.pojo.Filetype;
import com.minio.server.mapper.FiletypeMapper;
import com.minio.server.service.IFiletypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bin
 * @since 2022-04-09
 */
@Service
public class FiletypeServiceImpl extends ServiceImpl<FiletypeMapper, Filetype> implements IFiletypeService {

}
