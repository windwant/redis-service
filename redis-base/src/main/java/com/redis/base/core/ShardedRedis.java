package com.redis.base.core;

import com.redis.base.BaseConstants;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 18-6-26.
 */
public class ShardedRedis {
    public static ShardedJedisPool shardedJedisPool;

    static {
        if(shardedJedisPool == null) {
            synchronized (ShardedRedis.class) {
                if (shardedJedisPool == null) {
                    List<JedisShardInfo> shards = new ArrayList<>();
                    shards.add(new JedisShardInfo(BaseConstants.REDIS_DEFAULT_HOST, BaseConstants.REDIS_DEFAULT_PORT));
                    shardedJedisPool = new ShardedJedisPool(new JedisPoolConfig(), shards);
                }
            }
        }
    }

    public void destroy(){
        if(shardedJedisPool != null){
            shardedJedisPool.close();
        }
    }
}
