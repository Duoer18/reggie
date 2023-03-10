# 外卖前后台系统

[toc] 



### 一、功能概述

##### 1. 后台系统

店家进行店铺管理的系统，可实现

- 员工的管理
- 菜品、套餐、分类的管理
- 订单的管理

##### 2. 前台系统

用户进行下单点餐的系统，可实现

- 短信登录并注册
- 下单点餐
- 地址管理

### 二、分支概述

##### 1. master

实现全部描述功能，并且前后台分别进行登录验证（原项目要求的目标为混合认证，不区分前后台登录状态，但显然不合理），前后台的控制层也经过拆分。采用redis缓存了菜品和套餐的信息。采用sharding jdbc配置分库信息，实现读写分离（需要自行完成mysql的主从复制的配置，并将application.yml中spring.shardingsphere.enabled项设为true，同时修改sharding的数据源信息，包括master和多个slave，本处只采用一个slave，可自行配置多个slave）。服务器部署在windows系统上（若要部署在其他系统上，只需要修改application.yml中reggie.upload-path项的值，其意义为保存用户上传的文件的文件夹路径）。

##### 2. cache-dev（其功能已被master实现，现已经弃用）

实现全部描述功能，且采用redis缓存了菜品和套餐的信息，但未拆分前后台。服务器部署在windows系统上。

##### 3. master_slave_db（其功能已被master实现，现已经弃用）

实现全部描述功能以及cache-dev分支功能，并采用sharding jdbc配置分库信息，实现读写分离，但未拆分前后台。需要配置至少两个mysql方可实现。服务器部署在linux系统上。

##### 4. master_slave_db_win（其功能已被master实现，现已经弃用）

同master_slave_db，但服务器部署在windows系统上。