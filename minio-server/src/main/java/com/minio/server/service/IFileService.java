package com.minio.server.service;

import com.minio.server.pojo.File;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 文件信息表 服务类
 * </p>
 *
 * @author bin
 * @since 2022-02-27
 */
public interface IFileService extends IService<File> {

    List<File> getFilesByPath(String bucketName, String url, Long CurrentPage, Long PageCount);

    List<File> getFilesBytype(String bucketName, String fileType, Long CurrentPage, Long PageCount);

    List<File> listByName(String bucketName, String filename, Long currentPage, Long pageCount);

    Long sumBybuck(String username);

    void updatePath(String bucketName, String fileOldName, String fileNewName, String file_url);

    List<File> getfileRecycle(String bucketName, Long currentPage, Long pageCount);

    void RestoreTrash(String bucketName);

    List<File> selectFilePathTreeByBucket(String bucketName);

    void UpFilePath(String bucketName, String fileOldPath, String fileNewPath);

    void setDelTimeNull(int id);
}
