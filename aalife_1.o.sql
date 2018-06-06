CREATE DATABASE IF NOT EXISTS `life_v2` /*!40100 DEFAULT CHARACTER SET utf8
  COLLATE utf8_bin */;
USE `life_v2`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `wx_openid` varchar(50) NOT NULL,
  `nick_name` varchar(50) DEFAULT NULL,
  `entry_id` int(11) NOT NULL,
  `entry_date` datetime NOT NULL,
  `avatar_url` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userI1` (`wx_openid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `user_login`;
CREATE TABLE `user_login` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_login_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `cost_group`;
CREATE TABLE `cost_group` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `group_name` VARCHAR(10) NOT NULL,
  `group_code` VARCHAR(16) NOT NULL,
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  `delete_id` INT(11) DEFAULT NULL,
  `delete_date` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `cost_groupI1` (`group_code`)
) ENGINE=INNODB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `cost_group_user`;
CREATE TABLE `cost_group_user` (
  `group_id` INT(11) NOT NULL,
  `user_id` INT(11) NOT NULL,
  `admin` CHAR(1) NOT NULL DEFAULT 'N',
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  `delete_id` INT(11) DEFAULT NULL,
  `delete_date` DATETIME DEFAULT NULL,
  KEY `user_id` (`user_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `cost_group_user_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `cost_group_user_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `cost_group` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `cost_group_approval`;
CREATE TABLE `cost_group_approval` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `group_id` INT(11) NOT NULL,
  `user_id` INT(11) NOT NULL,
  `comment` VARCHAR(50) NOT NULL,
  `status` INT(11) NOT NULL DEFAULT '0' COMMENT '0：未处理 1：已接受',
  `approval_id` INT(11) DEFAULT NULL,
  `approval_date` DATETIME DEFAULT NULL,
  `entry_date` DATETIME NOT NULL,
  `entry_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `group_id` (`group_id`),
  KEY `approval_id` (`approval_id`),
  KEY `cost_group_approval_ibfk_2` (`user_id`),
  CONSTRAINT `cost_group_approval_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `cost_group` (`id`),
  CONSTRAINT `cost_group_approval_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `cost_group_approval_ibfk_3` FOREIGN KEY (`approval_id`) REFERENCES `user` (`id`)
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;