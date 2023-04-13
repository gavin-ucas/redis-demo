package com.gavin.jedis;


import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

/**
 * @author ：gavin
 * @since : 1.0.0
 */

public class JedisDemo {



    public static void main(String[] args) {
        // 创建jedis对象
        Jedis jedis = new Jedis("192.168.71.128",6379);
        jedis.auth("gavin0310");

        // 测试
        String mesg = jedis.ping();
        System.out.println(mesg);

        jedis.close();

    }


    @Test
    public void demo1(){
        Jedis jedis = new Jedis("192.168.71.128",6379);
        jedis.auth("gavin0310");

        // 设置新值
        jedis.set("name", "gavin");
        String name = jedis.get("name");

        System.out.println(name);

        // 一次添加多个键值对
        jedis.mset("age", "18", "sex", "man", "name", "Tom");
        List<String> mget = jedis.mget("age", "sex", "name");
        System.out.println(mget);

        // 遍历所有key
        Set<String> keys = jedis.keys("*");
        for (String key:keys) {
            System.out.println(key);
        }

        jedis.close();

    }

    @Test
    // 操作list
    public void demo2(){
        Jedis jedis = new Jedis("192.168.71.128",6379);
        jedis.auth("gavin0310");

        jedis.lpush("list","1","2","3","4","5");
        // 遍历全部list
        List<String> valus = jedis.lrange("list", 0, -1);
        System.out.println(valus);

        jedis.close();


    }

}
