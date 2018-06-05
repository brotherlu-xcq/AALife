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
  `admin_id` INT(11) NOT NULL,
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  `delete_id` INT(11) DEFAULT NULL,
  `delete_date` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `admin_id` (`admin_id`),
  CONSTRAINT `cost_group_ibfk_1` FOREIGN KEY (`admin_id`) REFERENCES `user` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;