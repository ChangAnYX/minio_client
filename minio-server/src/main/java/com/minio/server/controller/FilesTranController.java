package com.minio.server.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.google.common.collect.HashMultimap;
import com.minio.server.pojo.File;
import com.minio.server.pojo.DTO.UpFileParam;
import com.minio.server.service.IFileService;
import com.minio.server.service.IFilechunkService;
import com.minio.server.utils.MinioUtil;
import com.minio.server.pojo.Filechunk;
import io.minio.CreateMultipartUploadResponse;
import io.minio.ListPartsResponse;
import io.minio.messages.Part;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author bin
 * @CreateTime 2022/2/10
 * @Description 测试文件上传下载预览和删除
 **/
@RestController
@Api(value = "FilesTranController",tags = {"文件上传接口"})
@RequestMapping("/api/FileTran")
public class FilesTranController {

    @Autowired
    private MinioUtil minioUtil;
    @Autowired
    private IFileService fileService;
    @Autowired
    private IFilechunkService filechunkService;



//    /**
//     * 上传文件
//     * @param file
//     * @param bucketName 桶名称
//     * @return 返回对象名称和外链地址
//     */
//    @PostMapping(value = "/upFile")
//    @ApiOperation(value = "上传流文件")
//    public RespBean uploadFile(MultipartFile file, @RequestParam(required = false) String bucketName, String url) {
//        bucketName = StringUtils.hasLength(bucketName) ? bucketName : minioUtil.getUsername();
//        String objectName = url + file.getOriginalFilename();
//        boolean bol = minioUtil.upload(bucketName, objectName, file);
//        String viewPath = minioUtil.getPresignedObjectUrl(bucketName, objectName, 60, TimeUnit.SECONDS);
//        HashMap<String, String> objectInfo = new HashMap<>();
//        objectInfo.put("objectName", file.getOriginalFilename());
//        //只能预览图片、txt等部分文件
//        objectInfo.put("FilePath", viewPath);
//        if(bol){return RespBean.success("上传文件成功",objectInfo);}
//        else return RespBean.error("上传文件失败");
//    }


    /**
     * 返回分片上传需要的签名数据URL及 uploadId
     * @param upFileParam
     * @return
     */
    @GetMapping("/createMultipartUpload")
    @SneakyThrows
    @ResponseBody
    @ApiOperation(value = "申请上传需要的签名数据URL")
    public Map<String, Object> createMultipartUpload(UpFileParam upFileParam) {
        String bucketName =  minioUtil.getUsername();
        int chunkSize = upFileParam.getChunkNumber();
        Filechunk upFile = new Filechunk();
//        upFile.setFile_name(upFileParam.getFilename());
        upFile.setFile_name(upFileParam.getFilename().substring(0, upFileParam.getFilename().lastIndexOf(".")));
        upFile.setFile_url(upFileParam.getFilePath());
        upFile.setUpbucker_name(bucketName);
        upFile.setFile_size(upFileParam.getFileSize());
        upFile.setFile_MD5(upFileParam.getIdentifier());

        String Name = upFileParam.getFilename();

        String TypeName = Name.substring(Name.lastIndexOf("."));
        upFile.setFile_type(TypeName.substring(1));
        upFile.setChunkNumber(chunkSize);

        //IdWorker 重命名
        String fileName = IdWorker.getId() +TypeName;
        upFile.setIdworker_name(fileName);

        // 根据文件名创建签名
        Map<String, Object> result = new HashMap<>();
        // 获取uploadId
        String contentType = "application/octet-stream";
        HashMultimap<String, String> headers = HashMultimap.create();
        headers.put("Content-Type", contentType);
        CreateMultipartUploadResponse response = minioUtil.uploadId(bucketName, null, fileName, headers, null);
        String uploadId = response.result().uploadId();
        result.put("uploadId", uploadId);

        upFile.setUploadId(uploadId);
        filechunkService.save(upFile);

        // 请求Minio 服务，获取每个分块带签名的上传URL
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("uploadId", uploadId);
        // 循环分块数 从1开始,MinIO 存储服务定义分片索引却是从1开始的
        for (int i = 1; i <= chunkSize; i++) {
            reqParams.put("partNumber", String.valueOf(i));
            String uploadUrl = minioUtil.getPresignedObjectUrl(bucketName, fileName, reqParams);// 获取URL,主要这里前端上传的时候，要传递二进制流，而不是file
            result.put("chunk_" + (i - 1), uploadUrl); // 添加到集合
        }
        return result;
    }


    /**
     * 分片上传完后合并
     * @param FileName 文件名
     * @param uploadId 返回的uploadId
     * @return /
     */
    @GetMapping("/completeMultipartUpload")
    @ApiOperation(value = "上传分片后合并")
    @SneakyThrows
    @ResponseBody
    public boolean completeMultipartUpload(String FileName, String uploadId) {
        String bucketName = minioUtil.getUsername();
        LambdaQueryWrapper<Filechunk> Wrapper = new LambdaQueryWrapper<>();
        Wrapper.eq(Filechunk::getUpbucker_name, bucketName).eq(Filechunk::getUploadId, uploadId);
        Filechunk upFile = filechunkService.getOne(Wrapper);
        String objectName = upFile.getIdworker_name();
        try {
            Part[] parts = new Part[10000];
            // 1. 查询分片
            ListPartsResponse partResult = minioUtil.listMultipart(bucketName, null, objectName, 10000, 0, uploadId, null, null);
            int partNumber = 1; // 分片序列从1开始
//            System.err.println(partResult.result().partList().size() + "========================");
            // 2. 循环获取到的分片信息
            List<Part> partList = partResult.result().partList();
            for (int i = 0; i < partList.size(); i++) {
                // 3. 将分片标记传递给Part对象
                parts[partNumber - 1] = new Part(partNumber, partList.get(i).etag());
                partNumber++;
            }
            minioUtil.completeMultipartUpload(bucketName, null, objectName, uploadId, parts, null, null);
        } catch (Exception e) {
            return false;
        }
        File FileObj = new File();
        FileObj.setFile_bucket_name(bucketName);
        FileObj.setFile_inbuck_name(objectName);
        FileObj.setFile_type(upFile.getFile_type());
        FileObj.set_directory(false);
        FileObj.setFile_url(upFile.getFile_url());
        FileObj.setFile_size(upFile.getFile_size());
        FileObj.setFile_name(upFile.getFile_name());
        FileObj.setFile_MD5(upFile.getFile_MD5());
        fileService.save(FileObj);
        filechunkService.remove(Wrapper);
        return true;

            }



    /**
     * 下载文件
     * @param bucketName 桶名称
     * @param objectName 对象名称
     * @param response  相应结果
     */
    @GetMapping("downFile")
    @ApiOperation(value = "下载文件")
    public void downLoad(@RequestParam(required = false) String bucketName, String objectName,HttpServletResponse response) {
        bucketName = StringUtils.hasLength(bucketName) ? bucketName : minioUtil.getUsername();
        // 获取文件
        minioUtil.downResponse(bucketName,objectName,response);
    }




}
