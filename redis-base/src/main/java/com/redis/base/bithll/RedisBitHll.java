package com.redis.base.bithll;

import com.redis.base.BaseConstants;
import com.redis.base.core.PoolRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.Jedis;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Redis bit visit statistic
 * Created by Administrator on 18-3-20.
 */
public class RedisBitHll extends PoolRedis implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(RedisBitHll.class);

    private int duration;

    public void setbit(String key, long offset, int isOrNot){
        Jedis jedis = jedisPool.getResource();
        if(jedis == null) return;
        try {
            jedis.setbit(key, offset, String.valueOf(isOrNot));
        }finally {
            jedis.close();
        }
    }

    /**
     * hll pfadd
     * @param key
     * @param value
     */
    public void pfadd(String key, String value){
        Jedis jedis = jedisPool.getResource();
        if(jedis == null) return;
        try {
            jedis.pfadd(key, value);
        }finally {
            jedis.close();
        }
    }

    /**
     * count distinct ele
     * 因为 HyperLogLog 只会根据输入元素来计算基数，而不会储存输入元素本身，所以 HyperLogLog 不能像集合那样，返回输入的各个元素。
     * @param key
     */
    public long pfcount(String key){
        Jedis jedis = jedisPool.getResource();
        if(jedis == null) return 0;
        try {
            return jedis.pfcount(key);
        }finally {
            jedis.close();
        }
    }

    /**
     * count 1
     * @param key
     * @return
     */
    public long bitcount(String key){
        Jedis jedis = jedisPool.getResource();
        if(jedis == null) return 0;
        try {
            return jedis.bitcount(key);
        }finally {
            jedis.close();
        }
    }

    /**
     * index fisrt 1
     * @param key
     * @return
     */
    public long bitpos(String key){
        Jedis jedis = jedisPool.getResource();
        if(jedis == null) return 0;
        try {
            return jedis.bitpos(key, true);
        }finally {
            jedis.close();
        }
    }

    /**
     * operate two bit
     * @param key1
     * @param key2
     * @param op  AND, OR, XOR, NOT;
     * @return
     */
    public long bitop(String key1, String key2, BitOP op){
        Jedis jedis = jedisPool.getResource();
        if(jedis == null) return 0;
        try {
            return jedis.bitop(op, key1, key2);
        }finally {
            jedis.close();
        }
    }

    @Override
    public void run() {
        //clear bit offset
        bitop(BaseConstants.REDIS_BIT_KEY, "SS", BitOP.AND);
        long seconds = 0;
        for (int i = 0; i < duration ; i++) {
            //simulate visitor come or not 0 1
            int visit = ThreadLocalRandom.current().nextInt(2);
            setbit(BaseConstants.REDIS_BIT_KEY, i, visit);
            if(visit == 1) {
                //simulate visit user random same
                String user = BaseConstants.REDIS_VISIT_USER + ThreadLocalRandom.current().nextInt(10);
                pfadd(BaseConstants.REDIS_HLL_KEY, user);
                logger.info("time eclipse {}s, user: {}, current visit: {}", seconds, user, bitcount(BaseConstants.REDIS_BIT_KEY));
            }else {
                logger.info("time eclipse {}s", seconds);
            }
            seconds++;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        logger.info("time eclipse seconds: {}, total visit: {}, user count: {}",
                seconds, bitcount(BaseConstants.REDIS_BIT_KEY), pfcount(BaseConstants.REDIS_HLL_KEY));
        logger.info("fisrt visit at: {}", bitpos(BaseConstants.REDIS_BIT_KEY));

        destroy();
    }

    RedisBitHll(){

    }

    RedisBitHll(int duration){
        this.duration = duration;
    }

    public static void main(String[] args) {
        RedisBitHll bit = new RedisBitHll(20); //visit stastic in 20 seconds
        new Thread(bit).start();
    }
}
