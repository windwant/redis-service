package com.redis.disconf.service;

import com.redis.disconf.conf.JedisConfig;
import com.redis.disconf.util.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * Created by windwant on 2017/6/8.
 */
@Service
public class RedisService {

    private Jedis jedis;

    @Autowired
    private JedisConfig jedisConfig;

    private void destroy(){
        if(jedis != null){
            jedis.close();
        }
    }

    public void afterPropertiesSet(){
        jedis = JedisUtil.getJedis();
    }

    /**
     * 获取一个值
     *
     * @param key
     * @return
     */
    public String getKey(String key) {
        if (jedis != null) {
            return jedis.get(key);
        }

        return null;
    }
}
