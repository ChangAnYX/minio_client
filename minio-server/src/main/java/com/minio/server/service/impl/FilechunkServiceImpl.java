package com.minio.server.service.impl;

import com.minio.server.pojo.Filechunk;
import com.minio.server.mapper.FilechunkMapper;
import com.minio.server.service.IFilechunkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author bin
 * @since 2022-04-22
 */
@Service
public class FilechunkServiceImpl extends ServiceImpl<FilechunkMapper, Filechunk> implements IFilechunkService {

}
