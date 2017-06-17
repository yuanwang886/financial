SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `p2p_account`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_account`;
CREATE TABLE `p2p_account` (
  `user_id` bigint(20) NOT NULL,
  `bank` varchar(64) NOT NULL COMMENT '银行',
  `bank_detail` varchar(64) NOT NULL COMMENT '开户行',
  `user_name` varchar(20) NOT NULL COMMENT '开户名',
  `bank_card` varchar(20) NOT NULL COMMENT '银行卡号',
  `alipay` varchar(40) NOT NULL COMMENT '支付宝账号',
  `create_time` timestamp NULL DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户银行信息表';

-- ----------------------------
--  Table structure for `p2p_business_record`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_business_record`;
CREATE TABLE `p2p_business_record` (
  `id` bigint(20) NOT NULL,
  `buy_id` bigint(20) NOT NULL COMMENT '买入订单',
  `sell_id` bigint(20) NOT NULL COMMENT '卖出订单',
  `type` int(2) NOT NULL DEFAULT '1' COMMENT '对于买入的才有 1预付款 2剩余款',
  `buy_account_id` bigint(20) NOT NULL COMMENT '买家',
  `sell_account_id` bigint(20) NOT NULL COMMENT '卖家',
  `money` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '匹配金额',
  `state` int(2) NOT NULL DEFAULT '0' COMMENT ' 0匹配未打款  1 已打款 2 对方已确认  9 冻结',
  `pic_url` varchar(128) DEFAULT NULL COMMENT '打款上传截图地址',
  `lock_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `modify_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户匹配打款表';

-- ----------------------------
--  Table structure for `p2p_buy`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_buy`;
CREATE TABLE `p2p_buy` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `money` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '界面买入金额',
  `match_money` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '系统匹配剩余金额',
  `out_money` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '实际已经打款金额',
  `state` int(2) NOT NULL DEFAULT '0' COMMENT '0未激活   4匹配完成  5 交易完成  9 冻结',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户买入表';

-- ----------------------------
--  Table structure for `p2p_code`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_code`;
CREATE TABLE `p2p_code` (
  `code_type` varchar(20) NOT NULL COMMENT 'code',
  `code` varchar(20) NOT NULL COMMENT 'code',
  `name` varchar(50) NOT NULL COMMENT 'name',
  `order_id` int(2) NOT NULL DEFAULT '0' COMMENT '排序',
  `remark` varchar(100) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`code_type`,`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='键值表';

-- ----------------------------
--  Table structure for `p2p_feedback`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_feedback`;
CREATE TABLE `p2p_feedback` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `content` varchar(500) DEFAULT NULL COMMENT '反馈内容',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `img_url` varchar(128) DEFAULT NULL COMMENT '上传图片',
  `create_time` timestamp NULL DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='意见反馈表';

-- ----------------------------
--  Table structure for `p2p_income_buy`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_income_buy`;
CREATE TABLE `p2p_income_buy` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '买入订单',
  `money` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '金额',
  `interest` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '利息',
  `start_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `modify_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户金额利息表';

-- ----------------------------
--  Table structure for `p2p_income_track`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_income_track`;
CREATE TABLE `p2p_income_track` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `money` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '金额',
  `type` int(2) NOT NULL DEFAULT '0' COMMENT '1 买入  2 卖出  3 利息',
  `remark` varchar(128) NOT NULL COMMENT '对应的条目说明',
  `income_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `modify_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户金额轨迹表';

-- ----------------------------
--  Table structure for `p2p_menu`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_menu`;
CREATE TABLE `p2p_menu` (
  `menu_id` bigint(20) NOT NULL COMMENT 'ID',
  `menu_name` varchar(32) DEFAULT NULL COMMENT '菜单名称',
  `menu_icon` varchar(128) DEFAULT NULL,
  `menu_url` varchar(256) DEFAULT NULL COMMENT '菜单URL',
  `menu_pid` bigint(20) DEFAULT NULL COMMENT '父菜单ID',
  `menu_status` int(2) DEFAULT NULL COMMENT '菜单状态',
  `menu_level` int(2) DEFAULT NULL COMMENT '菜单级别',
  `menu_order` int(4) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='平台菜单表';

-- ----------------------------
--  Table structure for `p2p_p_notice`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_p_notice`;
CREATE TABLE `p2p_p_notice` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `title` varchar(64) DEFAULT NULL COMMENT '标题',
  `content` varchar(500) DEFAULT NULL COMMENT '内容',
  `create_time` timestamp NULL DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户通知表';

-- ----------------------------
--  Table structure for `p2p_release`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_release`;
CREATE TABLE `p2p_release` (
  `user_id` bigint(20) NOT NULL,
  `sum_num` int(6) NOT NULL DEFAULT '0' COMMENT '购买总个数',
  `used_num` int(6) NOT NULL DEFAULT '0' COMMENT '激活用户个数',
  `sell_num` int(6) NOT NULL DEFAULT '0' COMMENT '卖出个数',
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='激活码个数表';

-- ----------------------------
--  Table structure for `p2p_release_code`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_release_code`;
CREATE TABLE `p2p_release_code` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `code` varchar(6) NOT NULL COMMENT 'code',
  `state` int(2) NOT NULL DEFAULT '0' COMMENT '0未使用   1已使用',
  `create_time` datetime NOT NULL,
  `modify_time` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='激活码表';

-- ----------------------------
--  Table structure for `p2p_role`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_role`;
CREATE TABLE `p2p_role` (
  `role_id` bigint(20) NOT NULL COMMENT '自增ID',
  `role_name` varchar(32) DEFAULT NULL COMMENT '角色名称',
  `role_desc` varchar(256) DEFAULT NULL COMMENT '角色描述',
  `role_order` int(4) DEFAULT NULL COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '新增时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='平台角色表';

-- ----------------------------
--  Table structure for `p2p_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_role_menu`;
CREATE TABLE `p2p_role_menu` (
  `role_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色ID',
  `menu_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='平台角色菜单表';

-- ----------------------------
--  Table structure for `p2p_s_notice`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_s_notice`;
CREATE TABLE `p2p_s_notice` (
  `id` bigint(20) NOT NULL,
  `title` varchar(64) DEFAULT NULL COMMENT '标题',
  `content` varchar(500) DEFAULT NULL COMMENT '内容',
  `create_time` timestamp NULL DEFAULT NULL,
  `modify_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统通知表';

-- ----------------------------
--  Table structure for `p2p_sell`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_sell`;
CREATE TABLE `p2p_sell` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `money` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '界面提现金额',
  `match_money` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '系统匹配剩余金额',
  `income_money` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '实际已经收款金额',
  `state` int(2) NOT NULL DEFAULT '0' COMMENT '0未激活  3匹配中 4匹配完成  5 交易完成',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `modify_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户提现表';

-- ----------------------------
--  Table structure for `p2p_sms`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_sms`;
CREATE TABLE `p2p_sms` (
  `id` bigint(20) NOT NULL,
  `mobile` varchar(20) DEFAULT NULL COMMENT '发送手机',
  `content` varchar(500) DEFAULT NULL COMMENT '短信内容',
  `business_type` int(4) DEFAULT NULL COMMENT '业务类型',
  `report_status` int(2) DEFAULT NULL COMMENT '0:失败 1成功',
  `error_code` varchar(32) DEFAULT NULL COMMENT '未发送成功错误编码',
  `rep_result` varchar(1000) DEFAULT NULL COMMENT '获取阿里结果',
  `rep_error` varchar(1000) DEFAULT NULL COMMENT '获取阿里结果',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `modify_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='短信表';

-- ----------------------------
--  Table structure for `p2p_user`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_user`;
CREATE TABLE `p2p_user` (
  `user_id` bigint(20) NOT NULL,
  `user_role` int(2) NOT NULL DEFAULT '1' COMMENT '用户角色 1:普通用户  2:平台操作用户',
  `user_level` int(2) NOT NULL DEFAULT '1' COMMENT '用户等级 1:普通用户  2:平台操作用户',
  `password` varchar(128) DEFAULT NULL,
  `phone` varchar(20) NOT NULL COMMENT '手机',
  `nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
  `realname` varchar(64) DEFAULT NULL COMMENT '真实姓名',
  `sex` int(2) NOT NULL DEFAULT '1' COMMENT '性别  1男 2女  3未知',
  `birthday` varchar(10) DEFAULT NULL COMMENT '生日',
  `state` int(2) NOT NULL DEFAULT '0' COMMENT '用户状态  0未锁定  1已锁定',
  `udid` varchar(64) DEFAULT NULL COMMENT '推送ID pushid',
  `pay_password` varchar(128) DEFAULT NULL,
  `meg_switch` int(2) NOT NULL DEFAULT '1' COMMENT '用于以后手机端 消息通知设置 0不接收 1接收',
  `invite_phone` varchar(20) DEFAULT NULL COMMENT '邀请人手机',
  `relation` varchar(60) NOT NULL COMMENT '用户层级关系，使用-进行隔离',
  `activation` int(1) NOT NULL DEFAULT '0' COMMENT '用户是否激活。0未激活。1已激活',
  `activation_code` varchar(6) DEFAULT NULL COMMENT '用户的激活码',
  `pay_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户需要在这个点支付的时间，到达这个点就锁定',
  `lock_time` timestamp NULL DEFAULT NULL COMMENT '用户被锁定的时间',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `modify_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
--  Table structure for `p2p_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_user_role`;
CREATE TABLE `p2p_user_role` (
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '角色ID',
  `role_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '菜单ID',
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='平台用户角色表';

-- ----------------------------
--  Table structure for `p2p_user_time`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_user_time`;
CREATE TABLE `p2p_user_time` (
  `user_id` bigint(20) NOT NULL,
  `s_notice_time` timestamp NULL DEFAULT NULL,
  `p_notice_time` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户查看通知时间表';

-- ----------------------------
--  Table structure for `p2p_wallet`
-- ----------------------------
DROP TABLE IF EXISTS `p2p_wallet`;
CREATE TABLE `p2p_wallet` (
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `buy_money` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '买入金额金额',
  `sell_money` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '卖出金额金额',
  `integrity` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '诚信奖金',
  `interest` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '利息',
  `cantraded` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '当前可交易金额',
  `freeze` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '钱包冻结金额',
  `wallet` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '钱包实际金额',
  `create_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `modify_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户钱包表';

SET FOREIGN_KEY_CHECKS = 1;
