/*
Navicat MySQL Data Transfer

Source Server         : cheng
Source Server Version : 50717
Source Host           : 127.0.0.1:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-11-02 20:47:37
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for test_user_permission
-- ----------------------------
DROP TABLE IF EXISTS `test_user_permission`;
CREATE TABLE `test_user_permission` (
  `role_name` varchar(255) DEFAULT NULL,
  `permission_name` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of test_user_permission
-- ----------------------------
INSERT INTO `test_user_permission` VALUES ('user', 'user:select');
