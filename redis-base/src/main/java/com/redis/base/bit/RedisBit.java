package com.redis.base.bit;

import com.redis.base.BaseConstants;
import com.redis.base.core.PRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.BitOP;
import redis.clients.jedis.Jedis;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Redis bit visit statistic
 * Created by Administrator on 18-3-20.
 */
public class RedisBit extends PRedis implements Runnable{
    private static final Logger logger = LoggerFactory.getLogger(RedisBit.class);

    private Jedis jedis;

    private int duration;

    public void setbit(String key, long offset, int isOrNot){
        Jedis jedis = jedisPool.getResource();

        if(jedis == null) return;
        try {
            jedis.setbit(key, offset, String.valueOf(isOrNot));
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    /**
     * count 1
     * @param key
     * @return
     */
    public long bitcount(String key){
        Jedis jedis = getJedis();
        if(jedis == null) return 0;
        return jedis.bitcount(key);
    }

    /**
     * index fisrt 1
     * @param key
     * @return
     */
    public long bitpos(String key){
        Jedis jedis = getJedis();
        if(jedis == null) return 0;
        return jedis.bitpos(key, true);
    }

    /**
     * operate two bit
     * @param key1
     * @param key2
     * @param op  AND, OR, XOR, NOT;
     * @return
     */
    public long bitop(String key1, String key2, BitOP op){
        Jedis jedis = getJedis();
        if(jedis == null) return 0;
        return jedis.bitop(op, key1, key2);
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
                logger.info("time eclipse {}s, current visit: {}", seconds, bitcount(BaseConstants.REDIS_BIT_KEY));
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
        logger.info("time eclipse seconds: {}, total visit: {}", seconds, bitcount(BaseConstants.REDIS_BIT_KEY));
        logger.info("fisrt visit at: {}", bitpos(BaseConstants.REDIS_BIT_KEY));

        destroy();
    }

    RedisBit(){

    }

    RedisBit(int duration){
        this.duration = duration;
    }

    public Jedis getJedis() {
        if(jedis == null){
            synchronized (RedisBit.class){
                if(jedis == null){
                    jedis = jedisPool.getResource();
                    return jedis;
                }
            }
        }
        return jedis;
    }

    @Override
    public void destroy() {
        super.destroy();
        synchronized (RedisBit.class) {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static void main(String[] args) {
        RedisBit bit = new RedisBit(20); //visit stastic in 20 seconds
        new Thread(bit).start();
    }
}
