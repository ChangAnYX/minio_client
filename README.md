# minio_client
Springboot+VUE2的基于MinIO的网盘项目-后端
##### 前端VUE2项目地址
https://github.com/ChanAnYX/minio_web

当时基于老师的要求并未使用若依等类似后台管理框架

### 数据库设计
![图片](https://user-images.githubusercontent.com/126737340/224210656-eee8e638-160c-4a96-8c1a-187765cb8bd1.png)

### 项目架构
![图片](https://user-images.githubusercontent.com/126737340/224211031-0f7c6156-54ab-4153-ba2a-4d568cc8dbfe.png)

### 项目实施
用户登陆注册，存储文件信息交互大部分API操作都是普通的增删改查，可以通过配合Swagger自己读一下

Swagger：http://localhost:8081/doc.html

##### 值得一提的是
我分析了 MinIO SDK 的上传接口源码，其进行了分块，再上传分块到 Minio 服务器，最后再对文件块进行合并。
针对大文件的上传，如果采用其使用的上传到文件服务后端，再上传到 Minio 存储，其效率是非常低且不稳定的，其会首先上传到文件服务（会存放在 Tomcat 临时目录）就已经比较慢了。
所以进行了优化
![图片](https://user-images.githubusercontent.com/126737340/224213129-507b612e-eed4-418c-9b4c-cf16d0c0eef4.png)

首先前端调用文件分片上传地址 callback 接口，获取携带签名的分片文件上传地址。MinIO 外曝出来的 SDK 中未有此功能，所以注入自己写好的客户端。调用此接口则返回携带 uploadId 的直连 MinIO 上传链接，并将文件信息暂存Filechunk 表。
![图片](https://user-images.githubusercontent.com/126737340/224213478-b21a749a-61f2-4306-aadb-5d3b0efa297d.png)

前端分片上传成功后，再次调用分片合并接口，将分片文件在在存储中进行合并。转写入 File 表。

##### 文件展示，下载
首先利用 MInIO 外曝 SDK 获取 MinIO 中文件的元数据 InputStream，将其转换成浏览器可识别形态，Header 注意 Type 与 Disposition 的区别即可。具体不再赘述。
