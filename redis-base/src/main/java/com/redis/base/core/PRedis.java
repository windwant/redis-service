package com.redis.base.core;

import com.redis.base.BaseConstants;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 18-6-26.
 */
public class PRedis {
    public static JedisPool jedisPool; 

    public static ShardedJedisPool shardedJedisPool;

    static {
        if(jedisPool == null) {
            synchronized (PRedis.class) {
                if (jedisPool == null) {
                    jedisPool = new JedisPool(BaseConstants.REDIS_DEFAULT_HOST, BaseConstants.REDIS_DEFAULT_PORT);
                }
            }
        }

        if(shardedJedisPool == null) {
            synchronized (PRedis.class) {
                if (shardedJedisPool == null) {
                    List<JedisShardInfo> shards = new ArrayList<>();
                    shards.add(new JedisShardInfo(BaseConstants.REDIS_DEFAULT_HOST, BaseConstants.REDIS_DEFAULT_PORT));
                    shardedJedisPool = new ShardedJedisPool(new JedisPoolConfig(), shards);
                }
            }
        }
    }

    public void destroy(){
        if(jedisPool != null){
            jedisPool.close();
        }
        if(shardedJedisPool != null){
            shardedJedisPool.close();
        }
    }
}
