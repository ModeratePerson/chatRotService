/*
 Navicat Premium Data Transfer

 Source Server         : ServiceAdmin@localhost
 Source Server Type    : MySQL
 Source Server Version : 50740 (5.7.40)
 Source Host           : localhost:3306
 Source Schema         : chatbot

 Target Server Type    : MySQL
 Target Server Version : 50740 (5.7.40)
 File Encoding         : 65001

 Date: 23/05/2024 16:20:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `order_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `order_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `order_number` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `payment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `item_msg` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL,
  `creation_time` datetime NULL DEFAULT NULL,
  `pay_time` datetime NULL DEFAULT NULL,
  `status` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
