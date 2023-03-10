package com.minio.server.utils;

import com.google.common.collect.Multimap;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import io.minio.messages.Part;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;


@Component
@Slf4j
@Data
public class MinioUtil {

    @Autowired
    private MinioClient minioClient;
    @Autowired
    private PearlMinioClient pearlMinioClient;

    /**
     * 获取当前日期字符串格式
     * @return 2022/2/10
     */
    public String getDatePath() {
        LocalDateTime now = LocalDateTime.now();
        return String.format("%s-%s-%s", now.getYear(), now.getMonthValue(), now.getDayOfMonth());
    }


    /**
     * 判断桶是否存
     * @param bucketName 桶名称
     * @return
     */
    public boolean bucketExists(String bucketName) throws Exception {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }


    /**
     * 创建桶
     */
    public void createBucket(String bucketName) {
        try {
            //判断文件存储的桶对象是否存在
            boolean isExist = bucketExists(bucketName);
            if (isExist) {
                log.info("Bucket asiatrip already exists.");
            } else {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            log.error("errorMsg={}",e);
        }
    }


    /**
     * 列出桶里的所有对象
     * @param bucketName 桶名称
     */
    public Iterable<Result<Item>> listObjects(String bucketName) {
        return minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
    }


    /**
     * 删除桶
     * @param bucketName 桶名称
     * @return 是否删除成功
     */
    public boolean removeBucket(String bucketName) {
        try {
            boolean flag = bucketExists(bucketName);
            if (flag) {
                Iterable<Result<Item>> myObjects = listObjects(bucketName);
                for (Result<Item> result : myObjects) {
                    Item item = result.get();
                    // 有对象文件，则删除失败
                    if (item.size() > 0) {
                        return false;
                    }
                }
                // 删除存储桶，注意，只有存储桶为空时才能删除成功。
                minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
                flag = bucketExists(bucketName);
                if (!flag) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("errorMsg={}",e);
            return false;
        }
        return false;
    }


    /**
     * 获取所有桶信息
     */
    public List<Bucket> getAllBucket() {
        try {
            // 获取minio中所以的bucket
            List<Bucket> buckets = minioClient.listBuckets();
            for (Bucket bucket : buckets) {
                log.info("bucket 名称:  {}      bucket 创建时间: {}", bucket.name(), bucket.creationDate());
            }
            return buckets;
        } catch (Exception e) {
            log.error("errorMsg={}",e);
            return Collections.emptyList();
        }
    }


    /**
     * 上传本地文件到指定桶下
     * @param bucketName    桶名称
     * @param objectName    对象名称
     * @param localFileName 要上传的文件路径
     * @return
     */
    public boolean upload(String bucketName, String objectName, String localFileName) {
        try {
            File file = new File(localFileName);
            FileInputStream fileInputStream = new FileInputStream(file);
            minioClient.putObject(PutObjectArgs.builder()
                    .stream(fileInputStream, file.length(), PutObjectArgs.MIN_MULTIPART_SIZE)
                    .bucket(bucketName)
                    .object(objectName)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("errorMsg={}",e);
            return false;
        }
    }


    /**
     * 上传MultipartFile到指定桶下
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @param file       文件流
     */
    public boolean upload(String bucketName, String objectName, MultipartFile file) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .stream(file.getInputStream(), file.getSize(), PutObjectArgs.MIN_MULTIPART_SIZE)
                    .object(objectName)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("errorMsg={}",e);
            return false;
        }
    }


    /**
     * 下载文件到本地
     * @param bucketName    桶名称
     * @param objectName    对象名称
     * @param localFileName 本地文件存储路径
     */
    public boolean downLocal(String bucketName, String objectName, String localFileName) {
        try {
            minioClient.downloadObject(DownloadObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .filename(localFileName)
                    .build());
            return true;
        } catch (Exception e) {
            log.error("errorMsg={}",e);
            return false;
        }
    }

    /**
     * 下载文件写入到HttpServletResponse
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @param response   HttpServletResponse对象
     */
    @SneakyThrows
    public void downResponse(String bucketName, String objectName, HttpServletResponse response) {
        GetObjectResponse object = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
        response.setHeader("Content-Disposition", "attachment;filename=" + objectName.substring(objectName.lastIndexOf("/") + 1));
        response.setContentType("application/force-download");
        response.setCharacterEncoding("UTF-8");
        IOUtils.copy(object, response.getOutputStream());
    }

    /**
     * 删除指定桶的指定文件对象
     * @param bucketName 桶名称
     * @param objectName 对象名称
     */
    public boolean delete(String bucketName, String objectName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build());
            return true;
        }catch (Exception e){
            log.error("errorMsg={}",e);
            return false;
        }
    }

    /**
     * 获取文件带时效的访问链接   失效时间（以秒为单位），默认是7天不得大于七天
     * @param bucketName     桶名称
     * @param remoteFileName 对象名称
     * @param timeout        时间数
     * @param unit           时间单位
     * @return 文件访问链接
     */
    public String getPresignedObjectUrl(String bucketName, String remoteFileName, long timeout, TimeUnit unit) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(bucketName)
                            .object(remoteFileName)
                            .expiry((int) unit.toSeconds(timeout))
                            .build());
        } catch (Exception e) {
            log.error("errorMsg={}",e);
            return null;
        }
    }

    /**
     * 根据登录用户返回桶名（用户名）
     * @return
     */
    public String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }


    /**
     * 新建路径
     * @param bucketName
     * @param url
     */
    public void Newpath(String bucketName, String url) {
        try {
            createBucket(bucketName);
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(url+"/")
                    .stream(new ByteArrayInputStream(new byte[] {}), 0, -1)
                    .build());
        } catch (Exception ignored) {
        }
    }


    /**
     * 批量删除
     * @param bucketName
     * @param objectNames
     */
    public boolean delFiles(String bucketName, @NotNull String[] objectNames){
        for(int i=0;i<=objectNames.length - 1;){
            String objectName = objectNames[i];
            delete(bucketName, objectName);
            i = i+1;
        }
        return true;
    }

