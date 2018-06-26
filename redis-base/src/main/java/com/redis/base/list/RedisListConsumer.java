package com.redis.base.list;

import com.redis.base.BaseConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
                printList(jedis);
                System.out.println("consumer begin rpop " + BaseConstants.REDIS_LIST_KEY + " list ele ... ");
                //brpop get ele from list tail
                List<String> redisList = jedis.brpop(5, BaseConstants.REDIS_LIST_KEY);
                if (redisList != null && !redisList.isEmpty()) {
                    System.out.println("consume " + BaseConstants.REDIS_LIST_KEY + "  list ele: " + redisList.get(1));
                }
//                Thread.sleep(ThreadLocalRandom.current().nextInt(3)*1000);
            }
        } catch (InterruptedException e) {

        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    private void printList(ShardedJedis jedis){
        long size = jedis.llen(BaseConstants.REDIS_LIST_KEY);
        System.out.println(BaseConstants.REDIS_LIST_KEY + " list size: " + size);
        if(size > 0) {
            System.out.println(BaseConstants.REDIS_LIST_KEY + " eles: ");
            int from = 0;
            int pageSize = 10;
            long page = size%pageSize == 0? size/pageSize: (size/pageSize + 1);
            for (int i = 0; i < page; i++) {
                System.out.println(jedis.lrange(BaseConstants.REDIS_LIST_KEY, from, from + pageSize - 1));
                from += pageSize;
            }
        }
    }

    @PostConstruct
    public void consume(){
        new Thread(this).start();
    }
}
