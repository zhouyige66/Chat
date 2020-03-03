CREATE DATABASE chat_demo DEFAULT CHARACTER
    SET utf8 COLLATE utf8_general_ci;

USE chat_demo;
# 避免出现乱码
SET NAMES utf8;
# 取消外键约束
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `user`
-- ----------------------------
DROP TABLE
    IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`          BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        VARCHAR(20)    DEFAULT NULL COMMENT '姓名',
    `phone`       VARCHAR(11)    DEFAULT NULL COMMENT '电话',
    `email`       VARCHAR(20)    DEFAULT NULL COMMENT '邮箱',
    `password`    VARCHAR(20)    DEFAULT NULL COMMENT '密码',
    `head`        VARCHAR(256)   DEFAULT NULL COMMENT '用户头像',
    `friends`     TEXT           DEFAULT NULL COMMENT '好友列表',
    `groups`      TEXT           DEFAULT NULL COMMENT '群列表',
    `is_delete`   BIT(1)         DEFAULT FALSE COMMENT '是否删除',
    `create_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT '用户表';

-- ----------------------------
--  Table structure for `group`
-- ----------------------------
DROP TABLE
    IF EXISTS `group`;
CREATE TABLE `group`
(
    `id`          BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        VARCHAR(20)    DEFAULT NULL COMMENT '群名称',
    `description` varchar(256)   DEFAULT NULL COMMENT '群描述',
    `creator`     BIGINT    NOT NULL COMMENT '创建者',
    `manager`     VARCHAR(256)   DEFAULT NULL COMMENT '管理员',
    `members`     TEXT           DEFAULT NULL COMMENT '群成员',
    `is_delete`   BIT(1)         DEFAULT FALSE COMMENT '是否删除',
    `create_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT '群分组表';

-- ----------------------------
--  Table structure for `message`
-- ----------------------------
DROP TABLE
    IF EXISTS `message`;
CREATE TABLE `message`
(
    `id`               BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `from_user_id`     BIGINT    NOT NULL COMMENT '发送者',
    `to_user_id`       BIGINT    NOT NULL COMMENT '接收者',
    `content`          TEXT           DEFAULT NULL COMMENT '消息内容',
    `send_time`        TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    `status`           TINYINT(4)     DEFAULT '0' COMMENT '消息状态',
    `from_user_delete` BIT(1)         DEFAULT FALSE COMMENT '发送者删除',
    `to_user_delete`   BIT(1)         DEFAULT FALSE COMMENT '接收者删除',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT '消息表';

-- ----------------------------
--  Table structure for `message_log`
-- ----------------------------
DROP TABLE
    IF EXISTS `group_message_log`;
CREATE TABLE `group_message_log`
(
    `id`          BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `msg_id`      BIGINT    NOT NULL COMMENT '消息ID',
    `operator`    BIGINT    NOT NULL COMMENT '操作者',
    `create_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `index_msg_id` (`msg_id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT '群消息删除记录表';

-- ----------------------------
--  Table structure for `add_friend_log`
-- ----------------------------
DROP TABLE
    IF EXISTS `add_friend_log`;
CREATE TABLE `add_friend_log`
(
    `id`           BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `from_user_id` BIGINT    NOT NULL COMMENT '申请人',
    `to_user_id`   BIGINT    NOT NULL COMMENT '好友人',
    `is_agree`     BIT(1)    NOT NULL DEFAULT FALSE COMMENT '是否同意',
    `create_date`  TIMESTAMP NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_date`  TIMESTAMP NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `index_from_user_id` (`from_user_id`),
    KEY `index_to_user_id` (`to_user_id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT '添加好友申请记录表';

-- ----------------------------
--  Table structure for `add_group_log`
-- ----------------------------
DROP TABLE
    IF EXISTS `add_group_log`;
CREATE TABLE `add_group_log`
(
    `id`           BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `group_id`     BIGINT    NOT NULL COMMENT '群ID',
    `from_user_id` BIGINT    NOT NULL COMMENT '推荐人',
    `to_user_id`   BIGINT    NOT NULL COMMENT '受邀人',
    `is_agree`     BIT(1)    NOT NULL DEFAULT FALSE COMMENT '管理员是否同意',
    `create_date`  TIMESTAMP NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_date`  TIMESTAMP NULL     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `index_group_id` (`group_id`),
    KEY `index_from_user_id` (`from_user_id`),
    KEY `index_to_user_id` (`to_user_id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT '加入群申请记录表';

# 重置外键约束
SET FOREIGN_KEY_CHECKS = 1;

