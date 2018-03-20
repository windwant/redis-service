package com.redis.disconf.service;

import com.redis.disconf.conf.JedisConfig;
import com.redis.disconf.util.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by windwant on 2017/6/8.
 */
@Service
public class RedisService {

    @Autowired
    private JedisConfig jedisConfig;

    @PostConstruct
    private void init(){
        JedisUtil.initJedisPool(jedisConfig.getHost(), jedisConfig.getPort());
    }

    @PreDestroy
    private void destroy(){
        JedisUtil.destory();
    }

    /**
     * 获取一个值
     *
     * @param key
     * @return
     */
    public String getKey(String key) {
        Jedis jedis = null;
        String value;
        try {
            jedis = JedisUtil.getJedis();
            value = jedis.get(key);
        }finally {
           JedisUtil.returnResource(jedis);
        }
        return value;
    }
}
