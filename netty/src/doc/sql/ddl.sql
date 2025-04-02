CREATE DATABASE `chatroom` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
-- chatroom.t_group definition

CREATE TABLE `t_group` (
                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                           `group_name` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '群聊名',
                           `owner` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
                           `user_ids` varchar(2000) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '群聊用户',
                           `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除标记(0-未删除,1-已删除)',
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='群聊表';
-- chatroom.t_msg definition

CREATE TABLE `t_msg` (
                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                         `sender_id` bigint NOT NULL COMMENT '发送者id',
                         `receiver_id` bigint NOT NULL COMMENT '接收者id',
                         `group_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '群聊id（为空即私聊）',
                         `content` varchar(2000) COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT '' COMMENT '消息内容',
                         `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                         `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                         `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除标记(0-未删除,1-已删除)',
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1907397463969980419 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='信息表';

-- chatroom.t_user definition

CREATE TABLE `t_user` (
                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                          `username` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '用户名',
                          `password` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '密码(BCrypt加密)',
                          `nickname` varchar(100) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '昵称',
                          `birthday` date DEFAULT NULL COMMENT '生日',
                          `register_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
                          `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
                          `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          `update_time` datetime DEFAULT NULL COMMENT '更新时间',
                          `is_online` tinyint(1) NOT NULL DEFAULT '0' COMMENT '0-不在线 1-在线',
                          `is_deleted` tinyint(1) DEFAULT '0' COMMENT '逻辑删除标记(0-未删除,1-已删除)',
                          PRIMARY KEY (`id`),
                          UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

