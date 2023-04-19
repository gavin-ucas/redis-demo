# springBoot整合redis



1.  引入相关依赖。
2.  定义了key生成规则，通过redisTemplate来操作redis
3.  实现了简单redis操作
4.  使用redis set nx ex 实现了分布式锁
5.  锁标识使用当前进程号作为表示，防止锁误删
6.  使用lua脚本保证操作的原子性

### 分布式锁测试工具 ab

##### 安装

```shell
sudo apt install apache2-utils
```



##### 测试命令

```shell
ab -n 1000 -c 100 http://192.168.139.107:8080/redisTest/testLock
```

##### lua脚本保证原子性

```shell
ab -n 1000 -c 100 http://192.168.139.107:8080/redisTest/testLockByLua
```

