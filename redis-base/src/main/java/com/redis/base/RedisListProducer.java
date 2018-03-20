package com.redis.base;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.PostConstruct;

/**
 * Created by Administrator on 18-3-20.
 */
@Component
public class RedisListProducer implements Runnable {

    @Autowired
    JedisOpt jedisOpt;

    public void run(){
        ShardedJedis jedis = jedisOpt.getShardedRedisClient();
        int i = 0;
        try {
            while (i < Integer.MAX_VALUE){
                jedis.lpush(BaseConstants.REDIS_LIST_KEY, String.valueOf(i));
                System.out.println("produce list ele: " + i);
                try {
                    if(i > 10) {
                        Thread.sleep(3000);
                    }
                } catch (InterruptedException e) {

                }
                i++;
            }
        }finally {
            jedisOpt.returnShardedResource(jedis);
        }
    }

    @PostConstruct
    public void produce(){
        new Thread(this).start();
    }
}
