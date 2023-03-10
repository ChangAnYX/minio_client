package com.minio.server.controller;


import com.minio.server.pojo.File;
import com.minio.server.service.IFileService;
import com.minio.server.utils.MinioUtil;
import io.minio.StatObjectResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;

/**
 * @program: minio_sever
 * @description：文件前端展示接口
 * @author: bin
 * @create: 2022-04-28 19:52
 **/
@RestController
@Api(value = "ShowController",tags = {"文件获取/展示操作"})
@RequestMapping("/api/object")
public class ShowController {

    @Autowired
    private IFileService fileService;
    @Autowired
    private MinioUtil minioUtil;


    @ApiOperation(value = "流文件展示/Type")
    @GetMapping("/show/{fileId}")
    public void thumbnail(@PathVariable  String fileId,HttpServletResponse  response){

        File FileShow = fileService.getById(fileId);
        //获取MinIO分享链接
        String objectName = FileShow.getFile_inbuck_name();
        String bucketName = FileShow.getFile_bucket_name();

        try {
            // 获取object的输入流。
            InputStream stream = minioUtil.getFileInputStream(objectName,bucketName);
            //流转换
            IOUtils.copy(stream,response.getOutputStream());
            //设置返回类型
            response.addHeader("Content-Type", "audio/mpeg;charset=utf-8");
//            // 关闭流
            stream.close();
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
        }
//
//        return "success";
    }


    @ApiOperation(value = "文件下载/Disposition")
    @GetMapping("/send/{fileId}")
    public void ObjectSend(@PathVariable  String fileId,HttpServletResponse  response){
        File FileShow = fileService.getById(fileId);
        //获取MinIO分享链接
        String objectName = FileShow.getFile_inbuck_name();
        String bucketName = FileShow.getFile_bucket_name();
        String fileName = FileShow.getFile_name()+"."+FileShow.getFile_type();

        try {
            // 获取object的输入流。
            InputStream stream = minioUtil.getFileInputStream(objectName,bucketName);

            //流转换
//            IOUtils.copy(stream,response.getOutputStream());

            final StatObjectResponse stat = minioUtil.getObjectStat(objectName,bucketName);
            response.setContentType(stat.contentType());
            response.setCharacterEncoding("UTF-8");
            //设置返回类型
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
            //这里注释掉  要不然会报错
//            response.flushBuffer();
            //流转换
            org.apache.tomcat.util.http.fileupload.IOUtils.copy(stream, response.getOutputStream());
//            // 关闭流
            stream.close();
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
        }
    }

//
//    @ApiOperation(value = "文本文件展示/Type")
//    @GetMapping( "/preview/{fileId}")
//    public void textShow(@PathVariable String fileId,HttpServletResponse  response){
//        File FileShow = fileService.getById(fileId);
//        //获取MinIO分享链接
//        String objectName = FileShow.getFile_inbuck_name();
//        String bucketName = FileShow.getFile_bucket_name();
//        String fileName = FileShow.getFile_name()+"."+FileShow.getFile_type();
//
//        try {
//            // 获取object的输入流。
//            InputStream stream = minioUtil.getFileInputStream(objectName,bucketName);
//
//            //流转换
////            IOUtils.copy(stream,response.getOutputStream());
//
//            final StatObjectResponse stat = minioUtil.getObjectStat(objectName,bucketName);
//            response.setContentType(stat.contentType());
//            response.setCharacterEncoding("UTF-8");
//            //设置返回类型
//            response.setHeader("Content-Type", "text/html; charset=utf-8");
//            //这里注释掉  要不然会报错
////            response.flushBuffer();
//            //流转换
//            org.apache.tomcat.util.http.fileupload.IOUtils.copy(stream, response.getOutputStream());
////            // 关闭流
//            stream.close();
//        } catch (Exception e) {
//            System.out.println("Error occurred: " + e);
//        }
//    }



    @ApiOperation(value = "文本文件展示/Type")
    @GetMapping( "/preview/txt")
    public void textShsowsss(String userFileId,HttpServletResponse  response){
        File FileShow = fileService.getById(userFileId);
        //获取MinIO分享链接
        String objectName = FileShow.getFile_inbuck_name();
        String bucketName = FileShow.getFile_bucket_name();
        String fileName = FileShow.getFile_name()+"."+FileShow.getFile_type();

        try {
            // 获取object的输入流。
            InputStream stream = minioUtil.getFileInputStream(objectName,bucketName);

            //流转换
//            IOUtils.copy(stream,response.getOutputStream());

            final StatObjectResponse stat = minioUtil.getObjectStat(objectName,bucketName);
            response.setContentType(stat.contentType());
            response.setCharacterEncoding("UTF-8");
            //设置返回类型
            response.setHeader("Content-Type", "text/html; charset=utf-8");
            //这里注释掉  要不然会报错
//            response.flushBuffer();
            //流转换
            org.apache.tomcat.util.http.fileupload.IOUtils.copy(stream, response.getOutputStream());
//            // 关闭流
            stream.close();
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
        }
    }



}
