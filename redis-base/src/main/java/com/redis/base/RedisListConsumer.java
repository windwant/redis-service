package com.redis.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by Administrator on 18-3-20.
 */
@Component
public class RedisListConsumer implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RedisListConsumer.class);

    @Autowired
    JedisOpt jedisOpt;

    public void run(){
        ShardedJedis jedis = jedisOpt.getShardedRedisClient();
        while (true){
            List<String> redisList = jedis.brpop(5, BaseConstants.REDIS_LIST_KEY);
            if(redisList != null && !redisList.isEmpty()) {
                System.out.println("consume list ele: " + redisList.get(1));
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
    }

    @PostConstruct
    public void consume(){
        new Thread(this).start();
    }
}
