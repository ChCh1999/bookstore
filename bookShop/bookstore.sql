/*
Navicat MySQL Data Transfer

Source Server         : localhost_3306
Source Server Version : 80013
Source Host           : 127.0.0.1:3306
Source Database       : bookstore

Target Server Type    : MYSQL
Target Server Version : 80013
File Encoding         : 65001

Date: 2020-03-12 22:32:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for book
-- ----------------------------
DROP TABLE IF EXISTS `book`;
CREATE TABLE `book` (
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `publisher` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `id` int(6) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `img` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `storeCount` int(11) DEFAULT NULL,
  `info` varchar(512) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bookID` int(10) unsigned zerofill NOT NULL,
  `userAccount` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `count` int(10) unsigned zerofill NOT NULL,
  PRIMARY KEY (`id`),
  KEY `book_ref` (`bookID`),
  KEY `user_ref` (`userAccount`),
  CONSTRAINT `book_ref` FOREIGN KEY (`bookID`) REFERENCES `book` (`id`) ON UPDATE CASCADE,
  CONSTRAINT `user_ref` FOREIGN KEY (`userAccount`) REFERENCES `user` (`account`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `account` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(16) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_unicode_ci NOT NULL,
  `address` varchar(64) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `province` varchar(16) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `city` varchar(16) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  `money` float DEFAULT NULL,
  `info` varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`account`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
