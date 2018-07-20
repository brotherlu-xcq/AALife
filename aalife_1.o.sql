CREATE DATABASE IF NOT EXISTS `life_v2` /*!40100 DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_bin */;
USE `life_v2`;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `wx_openid` varchar(50) NOT NULL,
  `nick_name` varchar(50) DEFAULT NULL,
  `avatar_url` varchar(250) DEFAULT NULL,
  `entry_id` int(11) NOT NULL,
  `entry_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `userI1` (`wx_openid`)
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `user_login`;
CREATE TABLE `user_login` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_loginI1` (`user_id`)
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
) ENGINE=INNODB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4;

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
  KEY `cost_group_userI1` (`user_id`),
  KEY `cost_group_userI2` (`group_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `cost_user_remark`;
CREATE TABLE `cost_user_remark` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `source_no` INT(11) NOT NULL,
  `target_no` INT(11) NOT NULL,
  `remark_name` VARCHAR(10) NOT NULL,
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`,`source_no`,`target_no`),
  KEY `cost_user_remarkI1` (`source_no`),
  KEY `cost_user_remarkI2` (`target_no`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `cost_clean`;
CREATE TABLE `cost_clean` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `group_id` INT(11) NOT NULL,
  `comment` VARCHAR(50) NOT NULL,
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cost_cleanI1` (`user_id`),
  KEY `cost_cleanI2` (`group_id`)
) ENGINE=INNODB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `cost_clean_user`;
CREATE TABLE `cost_clean_user` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `clean_id` INT(11) NOT NULL,
  `user_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `cost_clean_userI1` (`clean_id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `cost_category`;
CREATE TABLE `cost_category` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `cate_name` VARCHAR(10) NOT NULL,
  `cate_icon` VARCHAR(50) NOT NULL,
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=INNODB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `cost_detail`;
CREATE TABLE `cost_detail` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `cate_id` INT(11) NOT NULL,
  `group_id` INT(11) NOT NULL,
  `cost_money` DECIMAL(10,2) NOT NULL,
  `cost_date` DATE NOT NULL,
  `cost_desc` VARCHAR(100) DEFAULT NULL,
  `clean_id` INT(11) DEFAULT NULL,
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  `delete_id` INT(11) DEFAULT NULL,
  `delete_date` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`,`user_id`),
  KEY `cost_detailI1` (`user_id`),
  KEY `cost_detailI2` (`cate_id`),
  KEY `cost_detailI3` (`group_id`),
  KEY `cost_detailI4` (`clean_id`)
) ENGINE=INNODB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `app_config`;
CREATE TABLE `app_config` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `app_name` VARCHAR(20) NOT NULL,
  `config_name` VARCHAR(50) NOT NULL,
  `config_value` TEXT DEFAULT NULL,
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
  PRIMARY KEY (`id`),
  KEY `user_action_logI1` (`user_id`),
  KEY `user_action_logI2` (`entry_date`)
) ENGINE=INNODB DEFAULT CHARSET=utf8mb4;

