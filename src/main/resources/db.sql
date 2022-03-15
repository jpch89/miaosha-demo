CREATE TABLE `t_user`
(
    `id`              BIGINT(20)   NOT NULL COMMENT '用户ID，手机号码',
    `nickname`        VARCHAR(255) NOT NULL,
    `password`        VARCHAR(32)  DEFAULT NULL COMMENT 'MD5(MD5(pass明文+固定salt)+salt)',
    `salt`            VARCHAR(10)  DEFAULT NULL,
    `head`            VARCHAR(128) DEFAULT NULL COMMENT '头像',
    `register_date`   datetime     DEFAULT NULL COMMENT '注册时间',
    `last_login_date` datetime     DEFAULT NULL COMMENT '最后一次登录时间',
    `login_count`     INT(11)      DEFAULT '0' COMMENT '登录次数',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 COMMENT = '用户';

CREATE TABLE `t_goods`
(
    `id`           BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '商品id',
    `goods_name`   VARCHAR(16)    DEFAULT NULL COMMENT '商品名称',
    `goods_title`  VARCHAR(64)    DEFAULT NULL COMMENT '商品标题',
    `goods_img`    VARCHAR(64)    DEFAULT NULL COMMENT '商品图片',
    `goods_detail` LONGTEXT COMMENT '商品描述',
    `goods_price`  DECIMAL(10, 2) DEFAULT '0.00' COMMENT '商品价格',
    `goods_stock`  INT(11)        DEFAULT '0' COMMENT '商品库存,-1表示没有限制',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4 COMMENT = '商品';

CREATE TABLE `t_order`
(
    `id`               BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
    `user_id`          BIGINT(20)     DEFAULT NULL COMMENT '用户ID',
    `goods_id`         BIGINT(20)     DEFAULT NULL COMMENT '商品ID',
    `delivery_addr_id` BIGINT(20)     DEFAULT NULL COMMENT '收获地址ID',
    `goods_name`       VARCHAR(16)    DEFAULT NULL COMMENT '冗余过来的商品名称',
    `goods_count`      INT(20)        DEFAULT '0' COMMENT '商品数量',
    `goods_price`      DECIMAL(10, 2) DEFAULT '0.00' COMMENT '商品单价',
    `order_channel`    TINYINT(4)     DEFAULT '0' COMMENT '1 pc, 2 android, 3 ios',
    `status`           TINYINT(4)     DEFAULT '0' COMMENT '订单状态，0新建未支付，1已支付，2已发货，3已收货，4已退货，5已完成',
    `create_date`      datetime       DEFAULT NULL COMMENT '订单创建时间',
    `pay_date`         datetime       DEFAULT NULL COMMENT '支付时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 12
  DEFAULT CHARSET = utf8mb4 COMMENT = '订单';

CREATE TABLE `t_miaosha_goods`
(
    `id`            BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `goods_id`      BIGINT(20)     DEFAULT NULL COMMENT '商品ID',
    `miaosha_price` DECIMAL(10, 2) DEFAULT '0.00' COMMENT '秒杀价',
    `stock_count`   INT(10)        DEFAULT NULL COMMENT '库存数量',
    `start_date`    datetime       DEFAULT NULL COMMENT '秒杀开始时间',
    `end_date`      datetime       DEFAULT NULL COMMENT '秒杀结束时间',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4 COMMENT = '秒杀商品';

CREATE TABLE `t_miaosha_order`
(
    `id`       BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '秒杀订单ID',
    `user_id`  BIGINT(20) DEFAULT NULL COMMENT '用户ID',
    `order_id` BIGINT(20) DEFAULT NULL COMMENT '订单ID',
    `goods_id` BIGINT(20) DEFAULT NULL COMMENT '商品ID',
    PRIMARY KEY (`id`)
) ENGINE = INNODB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4 COMMENT = '秒杀订单';

INSERT INTO `t_goods`
VALUES (1, 'IPHONE 12', 'IPHONE 12 64GB', '/img/iphone12.png', 'IPHONE 12 64GB', '6299.00', 100),
       (2,
        'IPHONE 12 PRO',
        'IHPONE 12 PRO 128GB',
        '/img/iphone12pro.png',
        'IPHONE 12 PRO 128GB',
        '9299.00',
        100);

-- 注意：秒杀商品的库存一定要低于商品的库存
INSERT INTO `t_miaosha_goods`
VALUES (1, 1, '629', 10, '2022-11-11 08:00:00', '2022-11-11 09:00:00'),
       (2, 2, '929', 10, '2022-11-11 08:00:00', '2022-11-11 09:00:00');

-- t_miaosha_order 添加唯一索引 miaosha_uid_gid
-- 解决同一个用户秒杀多商品
ALTER TABLE `t_miaosha_order`
    ADD INDEX `miaosha_uid_gid` (user_id, goods_id) USING BTREE COMMENT '用户id+商品id的唯一索引，解决同一个用户秒杀多商品';