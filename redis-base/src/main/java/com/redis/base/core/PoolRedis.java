package com.redis.base.core;

import com.redis.base.BaseConstants;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 18-6-26.
 */
public class PoolRedis {
    public static JedisPool jedisPool; 

    static {
        if(jedisPool == null) {
            synchronized (PoolRedis.class) {
                if (jedisPool == null) {
                    jedisPool = new JedisPool(BaseConstants.REDIS_DEFAULT_HOST, BaseConstants.REDIS_DEFAULT_PORT);
                }
            }
        }
    }

    public void destroy(){
        if(jedisPool != null){
            jedisPool.close();
        }
    }
}
