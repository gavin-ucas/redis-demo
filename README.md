# redis_demo

#### 介绍
redis 基础到高级特性


## redisDemo

### 1. Jedis 连接 redis 进行常见操作

### 2. 实现了手机验证码的简易功能

常见错误
 
 redis 配置文件 
    
    bind 127.0.0.1 需要注释掉
    daemonize yes  ##redis为后台启动
    requirepass ****  ## 设置redis 密码
 

最后需要关闭redis所在服务器的防火墙 
 

## redis_springboot

### springBoot整合redis

1. 引入相关依赖。
1. 定义了key生成规则，通过redisTemplate来操作redis
1. 实现了简单redis操作
1. 使用redis set nx ex 实现了分布式锁
1. 锁标识使用当前进程号作为表示，防止锁误删
1. 使用lua脚本保证操作的原子性


##### 分布式锁测试工具 ab

安装
```shell
sudo apt install apache2-utils
```
测试命令

```shell
ab -n 1000 -c 100 http://192.168.139.107:8080/redisTest/testLock
```

lua脚本保证原子性
```shell
ab -n 1000 -c 100 http://192.168.139.107:8080/redisTest/testLockByLua
```


