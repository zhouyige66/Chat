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
    IF EXISTS `c_user`;
CREATE TABLE `c_user`
(
    `id`          BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`        VARCHAR(20)        DEFAULT NULL COMMENT '姓名',
    `phone`       VARCHAR(11)        DEFAULT NULL COMMENT '电话',
    `email`       VARCHAR(20)        DEFAULT NULL COMMENT '邮箱',
    `password`    VARCHAR(20)        DEFAULT NULL COMMENT '密码',
    `head`        VARCHAR(256)       DEFAULT NULL COMMENT '用户头像',
    `friend_list` TEXT               DEFAULT NULL COMMENT '好友列表',
    `group_list`  TEXT               DEFAULT NULL COMMENT '群列表',
    `is_delete`   BIT(1)             DEFAULT FALSE COMMENT '是否删除',
    `create_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT '用户表';

-- ----------------------------
--  Table structure for `group`
-- ----------------------------
DROP TABLE
    IF EXISTS `c_group`;
CREATE TABLE `c_group`
(
    `id`           BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`         VARCHAR(20)        DEFAULT NULL COMMENT '群名称',
    `description`  varchar(256)       DEFAULT NULL COMMENT '群描述',
    `creator_id`   BIGINT    NOT NULL COMMENT '创建者',
    `manager_list` VARCHAR(256)       DEFAULT NULL COMMENT '管理员',
    `member_list`  TEXT               DEFAULT NULL COMMENT '群成员',
    `is_delete`    BIT(1)             DEFAULT FALSE COMMENT '是否删除',
    `create_date`  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_date`  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT '群分组表';

-- ----------------------------
--  Table structure for `message`
-- ----------------------------
DROP TABLE
    IF EXISTS `c_message`;
CREATE TABLE `c_message`
(
    `id`               BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `from_user_id`     BIGINT    NOT NULL COMMENT '发送者',
    `to_user_id`       BIGINT    NOT NULL COMMENT '接收者',
    `content`          TEXT               DEFAULT NULL COMMENT '消息内容',
    `send_time`        TIMESTAMP NULL     DEFAULT CURRENT_TIMESTAMP COMMENT '发送时间',
    `status`           TINYINT(4)         DEFAULT '0' COMMENT '消息状态',
    `from_user_delete` BIT(1)             DEFAULT FALSE COMMENT '发送者删除',
    `to_user_delete`   BIT(1)             DEFAULT FALSE COMMENT '接收者删除',
    `is_delete`        BIT(1)             DEFAULT FALSE COMMENT '是否删除',
    `create_date`      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_date`      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT '消息表';

-- ----------------------------
--  Table structure for `message_log`
-- ----------------------------
DROP TABLE
    IF EXISTS `c_group_message_log`;
CREATE TABLE `c_group_message_log`
(
    `id`          BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `msg_id`      BIGINT    NOT NULL COMMENT '消息ID',
    `operator_id` BIGINT    NOT NULL COMMENT '操作者',
    `is_delete`   BIT(1)             DEFAULT FALSE COMMENT '是否删除',
    `create_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `index_msg_id` (`msg_id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT '群消息删除记录表';

-- ----------------------------
--  Table structure for `add_friend_log`
-- ----------------------------
DROP TABLE
    IF EXISTS `c_apply_log`;
CREATE TABLE `c_apply_log`
(
    `id`             BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `type`           INTEGER   NOT NULL COMMENT '申请类型：0-加人，1-加群',
    `apply_user_id`  BIGINT    NOT NULL COMMENT '申请人',
    `target_user_id` BIGINT    NOT NULL COMMENT '目标：好友或群组',
    `verify_user_id` BIGINT    NOT NULL COMMENT '审批人',
    `is_agree`       BIT(1)             DEFAULT NULL COMMENT '是否同意',
    `apply_remark`   VARCHAR(255)       DEFAULT NULL COMMENT '申请备注',
    `verify_remark`  VARCHAR(255)       DEFAULT NULL COMMENT '审批备注',
    `is_delete`      BIT(1)             DEFAULT FALSE COMMENT '是否删除',
    `create_date`    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_date`    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `index_apply_user_id` (`apply_user_id`),
    KEY `index_target_user_id` (`target_user_id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT '添加好友申请记录表';

# 重置外键约束
SET FOREIGN_KEY_CHECKS = 1;

