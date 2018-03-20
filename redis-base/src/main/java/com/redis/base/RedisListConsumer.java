package com.redis.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by Administrator on 18-3-20.
 */
@Component
public class RedisListConsumer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RedisListConsumer.class);

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    public void run(){
        ShardedJedis jedis = shardedJedisPool.getResource();
        if(jedis == null) return;
        try {
            Thread.sleep(10000);
            while (true) {
                System.out.println("consumer wait for list ele ... ");
                List<String> redisList = jedis.brpop(5, BaseConstants.REDIS_LIST_KEY);
                if (redisList != null && !redisList.isEmpty()) {
                    System.out.println("consume list ele: " + redisList.get(1));
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {

        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    @PostConstruct
    public void consume(){
        new Thread(this).start();
    }
}
