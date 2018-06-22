CREATE DATABASE IF NOT EXISTS `life_v2` /*!40100 DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_bin */;
USE `life_v2`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `wx_openid` varchar(50) NOT NULL,
  `nick_name` varchar(50) DEFAULT NULL,
  `entry_id` int(11) NOT NULL,
  `entry_date` datetime NOT NULL,
  `avatar_url` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userI1` (`wx_openid`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `user_login`;
CREATE TABLE `user_login` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `user_login_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `cost_group_user`;
CREATE TABLE `cost_group_user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `group_id` INT(11) NOT NULL,
  `user_id` INT(11) NOT NULL,
  `admin` CHAR(1) NOT NULL DEFAULT 'N',
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  `delete_id` INT(11) DEFAULT NULL,
  `delete_date` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `cost_group_user_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `cost_group_user_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `cost_group` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

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
) ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `cost_user_remark`;
CREATE TABLE `cost_user_remark` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `source_no` INT(11) NOT NULL,
  `target_no` INT(11) NOT NULL,
  `remark_name` VARCHAR(10) NOT NULL,
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`,`source_no`,`target_no`),
  KEY `source_no` (`source_no`),
  KEY `target_no` (`target_no`),
  CONSTRAINT `cost_user_remark_ibfk_1` FOREIGN KEY (`source_no`) REFERENCES `user` (`id`),
  CONSTRAINT `cost_user_remark_ibfk_2` FOREIGN KEY (`target_no`) REFERENCES `user` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `cost_clean`;
CREATE TABLE `cost_clean` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `comment` VARCHAR(50) NOT NULL,
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `cost_clean_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `cost_category`;
CREATE TABLE `cost_category` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `cate_name` VARCHAR(10) NOT NULL,
  `cate_icon` VARCHAR(50) NOT NULL,
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `cost_detail`;
CREATE TABLE `cost_detail` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `cate_id` INT(11) NOT NULL,
  `group_id` INT(11) NOT NULL,
  `cost_money` DECIMAL(10,2) NOT NULL,
  `cost_date` DATETIME NOT NULL,
  `cost_desc` VARCHAR(100) DEFAULT NULL,
  `clean_id` INT(11) DEFAULT NULL,
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  `delete_id` INT(11) DEFAULT NULL,
  `delete_date` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`,`user_id`),
  KEY `user_id` (`user_id`),
  KEY `cate_id` (`cate_id`),
  KEY `group_id` (`group_id`),
  KEY `clean_id` (`clean_id`),
  CONSTRAINT `cost_detail_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `cost_detail_ibfk_2` FOREIGN KEY (`cate_id`) REFERENCES `cost_category` (`id`),
  CONSTRAINT `cost_detail_ibfk_3` FOREIGN KEY (`group_id`) REFERENCES `cost_group` (`id`),
  CONSTRAINT `cost_detail_ibfk_4` FOREIGN KEY (`clean_id`) REFERENCES `cost_clean` (`id`)
) ENGINE=INNODB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `app_config`;
CREATE TABLE `app_config` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `app_name` VARCHAR(20) NOT NULL,
  `config_name` VARCHAR(50) NOT NULL,
  `config_value` VARCHAR(100) DEFAULT NULL,
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`,`app_name`,`config_name`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;



INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('1','吃喝','icon-chichihehe:before','-9999','2018-06-22 18:07:29');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('2','娱乐','icon-yule:beforee','-9999','2018-06-22 18:07:47');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('3','家电','icon-jiadian:before','-9999','2018-06-22 18:08:07');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('4','交通','icon-jiaotong:before','-9999','2018-06-22 18:08:23');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('5','日用品','icon-riyongpin:before','-9999','2018-06-22 18:08:39');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('6','旅游','icon-lvhang:before','-9999','2018-06-22 18:09:12');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('7','其他','icon-qita:before','-9999','2018-06-22 18:09:27');
