/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80028
 Source Host           : localhost:3306
 Source Schema         : db_minioweb

 Target Server Type    : MySQL
 Target Server Version : 80028
 File Encoding         : 65001

 Date: 31/05/2022 14:06:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_admin
-- ----------------------------
DROP TABLE IF EXISTS `t_admin`;
CREATE TABLE `t_admin`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `enabled` tinyint(1) NULL DEFAULT 1 COMMENT '是否启用',
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '密码',
  `userFace` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `disksize` double NULL DEFAULT 100 COMMENT '存储大小',
  `email` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`, `username`) USING BTREE,
  INDEX `username`(`username`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 46 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_admin
-- ----------------------------
INSERT INTO `t_admin` VALUES (1, '管理', 1, 'admin', '$2a$10$ogvUqZZAxrBwrmVI/e7.SuFYyx8my8d.9zJ6bs9lPKWvbD9eefyCe', 'https://portrait.gitee.com/uploads/avatars/user/2617/7853024_mornd_1608529482.png!avatar200', 100, NULL);
INSERT INTO `t_admin` VALUES (42, '测试用户', 1, 'test', '$2a$10$pMuMiMoI5gI8AA1to5RByO2sWqMWbMeusZUERzt50pHdCyYZqvWv.', 'http://192.168.26.66:9000/userface/test/111.jpg', 100, '2776477318@qq.com');
INSERT INTO `t_admin` VALUES (45, 'MinIO用户', 1, 'qweqq', '$2a$10$/LtuahEhuZwUXctQ.DBk1uRc6pg50BqM2RFKpfkQHYr6aDp0hs4x.', NULL, 100, '2776477318@qq.com');

-- ----------------------------
-- Table structure for t_admin_role
-- ----------------------------
DROP TABLE IF EXISTS `t_admin_role`;
CREATE TABLE `t_admin_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `adminId` int NULL DEFAULT NULL COMMENT '用户id',
  `rid` int NULL DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `rid`(`rid`) USING BTREE,
  INDEX `adminId`(`adminId`) USING BTREE,
  CONSTRAINT `t_admin_role_ibfk_1` FOREIGN KEY (`adminId`) REFERENCES `t_admin` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `t_admin_role_ibfk_2` FOREIGN KEY (`rid`) REFERENCES `t_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 76 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_admin_role
-- ----------------------------
INSERT INTO `t_admin_role` VALUES (1, 1, 1);
INSERT INTO `t_admin_role` VALUES (73, 42, 2);
INSERT INTO `t_admin_role` VALUES (76, 45, 3);

-- ----------------------------
-- Table structure for t_file
-- ----------------------------
DROP TABLE IF EXISTS `t_file`;
CREATE TABLE `t_file`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `file_name` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件名称',
  `file_type` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件类型',
  `file_inbuck_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件存储IDwork名',
  `file_date` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上传日期',
  `file_del_date` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '删除日期',
  `file_size` bigint NULL DEFAULT NULL COMMENT '文件大小',
  `file_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件路径',
  `file_MD5` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'MD5唯一标识',
  `is_delete` int NOT NULL DEFAULT 0 COMMENT '状态 0未删除，1已删除',
  `is_directory` int NOT NULL COMMENT '状态 0不是文件夹，1是文件夹',
  `file_bucket_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '存储桶名',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `file_bucket_name`(`file_bucket_name`) USING BTREE,
  CONSTRAINT `user_file` FOREIGN KEY (`file_bucket_name`) REFERENCES `t_admin` (`username`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 84 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '文件信息表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_file
-- ----------------------------
INSERT INTO `t_file` VALUES (1, '789456', 'jpg', '111.jpg', '2022-04-28 20:00:51', NULL, 123000, '/1223123/', NULL, 0, 0, 'test');
INSERT INTO `t_file` VALUES (2, '789', 'zip', NULL, '2022-04-09 18:30:17', NULL, 1231230, '/', NULL, 0, 0, 'test');
INSERT INTO `t_file` VALUES (4, '789', 'mp4', '456789.mp4', '2022-04-28 20:52:17', NULL, 140000000, '/', NULL, 0, 0, 'test');
INSERT INTO `t_file` VALUES (6, '告五人 - 唯一', 'flac', '369.flac', '2022-04-29 13:09:41', NULL, 456789000, '/', NULL, 0, 0, 'test');
INSERT INTO `t_file` VALUES (7, '监听', 'txt', '监听linuxCPu.txt', '2022-05-03 13:41:01', NULL, 12345645, '/', NULL, 0, 0, 'test');
INSERT INTO `t_file` VALUES (69, 'Soler - 风的季节', 'flac', '1526850139390001154.flac', '2022-05-18 17:00:16', NULL, 27007759, '/', '11111', 0, 0, 'test');
INSERT INTO `t_file` VALUES (70, '草东没有派对 - 还愿', 'wav', '1526854126092730369.wav', '2022-05-18 17:16:08', NULL, 65201058, '/', '65201058--wav', 0, 0, 'test');
INSERT INTO `t_file` VALUES (73, 'hush! - 第三人称', 'flac', '1527207934235303938.flac', '2022-05-19 16:42:01', NULL, 26194505, '/', '26194505-hush-flac', 0, 0, 'test');
INSERT INTO `t_file` VALUES (75, 'canappeco - 日和', 'mp3', '1527236881702510593.mp3', '2022-05-19 18:37:02', NULL, 11847590, '/', '11847590-canappeco-mp3', 0, 0, 'qweqq');
INSERT INTO `t_file` VALUES (76, '岑宁儿、陈奕迅 - 一秒 with 陈奕迅', 'flac', '1527237169926692866.flac', '2022-05-19 18:38:11', NULL, 26689178, '/', '26689178--withflac', 0, 0, 'test');
INSERT INTO `t_file` VALUES (77, '1223123', NULL, NULL, '2022-05-19 18:38:58', NULL, NULL, '/', NULL, 0, 1, 'test');
INSERT INTO `t_file` VALUES (78, 'Vanilla Acoustic - 그런 사람 (那样的人)', 'flac', '1527240662808846340.flac', '2022-05-19 18:52:09', NULL, 20628032, '/', '20628032-VanillaAcoustic-flac', 0, 0, 'test');
INSERT INTO `t_file` VALUES (79, 'Soler - 风的季节', 'flac', '1527240662808846339.flac', '2022-05-19 18:52:09', NULL, 27007759, '/', '27007759-Soler-flac', 0, 0, 'test');
INSERT INTO `t_file` VALUES (80, 'Mr', 'flac', '1527240662779486210.flac', '2022-05-19 18:52:10', NULL, 37677875, '/', '37677875-Mr-flac', 0, 0, 'test');
INSERT INTO `t_file` VALUES (81, 'Twins - 下一站天后 (合唱版)_SQ', 'flac', '1527240662808846337.flac', '2022-05-19 18:52:10', NULL, 27101997, '/', '27101997-Twins-_SQflac', 0, 0, 'test');
INSERT INTO `t_file` VALUES (82, '草东没有派对 - 还愿', 'wav', '1527240662808846338.wav', '2022-05-19 18:52:12', NULL, 65201058, '/', '65201058--wav', 0, 0, 'test');
INSERT INTO `t_file` VALUES (83, '陈奕迅 - 今天只做一件事', 'flac', '1531517106766589954.flac', '2022-05-31 14:05:07', NULL, 23792307, '/', '23792307--flac', 0, 0, 'test');

-- ----------------------------
-- Table structure for t_filechunk
-- ----------------------------
DROP TABLE IF EXISTS `t_filechunk`;
CREATE TABLE `t_filechunk`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `file_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件名',
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件路径',
  `file_MD5` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'MD5',
  `file_size` bigint NULL DEFAULT NULL COMMENT '文件大小',
  `file_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '文件类型',
  `chunkNumber` int NULL DEFAULT NULL COMMENT '切片数量',
  `upload_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '上传时间',
  `upbucker_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '上传桶名',
  `Idworker_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'Idwork文件名',
  `uploadId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'uploadId',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 55 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_filechunk
-- ----------------------------
INSERT INTO `t_filechunk` VALUES (44, '2018101716 翟立彬 查重报告-- 基于Spring Boot VUE在线网盘系统', '/', '924549-2018101716--SpringBootVUEpdf', 924549, 'pdf', 1, '2022-05-19 16:41:37', 'test', '1527207839100100609.pdf', 'c006a3ef-ebb8-4228-84f1-9043627d44eb');

-- ----------------------------
-- Table structure for t_fileclass
-- ----------------------------
DROP TABLE IF EXISTS `t_fileclass`;
CREATE TABLE `t_fileclass`  (
  `Id` bigint NOT NULL AUTO_INCREMENT,
  `fileExtendName` varchar(25) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件扩展名',
  `fileTypeId` int NULL DEFAULT NULL COMMENT '文件类型id',
  PRIMARY KEY (`Id`) USING BTREE,
  INDEX `fileTypeId`(`fileTypeId`) USING BTREE,
  CONSTRAINT `t_fileclass_ibfk_1` FOREIGN KEY (`fileTypeId`) REFERENCES `t_filetype` (`Id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_fileclass
-- ----------------------------
INSERT INTO `t_fileclass` VALUES (1, 'bmp', 1);
INSERT INTO `t_fileclass` VALUES (2, 'jpg', 1);
INSERT INTO `t_fileclass` VALUES (3, 'png', 1);
INSERT INTO `t_fileclass` VALUES (4, 'tif', 1);
INSERT INTO `t_fileclass` VALUES (5, 'gif', 1);
INSERT INTO `t_fileclass` VALUES (6, 'jpeg', 1);
INSERT INTO `t_fileclass` VALUES (7, 'doc', 2);
INSERT INTO `t_fileclass` VALUES (8, 'docx', 2);
INSERT INTO `t_fileclass` VALUES (9, 'docm', 2);
INSERT INTO `t_fileclass` VALUES (10, 'dot', 2);
INSERT INTO `t_fileclass` VALUES (11, 'dotx', 2);
INSERT INTO `t_fileclass` VALUES (12, 'dotm', 2);
INSERT INTO `t_fileclass` VALUES (13, 'odt', 2);
INSERT INTO `t_fileclass` VALUES (14, 'fodt', 2);
INSERT INTO `t_fileclass` VALUES (15, 'ott', 2);
INSERT INTO `t_fileclass` VALUES (16, 'rtf', 2);
INSERT INTO `t_fileclass` VALUES (17, 'txt', 2);
INSERT INTO `t_fileclass` VALUES (18, 'html', 2);
INSERT INTO `t_fileclass` VALUES (19, 'htm', 2);
INSERT INTO `t_fileclass` VALUES (20, 'mht', 2);
INSERT INTO `t_fileclass` VALUES (21, 'xml', 2);
INSERT INTO `t_fileclass` VALUES (22, 'pdf', 2);
INSERT INTO `t_fileclass` VALUES (23, 'djvu', 2);
INSERT INTO `t_fileclass` VALUES (24, 'fb2', 2);
INSERT INTO `t_fileclass` VALUES (25, 'epub', 2);
INSERT INTO `t_fileclass` VALUES (26, 'xps', 2);
INSERT INTO `t_fileclass` VALUES (27, 'xls', 2);
INSERT INTO `t_fileclass` VALUES (28, 'xlsx', 2);
INSERT INTO `t_fileclass` VALUES (29, 'xlsm', 2);
INSERT INTO `t_fileclass` VALUES (30, 'xlt', 2);
INSERT INTO `t_fileclass` VALUES (31, 'xltx', 2);
INSERT INTO `t_fileclass` VALUES (32, 'xltm', 2);
INSERT INTO `t_fileclass` VALUES (33, 'ods', 2);
INSERT INTO `t_fileclass` VALUES (34, 'fods', 2);
INSERT INTO `t_fileclass` VALUES (35, 'ots', 2);
INSERT INTO `t_fileclass` VALUES (36, 'csv', 2);
INSERT INTO `t_fileclass` VALUES (37, 'pps', 2);
INSERT INTO `t_fileclass` VALUES (38, 'ppsx', 2);
INSERT INTO `t_fileclass` VALUES (39, 'ppsm', 2);
INSERT INTO `t_fileclass` VALUES (40, 'ppt', 2);
INSERT INTO `t_fileclass` VALUES (41, 'pptx', 2);
INSERT INTO `t_fileclass` VALUES (42, 'pptm', 2);
INSERT INTO `t_fileclass` VALUES (43, 'pot', 2);
INSERT INTO `t_fileclass` VALUES (44, 'potx', 2);
INSERT INTO `t_fileclass` VALUES (45, 'potm', 2);
INSERT INTO `t_fileclass` VALUES (46, 'odp', 2);
INSERT INTO `t_fileclass` VALUES (47, 'fodp', 2);
INSERT INTO `t_fileclass` VALUES (48, 'otp', 2);
INSERT INTO `t_fileclass` VALUES (49, 'hlp', 2);
INSERT INTO `t_fileclass` VALUES (50, 'wps', 2);
INSERT INTO `t_fileclass` VALUES (51, 'java', 2);
INSERT INTO `t_fileclass` VALUES (52, 'js', 2);
INSERT INTO `t_fileclass` VALUES (53, 'css', 2);
INSERT INTO `t_fileclass` VALUES (54, 'json', 2);
INSERT INTO `t_fileclass` VALUES (55, 'avi', 3);
INSERT INTO `t_fileclass` VALUES (56, 'mp4', 3);
INSERT INTO `t_fileclass` VALUES (57, 'mpg', 3);
INSERT INTO `t_fileclass` VALUES (58, 'mov', 3);
INSERT INTO `t_fileclass` VALUES (59, 'swf', 3);
INSERT INTO `t_fileclass` VALUES (60, 'wav', 4);
INSERT INTO `t_fileclass` VALUES (61, 'aif', 4);
INSERT INTO `t_fileclass` VALUES (62, 'au', 4);
INSERT INTO `t_fileclass` VALUES (63, 'mp3', 4);
INSERT INTO `t_fileclass` VALUES (64, 'ram', 4);
INSERT INTO `t_fileclass` VALUES (65, 'wma', 4);
INSERT INTO `t_fileclass` VALUES (66, 'mmf', 4);
INSERT INTO `t_fileclass` VALUES (67, 'amr', 4);
INSERT INTO `t_fileclass` VALUES (68, 'aac', 4);
INSERT INTO `t_fileclass` VALUES (69, 'flac', 4);
INSERT INTO `t_fileclass` VALUES (70, 'zip', 5);
INSERT INTO `t_fileclass` VALUES (71, 'rar', 5);
INSERT INTO `t_fileclass` VALUES (72, 'gz', 5);
INSERT INTO `t_fileclass` VALUES (73, '7z', 5);

-- ----------------------------
-- Table structure for t_fileshare
-- ----------------------------
DROP TABLE IF EXISTS `t_fileshare`;
CREATE TABLE `t_fileshare`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `fileId` int NULL DEFAULT NULL COMMENT '文件ID',
  `ExpirationTime` datetime NULL DEFAULT NULL COMMENT '过期时间',
  `ShareLinks` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '分享链接',
  `ShareTime` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分享时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fileid`(`fileId`) USING BTREE,
  CONSTRAINT `fileid` FOREIGN KEY (`fileId`) REFERENCES `t_file` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of t_fileshare
-- ----------------------------

-- ----------------------------
-- Table structure for t_filetype
-- ----------------------------
DROP TABLE IF EXISTS `t_filetype`;
CREATE TABLE `t_filetype`  (
  `Id` int NOT NULL,
  `fileTypeName` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '文件类型名',
  `orderNum` int NULL DEFAULT NULL COMMENT '次序',
  PRIMARY KEY (`Id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_filetype
-- ----------------------------
INSERT INTO `t_filetype` VALUES (0, '全部', NULL);
INSERT INTO `t_filetype` VALUES (1, '图片', 0);
INSERT INTO `t_filetype` VALUES (2, '文档', 0);
INSERT INTO `t_filetype` VALUES (3, '视频', 0);
INSERT INTO `t_filetype` VALUES (4, '音频', 0);
INSERT INTO `t_filetype` VALUES (5, '压缩', 1);
INSERT INTO `t_filetype` VALUES (6, '其他', 0);

-- ----------------------------
-- Table structure for t_menu
-- ----------------------------
DROP TABLE IF EXISTS `t_menu`;
CREATE TABLE `t_menu`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `url` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT 'url',
  `path` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '路径',
  `component` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '组件名称',
  `name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '菜单名称',
  `icon_cls` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '图标',
  `keep_alive` tinyint(1) NULL DEFAULT NULL COMMENT '种类权限',
  `require_auth` tinyint(1) NULL DEFAULT NULL COMMENT '是否要求权限',
  `parent_id` int NULL DEFAULT NULL COMMENT '父id',
  `enabled` tinyint(1) NULL DEFAULT NULL COMMENT '是否启用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_menu
-- ----------------------------
INSERT INTO `t_menu` VALUES (2, '/', '/main', 'Main', '首页', 'fa fa-address-card-o', NULL, 1, NULL, 1);
INSERT INTO `t_menu` VALUES (9, '/', '/index', 'FileWhole', '全部文件', NULL, NULL, 1, NULL, 1);
INSERT INTO `t_menu` VALUES (10, '/', '/index', 'FilePhoto', '图片', NULL, 1, 1, NULL, 1);
INSERT INTO `t_menu` VALUES (11, '/', '/index', 'FileDoc', '文档', NULL, 2, 1, NULL, 1);
INSERT INTO `t_menu` VALUES (13, '/', '/index', 'FileMusic', '音频', NULL, 4, 1, NULL, 1);
INSERT INTO `t_menu` VALUES (14, '/', '/index', 'FileVideo', '视频', NULL, 3, 1, NULL, 1);
INSERT INTO `t_menu` VALUES (15, '/', '/index', 'FileZip', '压缩包', NULL, 5, 1, NULL, 1);
INSERT INTO `t_menu` VALUES (16, '/', '/index', 'FileOther', '其他文件', NULL, 6, 1, NULL, 1);
INSERT INTO `t_menu` VALUES (17, '/', '/index', 'Recycle', '回收站', 'fa fa-gear', NULL, 1, NULL, 1);
INSERT INTO `t_menu` VALUES (19, '/', '/share', 'Share', '我的分享', 'fa fa-bar-chart-o', NULL, 1, NULL, 1);
INSERT INTO `t_menu` VALUES (20, '/', '/sys', 'Sys', '系统', 'fa fa-circle', NULL, 1, NULL, 1);

-- ----------------------------
-- Table structure for t_menu_role
-- ----------------------------
DROP TABLE IF EXISTS `t_menu_role`;
CREATE TABLE `t_menu_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `mid` int NULL DEFAULT NULL COMMENT '菜单id',
  `rid` int NULL DEFAULT NULL COMMENT '权限id',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `mid`(`mid`) USING BTREE,
  INDEX `rid`(`rid`) USING BTREE,
  CONSTRAINT `t_menu_role_ibfk_1` FOREIGN KEY (`mid`) REFERENCES `t_menu` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_menu_role_ibfk_2` FOREIGN KEY (`rid`) REFERENCES `t_role` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_menu_role
-- ----------------------------

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '名称',
  `nameZh` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_role
-- ----------------------------
INSERT INTO `t_role` VALUES (1, 'role_admin', '管理员');
INSERT INTO `t_role` VALUES (2, 'role_test', '测试用户');
INSERT INTO `t_role` VALUES (3, 'role_user', '用户');

SET FOREIGN_KEY_CHECKS = 1;
