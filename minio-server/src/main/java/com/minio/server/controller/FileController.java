package com.minio.server.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.minio.server.pojo.DTO.*;
import com.minio.server.utils.MinioUtil;
import com.minio.server.pojo.File;
import com.minio.server.pojo.RespBean;
import com.minio.server.pojo.VO.TreeNodeVO;
import com.minio.server.service.IFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 文件信息表 前端控制器
 * </p>
 *
 * @author bin
 * @since 2022-03-26
 */
@RestController
@Api(value = "FileController",tags = {"文件列表操作"})
@RequestMapping("/api/file")
public class FileController {

    @Resource
    private IFileService fileService;
    @Resource
    private MinioUtil minioUtil;

    @ApiOperation(value = "遍历路径")
    @GetMapping("/list")
    public RespBean getFilesBy(FileListParam fileListParam){
        String bucketName = minioUtil.getUsername();
        List<File> fileList = fileService.getFilesByPath(bucketName, fileListParam.getFilePath(),fileListParam.getCurrentPage(),fileListParam.getPageCount());
        LambdaQueryWrapper<File> FileLambdaQueryWrapper = new LambdaQueryWrapper<>();
        FileLambdaQueryWrapper.eq(File::getFile_bucket_name, bucketName)
                .eq(File::getFile_url, fileListParam.getFilePath()).isNull(File::getFile_del_date);
        int total = fileService.count(FileLambdaQueryWrapper);
        System.out.print(total);
        Map<String, Object> map = new HashMap<>();
        map.put("total", total);
        map.put("list", fileList);
        return RespBean.success("",map);
    }

    @ApiOperation(value = "新建文件夹")
    @PostMapping(value = "/mkDir")
    @ResponseBody
    public RespBean createFile(@RequestBody CreateFileParam createFileParam) {
        String bucketName = minioUtil.getUsername();
        LambdaQueryWrapper<File> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(File::getFile_bucket_name, bucketName)
                .eq(File::getFile_url, createFileParam.getFilePath())
                .eq(File::getFile_name, createFileParam.getFileName())
                .isNull(File::getFile_size);
        int total = fileService.count(lambdaQueryWrapper);
        if (total>0) {
            return RespBean.error("同名路径已存在");
        }

        File userFile = new File();
        userFile.setFile_bucket_name(bucketName);
        userFile.setFile_name(createFileParam.getFileName());
        userFile.setFile_url(createFileParam.getFilePath());
        userFile.set_directory(true);

        fileService.save(userFile);
        return RespBean.success("新建路径成功");
    }

    @ApiOperation(value = "按名模糊查询")
    @GetMapping("/list/search")
    public RespBean fileListByName(SelectFileParam selectFileParam){
        String bucketName =  minioUtil.getUsername();
        String filename = '%'+ selectFileParam.getFileName() +'%';
        List<File> fileListByName = fileService.listByName(bucketName,filename,selectFileParam.getCurrentPage(),selectFileParam.getPageCount());
        return RespBean.success("",fileListByName);
    }

    @ApiOperation(value = "按类搜索文件")
    @GetMapping("/list/type")
    public RespBean listType(FileListByTypeParam fileListByTypeParam){
        String bucketName =  minioUtil.getUsername();
        if(Integer.parseInt(fileListByTypeParam.getFileType())<7){
            List<File> fileTypeList = fileService.getFilesBytype(bucketName,fileListByTypeParam.getFileType(),fileListByTypeParam.getCurrentPage(),fileListByTypeParam.getPageCount());
            int total = 0;
            System.out.print(total);
            Map<String, Object> map = new HashMap<>();
            map.put("total", total);
            map.put("list", fileTypeList);
            return RespBean.success("",map);}
        if(Integer.parseInt(fileListByTypeParam.getFileType()) == 8){
            List<File> fileListRecycle = fileService.getfileRecycle(bucketName,fileListByTypeParam.getCurrentPage(),fileListByTypeParam.getPageCount());
            LambdaQueryWrapper<File> FileRecycle = new LambdaQueryWrapper<>();
            FileRecycle.eq(File::getFile_bucket_name, bucketName)
                    .isNotNull(File::getFile_del_date);
            int total = fileService.count(FileRecycle);
            System.out.print(total);
            Map<String, Object> map = new HashMap<>();
            map.put("total", total);
            map.put("list", fileListRecycle);
            return RespBean.success("",map);
        }
        if(Integer.parseInt(fileListByTypeParam.getFileType()) == 9){
//            List<map> sharefils = fileService.getsharefiles(bucketName,fileListByTypeParam.getCurrentPage(),fileListByTypeParam.getPageCount());
            return null;
        }
        else {return null;}
    }

