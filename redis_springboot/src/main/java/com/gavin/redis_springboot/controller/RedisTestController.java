package com.gavin.redis_springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ：gavin
 * @since : 1.0.0
 */

@RestController
@RequestMapping("redisTest")
public class RedisTestController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping
    public String testRedis(){
        redisTemplate.opsForValue().set("name","gavin");

        return (String)redisTemplate.opsForValue().get("name");
    }
}
