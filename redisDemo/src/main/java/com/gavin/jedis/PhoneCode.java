package com.gavin.jedis;

import redis.clients.jedis.Jedis;

import java.util.Random;

/**
 * @author ：gavin
 * @since : 1.0.0
 */

public class PhoneCode {
    public static void main(String[] args) {
        String phone = "15467894256";
        // 前端接收到的code
        String code = "783290";

        String s = setExpire(phone);

        if(verifyCode("15467894256", s)){
            System.out.println("验证成功");
        }else{
            System.out.println("验证失败");
        }
    }
    // 1.生成code
    public static String getCode(){
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            int val = random.nextInt(10);
            sb.append(val);
        }
        return sb.toString();
    }

    // 2. 存手机号和 code

    /**
     * 为指定的手机号设置验证码 两分钟过期  且 每个手机号每天限制验证发送次数为五次
     * @param phone
     * @return 生成的验证码
     */
    public static String setExpire(String phone){
        Jedis jedis = new Jedis("192.168.71.128",6379);
        jedis.auth("gavin0310");

        String phoneKey = "phone_" + phone + "_key";
        String phoneCount= "phone_" + phone + "_count";
        String code = getCode();

        String count = jedis.get(phoneCount);
        // 每天不超过5次
        if(count == null){
            jedis.setex(phoneCount, 24*60*60, "1");

        }else if(Integer.parseInt(count) <= 4){
            jedis.incr(phoneCount);
        }else {
            // 超过限制
            System.out.println("超出了每天限制次数");
            return code;
        }

        // code 两分钟过期
        jedis.setex(phoneKey, 2*60, code);
        jedis.close();

        return code;
    }

    //3. 验证code
    public static boolean verifyCode(String phone, String code){
        Jedis jedis = new Jedis("192.168.71.128",6379);
        jedis.auth("gavin0310");


        String phoneKey = "phone_" + phone + "_key";

        return jedis.get(phoneKey).equals(code);

    }
}