DROP TABLE IF EXISTS `user_wx_form`;
CREATE TABLE `user_wx_form` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `form_id` VARCHAR(100) NOT NULL,
  `comment` VARCHAR(100) DEFAULT NULL,
  `entry_id` INT(11) NOT NULL,
  `entry_date` DATETIME NOT NULL,
  `delete_id` INT(11) DEFAULT NULL,
  `delete_date` DATETIME DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_wx_formI1` (`user_id`)
) ENGINE=INNODB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4;

INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('10000','吃喝','icon-chihe:before','-9999','2018-06-10 19:18:17');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('10001','娱乐','icon-yule:beforee','-9999','2018-06-22 18:07:47');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('10002','缴费','icon-jiaofei:beforee','-9999','2018-06-22 18:07:47');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('10003','药物','icon-yaowu:beforee','-9999','2018-06-22 18:07:47');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('10004','房租','icon-fangzu:beforee','-9999','2018-06-22 18:07:47');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('10005','家电','icon-jiadian:before','-9999','2018-06-22 18:08:07');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('10006','交通','icon-jiaotong:before','-9999','2018-06-22 18:08:23');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('10007','日用品','icon-riyongpin:before','-9999','2018-06-22 18:08:39');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('10008','门票','icon-menpiao:before','-9999','2018-06-22 18:09:12');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('10009','购物','icon-gouwu:before','-9999','2018-06-22 18:09:27');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('10010','住宿','icon-zhusu:before','-9999','2018-06-22 18:09:27');
INSERT INTO `cost_category` (`id`, `cate_name`, `cate_icon`, `entry_id`, `entry_date`) VALUES('10011','其他','icon-money:before','-9999','2018-06-22 18:09:27');


INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('1','INVOICE','HOST','http://vop.baidu.com/server_api','-9999','2018-06-26 18:28:13');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('2','INVOICE','SECRET','SvZG4PwLkxEZ2WPWsD1gQTpq','-9999','2018-06-26 19:11:44');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('3','INVOICE','KEY','13a30b5edee7c363e52076a0a6cbd7d1','-9999','2018-06-26 19:12:06');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('4','INVOICE','TOKEN_HOST','http://openapi.baidu.com/oauth/2.0/token','-9999','2018-06-26 19:12:44');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('5','WX','APPID','wx9c7abce098df46e1','-9999','2018-06-27 18:13:32');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('6','WX','SECRET','9fcee84ff426e4a939833a87e56c32e9','-9999','2018-06-27 18:13:49');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('7','WX','HOST','https://api.weixin.qq.com/sns/jscode2session','-9999','2018-06-27 18:14:17');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('8','AALIFE','ENV','DEV','-9999','2018-07-09 10:08:36');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('10','INVOICE','TOKEN','24.90086ab62e7790c6c37f10b43dec2866.2592000.1533695317.282335-10462243','-9999','2018-07-09 10:28:37');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('11','INVOICE','DEV_PID','1536','-9999','2018-07-09 17:57:43');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('12','INVOICE','RATE','16000','-9999','2018-07-09 17:58:13');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('13','WX','TEMPLATE_HOST','https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token=','-9999','2018-07-13 18:02:58');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('14','WX','SESSION_HOST','https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wx9c7abce098df46e1&secret=9fcee84ff426e4a939833a87e56c32e9','-9999','2018-07-13 20:27:57');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('15','WX_PAGE','APPROVAL_REQUEST','pages/index/index?groupId=','-9999','2018-07-13 22:43:08');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('16','WX_TEMP','APPROVAL_REQUEST','MHNNdUvDh5lYh1rEMB9D8EGgQtMz9J2S9DFbyRpXWYw','-9999','2018-07-13 22:43:43');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('17','WX_PAGE','CLEAN_RESULT','pages/settleDetail/settleDetail','-9999','2018-07-13 22:44:57');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('18','WX_TEMP','CLEAN_RESULT','PncFKdUq4YVB88mT4SWnTR8RVas2H4h-fSqO4usThCQ','-9999','2018-07-13 23:49:58');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('19','WX_TEMP','APPROVAL_PASS','OncK6zz6194ietR8BVWtQKmz-7EtOhmB0WJyp3pbopg','-9999','2018-07-13 23:55:45');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('20','WX_PAGE','APPROVAL_PASS','pages/index/index?groupId=','-9999','2018-07-14 00:43:25');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('21','MAIL_TEMP','DAILY_REPORT','<table>\r\n	<th>业务块</th>\r\n	<th>数量</th>\r\n	<#list data as item>\r\n	<tr>\r\n		<td>${item.name}</td>\r\n		<td>${item.value}</td>\r\n	</tr>\r\n	</#list>\r\n</table>','-9999','2018-07-16 18:25:24');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('22','MAIL_TO','DAILY_REPORT','1285823170@qq.com','-9999','2018-07-16 18:25:50');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('23','MAIL_CC','DAILY_REPORT','1285823170@qq.com','-9999','2018-07-16 18:26:14');
INSERT INTO `app_config` (`id`, `app_name`, `config_name`, `config_value`, `entry_id`, `entry_date`) VALUES('24','MAIL_SUB','DAILY_REPORT','测试邮件','-9999','2018-07-16 18:26:39');
