# minio_client
Springboot+VUE2的基于MinIO的网盘项目-后端

当时基于老师的要求并未使用若依等类似后台管理框架

### 数据库设计
![图片](https://user-images.githubusercontent.com/126737340/224210656-eee8e638-160c-4a96-8c1a-187765cb8bd1.png)

### 项目架构
![图片](https://user-images.githubusercontent.com/126737340/224211031-0f7c6156-54ab-4153-ba2a-4d568cc8dbfe.png)

### 项目实施
大部分API操作都是普通的增删改查，可以通过配合Swagger自己读一下
Swagger：http://localhost:8081/doc.html
值得一提的是
  我分析了 MinIO SDK 的上传接口源码，其进行了分块，再上传分块到 Minio 服务器，最后再对文件块进行合并。
  针对大文件的上传，如果采用其使用的上传到文件服务后端，再上传到 Minio 存储，其效率是非常低且不稳定的，其会首先上传到文件服务（会存放在 Tomcat 临时目录）就已经比较慢了。
![图片](https://user-images.githubusercontent.com/126737340/224213129-507b612e-eed4-418c-9b4c-cf16d0c0eef4.png)
