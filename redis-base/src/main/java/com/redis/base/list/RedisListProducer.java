package com.redis.base.list;
import com.redis.base.BaseConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by Administrator on 18-3-20.
 */
@Component
public class RedisListProducer implements Runnable {

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    public void run(){
        ShardedJedis jedis = shardedJedisPool.getResource();
        if(jedis == null) return;
        int i = 0;
        try {
            while (i < Integer.MAX_VALUE){
                jedis.lpush(BaseConstants.REDIS_LIST_KEY, String.valueOf(i));
                System.out.println("produce list ele: " + i);
                Thread.sleep(ThreadLocalRandom.current().nextInt(3)*1000);
                i++;
            }
        } catch (InterruptedException e) {
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    @PostConstruct
    public void produce(){
        new Thread(this).start();
    }
}