    @ApiOperation(value = "假删除文件")
    @PostMapping("/delete")
    public RespBean delFiles(FileUpdateParam fileUpdateParam){
      try{ String ID = String.valueOf(fileUpdateParam.getId());
           File fileDel = fileService.getById(ID);
           fileDel.set_delete(true);
           fileDel.setFile_del_date(minioUtil.getDatePath());
           fileService.updateById(fileDel);
           return RespBean.success("删除文件成功");
          }
      catch (Exception e) {
            return RespBean.error("删除文件失败",e);
        }
    }

    @ApiOperation(value = "删除多个文件")
    @PostMapping("/delete/Batch")
    public RespBean delFiles(@RequestBody BatchFilesParam batchFilesParam){
        List<File> userFiles = JSON.parseArray(batchFilesParam.getFiles(), File.class);
        for(File file:userFiles){
            file.set_delete(true);
            file.setFile_del_date(minioUtil.getDatePath());
            fileService.updateById(file);
        }
        return RespBean.success("删除成功");
    }


    @ApiOperation(value = "文件重命名")
    @PostMapping("/rename")
    public RespBean FileRename(FileUpdateParam fileUpdateParam){
        String bucketName =  minioUtil.getUsername();
        LambdaQueryWrapper<File> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(File::getFile_bucket_name, bucketName)
                .eq(File::getFile_url, fileUpdateParam.getFile_url()).eq(File::getFile_name, fileUpdateParam.getFile_name());
        int total = fileService.count(lambdaQueryWrapper);
        if (total>0) {
            return RespBean.error("同名文件已存在");
        };
        File file = fileService.getById(fileUpdateParam.getId());
        String fileOldName = file.getFile_name();
        if(file.is_directory()){
            fileService.updatePath(bucketName,fileOldName,fileUpdateParam.getFile_name(),fileUpdateParam.getFile_url());
        };
        File fileRename = fileService.getById(fileUpdateParam.getId());
        fileRename.setFile_name(fileUpdateParam.getFile_name());
        fileService.updateById(fileRename);
        return RespBean.success("重命名成功");
    }

    /**
     * 彻底删除回收站里的某个对象
     * @param fileUpdateParam 对象
     * @return
     */
    @PostMapping(value = "/delFile")
    @ApiOperation(value = "彻底删除文件")
    public RespBean delFile(FileUpdateParam fileUpdateParam) {
        String bucketName =  minioUtil.getUsername();
        File delFile = fileService.getById(fileUpdateParam.getId());
        fileService.removeById(fileUpdateParam.getId());
        if(minioUtil.delete(bucketName, delFile.getFile_inbuck_name()))
        {return RespBean.success("删除成功");}
        else return RespBean.error("删除失败");
    }

    /**
     * 清空回收站
     * @return
     */
    @GetMapping(value = "/EmptyTrash")
    @ApiOperation(value = "清空回收站")
    public RespBean delFiles() {
        String bucketName =  minioUtil.getUsername();
        LambdaQueryWrapper<File> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(File::getFile_bucket_name, bucketName).isNotNull(File::getFile_del_date);
        fileService.remove(lambdaQueryWrapper);
        return RespBean.success("清空回收站成功");
    }

    /**
     * 还原回收站
     * @return
     */
    @GetMapping(value = "/RestoreTrash")
    @ApiOperation(value = "还原回收站")
    public RespBean RestoreTrash(){
        String bucketName =  minioUtil.getUsername();
        fileService.RestoreTrash(bucketName);
        return RespBean.success("还原回收站成功");
    }

    /**
     * 还原回收站多个文件
     * @param batchFilesParam
     * @return
     */
    @ApiOperation(value = "还原多个文件")
    @PostMapping("/Restore/Batch")
    public RespBean reFiles(@RequestBody BatchFilesParam batchFilesParam){
        List<File> userFiles = JSON.parseArray(batchFilesParam.getFiles(), File.class);
        for(File file:userFiles){
            file.set_delete(false);
            fileService.setDelTimeNull(file.getId());
            file.setFile_del_date(null);
//            file.setFile_del_date("");
            fileService.updateById(file);
        }
        return RespBean.success("还原文件成功");
    }

    /**
     * 彻底删除回收站多个文件
     * @param batchFilesParam
     * @return
     */
    @ApiOperation(value = "彻底删除多个文件")
    @PostMapping("/Empty/Batch")
    public RespBean dellsFiles(@RequestBody BatchFilesParam batchFilesParam){
        String bucketName =  minioUtil.getUsername();
        List<File> userFiles = JSON.parseArray(batchFilesParam.getFiles(), File.class);
        for(File file:userFiles){
            if(!file.is_directory()){

                minioUtil.delete(bucketName, file.getFile_inbuck_name());
            }
            fileService.removeById(file);
        }
        return RespBean.success("彻底删除文件成功");
    }


