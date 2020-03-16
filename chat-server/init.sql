CREATE DATABASE chat_demo DEFAULT CHARACTER
    SET utf8 COLLATE utf8_general_ci;

USE chat_demo;
# 避免出现乱码
SET NAMES utf8;
# 取消外键约束
SET FOREIGN_KEY_CHECKS = 0;

-- -----------------------------
--  Table structure for `c_user`
-- -----------------------------
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

-- --------------------------------
--  Table structure for `c_group`
-- --------------------------------
DROP TABLE
    IF EXISTS `c_group`;
CREATE TABLE `c_group`
(
    `id`           BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name`         VARCHAR(20)        DEFAULT NULL COMMENT '群名称',
    `description`  VARCHAR(256)       DEFAULT NULL COMMENT '群描述',
    `creator_id`   BIGINT    NOT NULL COMMENT '创建者',
    `manager_list` VARCHAR(256)       DEFAULT NULL COMMENT '管理员',
    `member_list`  TEXT               DEFAULT NULL COMMENT '群成员',
    `is_delete`    BIT(1)             DEFAULT FALSE COMMENT '是否删除',
    `create_date`  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_date`  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT '群组表';

-- --------------------------------
--  Table structure for `c_message`
-- --------------------------------
DROP TABLE
    IF EXISTS `c_message`;
CREATE TABLE `c_message`
(
    `id`               BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `from_user_id`     BIGINT    NOT NULL COMMENT '发送者ID',
    `to_user_id`       BIGINT    NOT NULL COMMENT '接收者ID',
    `content_type`     TINYINT            DEFAULT NULL COMMENT '消息内容类型',
    `content`          TEXT               DEFAULT NULL COMMENT '消息内容',
    `received`         BIT(1)             DEFAULT FALSE COMMENT '是否已接收',
    `from_user_delete` BIT(1)             DEFAULT FALSE COMMENT '发送者删除',
    `to_user_delete`   BIT(1)             DEFAULT FALSE COMMENT '接收者删除',
    `is_delete`        BIT(1)             DEFAULT FALSE COMMENT '是否删除',
    `create_date`      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_date`      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `index_from_user_id` (`from_user_id`),
    KEY `index_to_user_id` (`to_user_id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT '消息表';

-- --------------------------------------
--  Table structure for `c_group_message`
-- --------------------------------------
DROP TABLE
    IF EXISTS `c_group_message`;
CREATE TABLE `c_group_message`
(
    `id`           BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`      BIGINT    NOT NULL COMMENT '发送者ID',
    `group_id`     BIGINT    NOT NULL COMMENT '群ID',
    `content_type` TINYINT            DEFAULT NULL COMMENT '消息内容类型',
    `content`      TEXT               DEFAULT NULL COMMENT '消息内容',
    `is_delete`    BIT(1)             DEFAULT FALSE COMMENT '是否删除',
    `create_date`  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_date`  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `index_user_id` (`user_id`),
    KEY `index_group_id` (`group_id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT '群消息表';

-- -------------------------------------
--  Table structure for `c_login_log`
-- -------------------------------------
DROP TABLE
    IF EXISTS `c_login_log`;
CREATE TABLE `c_login_log`
(
    `id`          BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`     BIGINT    NOT NULL COMMENT '用户ID',
    `ip`          VARCHAR(255)       DEFAULT NULL COMMENT '主机IP',
    `location`    VARCHAR(255)       DEFAULT NULL COMMENT '登录地址',
    `device`      VARCHAR(255)       DEFAULT NULL COMMENT '登录设备',
    `is_delete`   BIT(1)             DEFAULT FALSE COMMENT '是否删除',
    `create_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `index_user_id` (`user_id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT '登录日志表';

-- ------------------------------------------
--  Table structure for `c_group_message_log`
-- ------------------------------------------
DROP TABLE
    IF EXISTS `c_group_message_log`;
CREATE TABLE `c_group_message_log`
(
    `id`           BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `group_msg_id` BIGINT    NOT NULL COMMENT '消息ID',
    `user_id`      BIGINT    NOT NULL COMMENT '接收者ID',
    `received`     BIT(1)             DEFAULT FALSE COMMENT '是否已接收',
    `is_delete`    BIT(1)             DEFAULT FALSE COMMENT '是否删除',
    `create_date`  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `modify_date`  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY `index_group_msg_id` (`group_msg_id`),
    KEY `index_user_id` (`user_id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8 COMMENT '群消息日志表';

-- ---------------------------------
--  Table structure for c_apply_log`
-- ---------------------------------
DROP TABLE
    IF EXISTS `c_apply_log`;
CREATE TABLE `c_apply_log`
(
    `id`             BIGINT    NOT NULL AUTO_INCREMENT COMMENT '主键',
    `type`           TINYINT   NOT NULL COMMENT '申请类型：0-加人，1-加群',
    `apply_user_id`  BIGINT    NOT NULL COMMENT '申请人ID',
    `target_user_id` BIGINT    NOT NULL COMMENT '目标ID：好友或群组',
    `verify_user_id` BIGINT    NOT NULL COMMENT '审批人ID',
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
  DEFAULT CHARSET = utf8 COMMENT '申请日志表';

# 重置外键约束
SET FOREIGN_KEY_CHECKS = 1;

