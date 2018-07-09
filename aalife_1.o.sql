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
  KEY `user_id` (`user_id`)
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
  KEY `group_id` (`group_id`)
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
  KEY `cost_group_approval_ibfk_2` (`user_id`)
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
  KEY `target_no` (`target_no`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `cost_clean`;
CREATE TABLE `cost_clean` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `comment` VARCHAR(50) NOT NULL,
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`)
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
  KEY `clean_id` (`clean_id`)
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

DROP TABLE IF EXISTS `user_action_log`;
CREATE TABLE `user_action_log` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `method_name` VARCHAR(100) DEFAULT NULL,
  `user_id` INT(11) DEFAULT NULL,
  `request_url` VARCHAR(500) DEFAULT NULL,
  `in_params` TEXT,
  `out_params` TEXT,
  `exception` TEXT,
  `session_id` VARBINARY(100) DEFAULT NULL,
  `ip_address` VARCHAR(50) DEFAULT NULL,
  `start_date` DATETIME DEFAULT NULL,
  `end_date` DATETIME DEFAULT NULL,
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('1','吃喝','icon-chichihehe:before','-9999','2018-06-22 18:07:29');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('2','娱乐','icon-yule:beforee','-9999','2018-06-22 18:07:47');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('3','家电','icon-jiadian:before','-9999','2018-06-22 18:08:07');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('4','交通','icon-jiaotong:before','-9999','2018-06-22 18:08:23');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('5','日用品','icon-riyongpin:before','-9999','2018-06-22 18:08:39');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('6','旅游','icon-lvhang:before','-9999','2018-06-22 18:09:12');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('7','其他','icon-qita:before','-9999','2018-06-22 18:09:27');


INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('1','INVOICE','HOST','http://vop.baidu.com/server_api','-9999','2018-06-26 18:28:13');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('2','INVOICE','SECRET','SvZG4PwLkxEZ2WPWsD1gQTpq','-9999','2018-06-26 19:11:44');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('3','INVOICE','KEY','13a30b5edee7c363e52076a0a6cbd7d1','-9999','2018-06-26 19:12:06');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('4','INVOICE','TOKEN_HOST','http://openapi.baidu.com/oauth/2.0/token','-9999','2018-06-26 19:12:44');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('5','WX','APPID','wx9c7abce098df46e1','-9999','2018-06-27 18:13:32');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('6','WX','SECRET','9fcee84ff426e4a939833a87e56c32e9','-9999','2018-06-27 18:13:49');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('7','WX','HOST','https://api.weixin.qq.com/sns/jscode2session','-9999','2018-06-27 18:14:17');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('11','INVOICE','DEV_PID','1536','-9999','2018-07-09 17:57:43');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('12','INVOICE','RATE','16000','-9999','2018-07-09 17:58:13');
