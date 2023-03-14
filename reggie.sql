create table address_book
(
    id            bigint                       not null comment '主键'
        primary key,
    user_id       bigint                       not null comment '用户id',
    consignee     varchar(50)                  not null comment '收货人',
    sex           tinyint                      not null comment '性别 0 女 1 男',
    phone         varchar(11)                  not null comment '手机号',
    province_code varchar(12) charset utf8mb4  null comment '省级区划编号',
    province_name varchar(32) charset utf8mb4  null comment '省级名称',
    city_code     varchar(12) charset utf8mb4  null comment '市级区划编号',
    city_name     varchar(32) charset utf8mb4  null comment '市级名称',
    district_code varchar(12) charset utf8mb4  null comment '区级区划编号',
    district_name varchar(32) charset utf8mb4  null comment '区级名称',
    detail        varchar(200) charset utf8mb4 null comment '详细地址',
    label         varchar(100) charset utf8mb4 null comment '标签',
    is_default    tinyint(1) default 0         not null comment '默认 0 否 1是',
    create_time   datetime                     not null comment '创建时间',
    update_time   datetime                     not null comment '更新时间',
    create_user   bigint                       not null comment '创建人',
    update_user   bigint                       not null comment '修改人',
    is_deleted    int        default 0         not null comment '是否删除'
)
    comment '地址管理' collate = utf8mb3_bin;

create table category
(
    id          bigint        not null comment '主键'
        primary key,
    type        int           null comment '类型   1 菜品分类 2 套餐分类',
    name        varchar(64)   not null comment '分类名称',
    sort        int default 0 not null comment '顺序',
    create_time datetime      not null comment '创建时间',
    update_time datetime      not null comment '更新时间',
    create_user bigint        not null comment '创建人',
    update_user bigint        not null comment '修改人',
    constraint idx_category_name
        unique (name)
)
    comment '菜品及套餐分类' collate = utf8mb3_bin;

create table dish
(
    id          bigint         not null comment '主键'
        primary key,
    name        varchar(64)    not null comment '菜品名称',
    category_id bigint         not null comment '菜品分类id',
    price       decimal(10, 2) null comment '菜品价格',
    code        varchar(64)    not null comment '商品码',
    image       varchar(200)   not null comment '图片',
    description varchar(400)   null comment '描述信息',
    status      int default 1  not null comment '0 停售 1 起售',
    sort        int default 0  not null comment '顺序',
    create_time datetime       not null comment '创建时间',
    update_time datetime       not null comment '更新时间',
    create_user bigint         not null comment '创建人',
    update_user bigint         not null comment '修改人',
    is_deleted  int default 0  not null comment '是否删除',
    constraint idx_dish_name
        unique (name)
)
    comment '菜品管理' collate = utf8mb3_bin;

create table dish_flavor
(
    id          bigint        not null comment '主键'
        primary key,
    dish_id     bigint        not null comment '菜品',
    name        varchar(64)   not null comment '口味名称',
    value       varchar(500)  null comment '口味数据list',
    create_time datetime      not null comment '创建时间',
    update_time datetime      not null comment '更新时间',
    create_user bigint        not null comment '创建人',
    update_user bigint        not null comment '修改人',
    is_deleted  int default 0 not null comment '是否删除'
)
    comment '菜品口味关系表' collate = utf8mb3_bin;

create table employee
(
    id          bigint        not null comment '主键'
        primary key,
    name        varchar(32)   not null comment '姓名',
    username    varchar(32)   not null comment '用户名',
    password    varchar(64)   not null comment '密码',
    phone       varchar(11)   not null comment '手机号',
    sex         varchar(2)    not null comment '性别',
    id_number   varchar(18)   not null comment '身份证号',
    status      int default 1 not null comment '状态 0:禁用，1:正常',
    create_time datetime      not null comment '创建时间',
    update_time datetime      not null comment '更新时间',
    create_user bigint        not null comment '创建人',
    update_user bigint        not null comment '修改人',
    constraint idx_username
        unique (username)
)
    comment '员工信息' collate = utf8mb3_bin;

insert into employee values (1, '管理员', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 13812312312, 1,
                             110101199001010047, 1, '2021-05-06 17:20:07', '2021-05-10 02:24:09', 1, 1);

