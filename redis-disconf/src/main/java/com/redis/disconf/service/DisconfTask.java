package com.redis.disconf.service;

import com.redis.disconf.conf.JedisConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by windwant on 2017/6/8.
 */
@Service
public class DisconfTask {

    @Autowired
    private RedisService redisService;

    @Autowired
    private JedisConfig jedisConfig;

    private static final String REDIS_KEY = "distribute_key";
    private static final String REDIS_VALUE = "distribute_value";

    /**
     *
     */
    public int run() {
        try {
            generateLock();
            while (true) {
                Thread.sleep(5000);
                String value = redisService.getKey(REDIS_KEY);
                System.out.println("redis( " + jedisConfig.getHost() + ","
                        + jedisConfig.getPort() + ")  get key: " + REDIS_KEY + " value: " + value);
                if(value == null) generateLock();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private boolean generateLock(){
        boolean result = redisService.setLockKey(REDIS_KEY, REDIS_VALUE, 10);
        System.out.println("set lock key: " + REDIS_KEY + ", value: " + REDIS_VALUE + ", seconds: " + 10 + ", result: " + result);
        return result;
    }
}
