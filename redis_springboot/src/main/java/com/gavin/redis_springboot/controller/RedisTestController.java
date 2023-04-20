package com.gavin.redis_springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

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

    /**
     * redis实现分布式锁
     * 1. 通过setnx 设置锁
     * 2. 为了避免服务异常导致锁无法释放的问题 设置过期时间
     */
    @GetMapping("testLock")
    public void testLock(){

        String threadId = String.valueOf(Thread.currentThread().getId());
        // 获取锁  5秒后过期
        Boolean lock = redisTemplate.opsForValue().setIfAbsent("lock", threadId, 5, TimeUnit.SECONDS);

        // 加锁成功后 执行操作（此处为加1计数操作）
        if(lock){
            optionAdd();
            // 释放锁
            String lockId = (String) redisTemplate.opsForValue().get("lock");

            if(threadId.equals(lockId)){
                redisTemplate.delete("lock");
            }

        }else{
            // 获取锁失败，等待重试
            try{
                Thread.sleep(100);
                testLock();
            }catch (InterruptedException e){
                e.printStackTrace();
            }

        }
    }

    /**
     * 使用lua脚本来保证redis操作的原子性
     */

    @GetMapping("testLockByLua")
    public void testLockByLua(){

        String threadId = String.valueOf(Thread.currentThread().getId());

        // lua脚本使用的锁
        String luaLockKey = "luaLock:"+threadId;

        // 获取锁  5秒后过期
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(luaLockKey, threadId, 5, TimeUnit.SECONDS);



        // 加锁成功后 执行操作（此处为加1计数操作）
        if(lock){
            optionAdd();

            String script = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
            DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
            redisScript.setScriptText(script);
            // 设置返回值类型
            redisScript.setResultType(Long.class);

            redisTemplate.execute(redisScript, Arrays.asList(luaLockKey), threadId);

        }else{
            // 获取锁失败，等待重试
            try{
                Thread.sleep(100);
                testLockByLua();
            }catch (InterruptedException e){
                e.printStackTrace();
            }

        }
    }

    // 业务操作 （可以根据实际修改）
    public void optionAdd(){
        Object countVal = redisTemplate.opsForValue().get("count");
        // 为空 返回， 不为空转换为int 进行加1 操作
        if(countVal == null){
            redisTemplate.opsForValue().set("count",0);
            return;
        }
        redisTemplate.opsForValue().increment("count");

    }


}