//    /**
//     * 列出一个桶指定路径中的所有文件和目录
//     * @param bucketname
//     * @return
//     */
////    public List<Fileinfo> listFiles(String bucketname,String url) {
////        Iterable<Result<Item>> results = minioClient.listObjects(
////                ListObjectsArgs.builder()
////                        .bucket(bucketname)
////                        .prefix(url+"/")
//////                        .prefix(url)
//////                        .recursive(false)
////                        .build());
////        List<Fileinfo> infos = new ArrayList<>();
////        results.forEach(r->{
////            Fileinfo info = new Fileinfo();
////            String leftStr = url+"/";
//////            String leftStr = url;
////            try {
////                Item item = r.get();
////                String name = item.objectName();
////                if(Objects.equals(leftStr, "/")){
////                    if(item.isDir()){
////                        info.setFilename(name.substring(0,name.length()-1));
////                    }
////                    else {info.setFilename(item.objectName());}
////                }
////                else {
////                    int index = name.indexOf(leftStr);
////                    String right = name.substring(index+leftStr.length());
////                    if(item.isDir()){
////                        info.setFilename(right.substring(0,right.length()-1));
////                    }
////                    else {info.setFilename(right);}
////                }
////                info.setDirectory(item.isDir());
////                info.setSize(formatFileSize(item.size()));
////                if (!item.isDir()){
////                    info.setLastModified(item.lastModified());
////                }
////                infos.add(info);
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        });
////        return infos;
////    }


    /**
     *  上传分片上传请求，返回uploadId
     */
    public CreateMultipartUploadResponse uploadId(String bucketName, String region, String objectName,
                                                  Multimap<String, String> headers, Multimap<String, String> extraQueryParams)
            throws NoSuchAlgorithmException, InsufficientDataException, IOException, InvalidKeyException, ServerException, XmlParserException,
            ErrorResponseException, InternalException, InvalidResponseException {
        return pearlMinioClient.createMultipartUpload(bucketName, region, objectName, headers, extraQueryParams);
    }

    /**
     * 查询分片数据
     *
     * @param bucketName       存储桶
     * @param region           区域
     * @param objectName       对象名
     * @param uploadId         上传ID
     * @param extraHeaders     额外消息头
     * @param extraQueryParams 额外查询参数
     */
    public ListPartsResponse listMultipart(String bucketName, String region, String objectName, Integer maxParts, Integer partNumberMarker, String uploadId, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws NoSuchAlgorithmException, InsufficientDataException, IOException, InvalidKeyException, ServerException, XmlParserException, ErrorResponseException, InternalException, InvalidResponseException {
        return pearlMinioClient.listMultipart(bucketName, region, objectName, maxParts, partNumberMarker, uploadId, extraHeaders, extraQueryParams);
    }

    /**
     * 完成分片上传，执行合并文件
     *
     * @param bucketName       存储桶
     * @param region           区域
     * @param objectName       对象名
     * @param uploadId         上传ID
     * @param parts            分片
     * @param extraHeaders     额外消息头
     * @param extraQueryParams 额外查询参数
     */
    public ObjectWriteResponse completeMultipartUpload(String bucketName, String region, String objectName, String uploadId, Part[] parts, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws NoSuchAlgorithmException, InsufficientDataException, IOException, InvalidKeyException, ServerException, XmlParserException, ErrorResponseException, InternalException, InvalidResponseException {
        return pearlMinioClient.completeMultipartUpload(bucketName, region, objectName, uploadId, parts, extraHeaders, extraQueryParams);
    }


    /**
     * 返回临时带签名、过期时间一天、Get请求方式的访问URL
     *
     * @param bucketName  桶名
     * @param ossFilePath Oss文件路径
     * @return
     */
    @SneakyThrows
    public String getPresignedObjectUrl(String bucketName, String ossFilePath,Map<String, String> queryParams) {
        return pearlMinioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucketName)
                        .object(ossFilePath)
                        .expiry(60 * 60 * 24)
                        .extraQueryParams(queryParams)
                        .build());
    }

    /**
     * 删除路径
     * @param bucketname
     * @param path
     * @return
     */
    public boolean delpath(String bucketname,String path){
        Iterable<Result<Item>> names = minioClient.listObjects(
                ListObjectsArgs.builder()
                        .bucket(bucketname)
                        .prefix(path+"/")
                        .recursive(true)
                        .build());
        names.forEach(r->{
            try {
                Item item = r.get();
                delete(bucketname,item.objectName());
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
        return true;
    }

    /**
     * 获取文件流
     * @param objectName 文件名
     * @param bucketName 桶名（文件夹）
     * @return
     */
    public InputStream getFileInputStream(String objectName,String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)

                        .build());
    }


    public StatObjectResponse getObjectStat(String objectName,String bucketName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return  minioClient.statObject(
                        StatObjectArgs.builder()
                                .bucket(bucketName)
                                .object(objectName)
                                .build());
    }
}