    @ApiOperation(value = "获取文件树")
    @GetMapping("/getTree")
    @ResponseBody
    public RespBean getFileTree(){
        String bucketName =  minioUtil.getUsername();
        List<File> filePathList = fileService.selectFilePathTreeByBucket(bucketName);
        TreeNodeVO resultTreeNode = new TreeNodeVO();
        resultTreeNode.setLabel("/");

        for (int i = 0; i < filePathList.size(); i++){
            String filePath = filePathList.get(i).getFile_url() + filePathList.get(i).getFile_name() + "/";

            Queue<String> queue = new LinkedList<>();

            String[] strArr = filePath.split("/");
            for (int j = 0; j < strArr.length; j++){
                if (!"".equals(strArr[j]) && strArr[j] != null){
                    queue.add(strArr[j]);
                }

            }
            if (queue.size() == 0){
                continue;
            }
            resultTreeNode = insertTreeNode(resultTreeNode,"/", queue);


        }
//        result.setSuccess(true);
//        result.setData(resultTreeNode);
        return RespBean.success("",resultTreeNode);

    }

    public TreeNodeVO insertTreeNode(TreeNodeVO treeNode, String filePath, Queue<String> nodeNameQueue){

        List<TreeNodeVO> childrenTreeNodes = treeNode.getChildren();
        String currentNodeName = nodeNameQueue.peek();
        if (currentNodeName == null){
            return treeNode;
        }

        Map<String, String> map = new HashMap<>();
        filePath = filePath + currentNodeName + "/";
        map.put("filePath", filePath);

        if (!isExistPath(childrenTreeNodes, currentNodeName)){  //1、判断有没有该子节点，如果没有则插入
            //插入
            TreeNodeVO resultTreeNode = new TreeNodeVO();


            resultTreeNode.setAttributes(map);
            resultTreeNode.setLabel(nodeNameQueue.poll());
            // resultTreeNode.setId(treeid++);

            childrenTreeNodes.add(resultTreeNode);

        }else{  //2、如果有，则跳过
            nodeNameQueue.poll();
        }

        if (nodeNameQueue.size() != 0) {
            for (int i = 0; i < childrenTreeNodes.size(); i++) {

                TreeNodeVO childrenTreeNode = childrenTreeNodes.get(i);
                if (currentNodeName.equals(childrenTreeNode.getLabel())){
                    childrenTreeNode = insertTreeNode(childrenTreeNode, filePath, nodeNameQueue);
                    childrenTreeNodes.remove(i);
                    childrenTreeNodes.add(childrenTreeNode);
                    treeNode.setChildren(childrenTreeNodes);
                }
            }
        }else{
            treeNode.setChildren(childrenTreeNodes);
        }

        return treeNode;

    }

    public boolean isExistPath(List<TreeNodeVO> childrenTreeNodes, String path){
        boolean isExistPath = false;

        try {
            for (int i = 0; i < childrenTreeNodes.size(); i++){
                if (path.equals(childrenTreeNodes.get(i).getLabel())){
                    isExistPath = true;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return isExistPath;
    }

    @ApiOperation(value = "文件移动")
    @PostMapping("/move")
    public RespBean moveFiles(FileUpdateParam fileUpdateParam){
        String bucketName =  minioUtil.getUsername();
        File fileUpUrl = fileService.getById(fileUpdateParam.getId());
        String fileURL = fileUpUrl.getFile_url();
        fileUpUrl.setFile_url(fileUpdateParam.getFile_url());
        if(fileUpUrl.is_directory()){
            String fileOldPath = fileURL+fileUpUrl.getFile_name()+"/";
            String fileNewPath = fileUpdateParam.getFile_url()+fileUpUrl.getFile_name()+"/";
            fileService.UpFilePath(bucketName,fileOldPath,fileNewPath);

        }
        fileService.updateById(fileUpUrl);
        return RespBean.success("文件移动成功");
    }

    @ApiOperation(value = "批量文件移动")
    @PostMapping("/move/Batch")
    public RespBean BatchMoveFiles(@RequestBody BatchFilesParam batchFilesParam){
        String bucketName = minioUtil.getUsername();
        List<File> userFiles = JSON.parseArray(batchFilesParam.getFiles(), File.class);
        for(File file:userFiles){
            if(file.is_directory()){
                String fileOldPath = file.getFile_url()+file.getFile_name()+"/";
                String fileNewPath = batchFilesParam.getFilePath()+file.getFile_name()+"/";
                fileService.UpFilePath(bucketName,fileOldPath,fileNewPath);
            }
            file.setFile_url(batchFilesParam.getFilePath());
            fileService.updateById(file);
        }
        return RespBean.success("文件移动成功");
    }



}
