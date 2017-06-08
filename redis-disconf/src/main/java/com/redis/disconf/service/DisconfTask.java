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

    private static final String REDIS_KEY = "disconf_key";

    /**
     *
     */
    public int run() {
        try {
            while (true) {
                Thread.sleep(5000);
                System.out.println("redis( " + jedisConfig.getHost() + ","
                        + jedisConfig.getPort() + ")  get key: " + REDIS_KEY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
