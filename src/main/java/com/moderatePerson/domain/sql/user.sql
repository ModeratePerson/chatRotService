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

 Date: 16/05/2024 22:41:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `userId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `phoneNumber` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `avatarUrl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `levelId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `itemId` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `points` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `registrationTime` timestamp NOT NULL DEFAULT '2024-05-16 20:49:00',
  `lastLoginTime` timestamp NOT NULL DEFAULT '2024-05-16 20:49:00',
  `ipAddress` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `requestCount` int(11) NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`userId`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'user1', '1234567890', 'https://example.com/avatar1.jpg', '1', '1', '100', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.1', 10, 'password1');
INSERT INTO `user` VALUES ('10', 'user10', '1234567890', 'https://example.com/avatar10.jpg', '1', '1', '1000', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.10', 100, 'password10');
INSERT INTO `user` VALUES ('11', 'user11', '2468013579', 'https://example.com/avatar11.jpg', '2', '2', '1100', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.11', 110, 'password11');
INSERT INTO `user` VALUES ('12', 'user12', '1357924680', 'https://example.com/avatar12.jpg', '3', '3', '1200', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.12', 120, 'password12');
INSERT INTO `user` VALUES ('13', 'user13', '0987654321', 'https://example.com/avatar13.jpg', '1', '1', '1300', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.13', 130, 'password13');
INSERT INTO `user` VALUES ('14', 'user14', '1234567890', 'https://example.com/avatar14.jpg', '2', '2', '1400', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.14', 140, 'password14');
INSERT INTO `user` VALUES ('15', 'user15', '2468013579', 'https://example.com/avatar15.jpg', '3', '3', '1500', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.15', 150, 'password15');
INSERT INTO `user` VALUES ('16', 'user16', '1357924680', 'https://example.com/avatar16.jpg', '1', '1', '1600', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.16', 160, 'password16');
INSERT INTO `user` VALUES ('17', 'user17', '0987654321', 'https://example.com/avatar17.jpg', '2', '2', '1700', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.17', 170, 'password17');
INSERT INTO `user` VALUES ('18', 'user18', '1234567890', 'https://example.com/avatar18.jpg', '3', '3', '1800', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.18', 180, 'password18');
INSERT INTO `user` VALUES ('19', 'user19', '2468013579', 'https://example.com/avatar19.jpg', '1', '1', '1900', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.19', 190, 'password19');
INSERT INTO `user` VALUES ('2', 'user2', '0987654321', 'https://example.com/avatar2.jpg', '2', '2', '200', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.2', 20, 'password2');
INSERT INTO `user` VALUES ('20', 'user20', '1357924680', 'https://example.com/avatar20.jpg', '2', '2', '2000', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.20', 200, 'password20');
INSERT INTO `user` VALUES ('21', 'user21', '0987654321', 'https://example.com/avatar21.jpg', '3', '3', '2100', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.21', 210, 'password21');
INSERT INTO `user` VALUES ('22', 'user22', '1234567890', 'https://example.com/avatar22.jpg', '1', '1', '2200', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.22', 220, 'password22');
INSERT INTO `user` VALUES ('23', 'user23', '2468013579', 'https://example.com/avatar23.jpg', '2', '2', '2300', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.23', 230, 'password23');
INSERT INTO `user` VALUES ('24', 'user24', '1357924680', 'https://example.com/avatar24.jpg', '3', '3', '2400', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.24', 240, 'password24');
INSERT INTO `user` VALUES ('25', 'user25', '0987654321', 'https://example.com/avatar25.jpg', '1', '1', '2500', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.25', 250, 'password25');
INSERT INTO `user` VALUES ('26', 'user26', '1234567890', 'https://example.com/avatar26.jpg', '2', '2', '2600', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.26', 260, 'password26');
INSERT INTO `user` VALUES ('27', 'user27', '2468013579', 'https://example.com/avatar27.jpg', '3', '3', '2700', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.27', 270, 'password27');
INSERT INTO `user` VALUES ('28', 'user28', '1357924680', 'https://example.com/avatar28.jpg', '1', '1', '2800', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.28', 280, 'password28');
INSERT INTO `user` VALUES ('29', 'user29', '0987654321', 'https://example.com/avatar29.jpg', '2', '2', '2900', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.29', 290, 'password29');
INSERT INTO `user` VALUES ('3', 'user3', '1357924680', 'https://example.com/avatar3.jpg', '3', '3', '300', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.3', 30, 'password3');
INSERT INTO `user` VALUES ('30', 'user30', '1234567890', 'https://example.com/avatar30.jpg', '3', '3', '3000', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.30', 300, 'password30');
INSERT INTO `user` VALUES ('4', 'user4', '2468013579', 'https://example.com/avatar4.jpg', '1', '1', '400', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.4', 40, 'password4');
INSERT INTO `user` VALUES ('5', 'user5', '0987654321', 'https://example.com/avatar5.jpg', '2', '2', '500', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.5', 50, 'password5');
INSERT INTO `user` VALUES ('6', 'user6', '1234567890', 'https://example.com/avatar6.jpg', '3', '3', '600', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.6', 60, 'password6');
INSERT INTO `user` VALUES ('7', 'user7', '2468013579', 'https://example.com/avatar7.jpg', '1', '1', '700', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.7', 70, 'password7');
INSERT INTO `user` VALUES ('8', 'user8', '1357924680', 'https://example.com/avatar8.jpg', '2', '2', '800', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.8', 80, 'password8');
INSERT INTO `user` VALUES ('9', 'user9', '0987654321', 'https://example.com/avatar9.jpg', '3', '3', '900', '2024-05-16 20:49:00', '2024-05-16 20:49:00', '192.168.1.9', 90, 'password9');

SET FOREIGN_KEY_CHECKS = 1;
