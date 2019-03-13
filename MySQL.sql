CREATE DATABASE chat_demo DEFAULT CHARACTER
SET utf8 COLLATE utf8_general_ci;

USE chat_demo;


SET NAMES utf8;


SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `message`
-- ----------------------------
DROP TABLE
IF EXISTS `message`;

CREATE TABLE `message` (
	`id` VARCHAR (40) NOT NULL,
	`from_user_id` VARCHAR (40) NOT NULL,
	`to_user_id` VARCHAR (40) NOT NULL,
	`content` VARCHAR (255) DEFAULT NULL,
	`send_time` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
	`status` TINYINT (4) DEFAULT '0',
	`from_user_delete` BIT (1) DEFAULT FALSE,
	`to_user_delete` BIT (1) DEFAULT FALSE,
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8;

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE
IF EXISTS `user`;

CREATE TABLE `user` (
	`id` VARCHAR (40) NOT NULL,
	`create_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
	`modify_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`is_delete` BIT (1) DEFAULT FALSE,
	`name` VARCHAR (20) DEFAULT NULL,
	`password` VARCHAR (20) DEFAULT NULL,
	`phone` VARCHAR (11) DEFAULT NULL,
	`email` VARCHAR (20) DEFAULT NULL,
	PRIMARY KEY (`id`)
) ENGINE = INNODB DEFAULT CHARSET = utf8;

-- ----------------------------
--  Records of `user`
-- ----------------------------
BEGIN
;

INSERT INTO `user`
VALUES
	(
		'cf4f50b1-e147-4cf9-8ac6-f28b78a6d885',
		'2019-01-29 11:10:13',
		'2019-02-14 10:49:25',
		FALSE,
		'roy',
		'123456',
		'15881016542',
		'545344387@qq.com'
	),
	(
		'dc3db082-8f88-425b-b9e6-c88af1fe8444',
		'2019-01-29 11:08:34',
		'2019-02-14 10:49:27',
		FALSE,
		'yige',
		'123456',
		'13981010878',
		'345344264@qq.com'
	),
	(
		'f52105ea-da07-4366-9305-5be3912adcf9',
		'2019-01-29 11:06:09',
		'2019-02-14 10:49:29',
		FALSE,
		'kk20',
		'123456',
		'13989090909',
		'756534264@qq.com'
	);

COMMIT;


SET FOREIGN_KEY_CHECKS = 1;

