package com.minio.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.minio.server.pojo.File;
import com.minio.server.mapper.FileMapper;
import com.minio.server.service.IFileService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 文件信息表 服务实现类
 * </p>
 *
 * @author bin
 * @since 2022-02-27
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {
    @Resource
    private FileMapper fileMapper;

    @Override
    public List<File> getFilesByPath(String bucketName, String url, Long CurrentPage, Long PageCount) {
        Long beginCount = (CurrentPage - 1) * PageCount;
        return fileMapper.getFilesBy(bucketName,url,beginCount, PageCount);
    }

    @Override
    public List<File> getFilesBytype(String bucketName, String fileType, Long CurrentPage, Long PageCount) {
        Long beginCount = (CurrentPage - 1) * PageCount;
        return  fileMapper.getFilesBytype(bucketName,fileType,beginCount, PageCount);
    }

    @Override
    public List<File> listByName(String bucketName, String filename,Long CurrentPage, Long PageCount ) {
        Long beginCount = (CurrentPage - 1) * PageCount;
        return fileMapper.listByName(bucketName,filename,beginCount,PageCount);
           }

    @Override
    public Long sumBybuck(String username) {
        return fileMapper.sumBybuck(username);
    }

    @Override
    public void updatePath(String bucketName, String fileOldName, String fileNewName, String file_url) {
        String url = file_url+fileOldName+"/%";
        fileMapper.updatePath(bucketName, fileNewName, url, fileOldName);
    }

    @Override
    public List<File> getfileRecycle(String bucketName, Long currentPage, Long pageCount) {
        Long beginCount = (currentPage - 1) * pageCount;
        return fileMapper.getfileRecycle(bucketName,beginCount, pageCount);
    }

    @Override
    public void RestoreTrash(String bucketName) {
        fileMapper.RestoreTrash(bucketName);
    }

    @Override
    public List<File> selectFilePathTreeByBucket(String bucketName) {
        LambdaQueryWrapper<File> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(File::getFile_bucket_name, bucketName)
                .isNull(File::getFile_size).isNull(File::getFile_del_date);
        return fileMapper.selectList(lambdaQueryWrapper);
    }

    @Override
    public void UpFilePath(String bucketName, String fileOldPath, String fileNewPath) {
        String fileURL = fileOldPath+"%";
        fileMapper.UpFilePath(bucketName,fileNewPath,fileOldPath,fileURL);
    }

    @Override
    public void setDelTimeNull(int id) {
        fileMapper.setDelTimeNull(id);
    }


}