create table order_detail
(
    id          bigint         not null comment '主键'
        primary key,
    name        varchar(50)    null comment '名字',
    image       varchar(100)   null comment '图片',
    order_id    bigint         not null comment '订单id',
    dish_id     bigint         null comment '菜品id',
    setmeal_id  bigint         null comment '套餐id',
    dish_flavor varchar(50)    null comment '口味',
    number      int default 1  not null comment '数量',
    amount      decimal(10, 2) not null comment '金额'
)
    comment '订单明细表' collate = utf8mb3_bin;

create table orders
(
    id              bigint         not null comment '主键'
        primary key,
    number          varchar(50)    null comment '订单号',
    status          int default 1  not null comment '订单状态 1待付款，2待派送，3已派送，4已完成，5已取消',
    user_id         bigint         not null comment '下单用户',
    address_book_id bigint         not null comment '地址id',
    order_time      datetime       not null comment '下单时间',
    checkout_time   datetime       not null comment '结账时间',
    pay_method      int default 1  not null comment '支付方式 1微信,2支付宝',
    amount          decimal(10, 2) not null comment '实收金额',
    remark          varchar(100)   null comment '备注',
    phone           varchar(255)   null,
    address         varchar(255)   null,
    user_name       varchar(255)   null,
    consignee       varchar(255)   null
)
    comment '订单表' collate = utf8mb3_bin;

create table setmeal
(
    id          bigint         not null comment '主键'
        primary key,
    category_id bigint         not null comment '菜品分类id',
    name        varchar(64)    not null comment '套餐名称',
    price       decimal(10, 2) not null comment '套餐价格',
    status      int default 1  null comment '状态 0:停用 1:启用',
    code        varchar(32)    null comment '编码',
    description varchar(512)   null comment '描述信息',
    image       varchar(255)   null comment '图片',
    create_time datetime       not null comment '创建时间',
    update_time datetime       not null comment '更新时间',
    create_user bigint         not null comment '创建人',
    update_user bigint         not null comment '修改人',
    is_deleted  int default 0  not null comment '是否删除',
    constraint idx_setmeal_name
        unique (name)
)
    comment '套餐' collate = utf8mb3_bin;

create table setmeal_dish
(
    id          bigint         not null comment '主键'
        primary key,
    setmeal_id  varchar(32)    not null comment '套餐id ',
    dish_id     varchar(32)    not null comment '菜品id',
    name        varchar(32)    null comment '菜品名称 （冗余字段）',
    price       decimal(10, 2) null comment '菜品原价（冗余字段）',
    copies      int            not null comment '份数',
    sort        int default 0  not null comment '排序',
    create_time datetime       not null comment '创建时间',
    update_time datetime       not null comment '更新时间',
    create_user bigint         not null comment '创建人',
    update_user bigint         not null comment '修改人',
    is_deleted  int default 0  not null comment '是否删除'
)
    comment '套餐菜品关系' collate = utf8mb3_bin;

create table shopping_cart
(
    id          bigint         not null comment '主键'
        primary key,
    name        varchar(50)    null comment '名称',
    image       varchar(100)   null comment '图片',
    user_id     bigint         not null comment '主键',
    dish_id     bigint         null comment '菜品id',
    setmeal_id  bigint         null comment '套餐id',
    dish_flavor varchar(50)    null comment '口味',
    number      int default 1  not null comment '数量',
    amount      decimal(10, 2) not null comment '金额',
    create_time datetime       null comment '创建时间',
    constraint shopping_cart_user_id_dish_id_uindex
        unique (user_id, dish_id) comment '同一用户的同一款菜品不能重复',
    constraint shopping_cart_user_id_setmeal_id_uindex
        unique (user_id, setmeal_id) comment '同一用户的同一款套餐不能重复'
)
    comment '购物车' collate = utf8mb3_bin;

create table user
(
    id        bigint        not null comment '主键'
        primary key,
    name      varchar(50)   null comment '姓名',
    phone     varchar(100)  not null comment '手机号',
    sex       varchar(2)    null comment '性别',
    id_number varchar(18)   null comment '身份证号',
    avatar    varchar(500)  null comment '头像',
    status    int default 1 null comment '状态 0:禁用，1:正常'
)
    comment '用户信息' collate = utf8mb3_bin;

