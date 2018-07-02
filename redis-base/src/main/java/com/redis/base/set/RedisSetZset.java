package com.redis.base.set;

import com.redis.base.BaseConstants;
import com.redis.base.core.PoolRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Redis set
 * Created by Administrator on 18-3-20.
 */
public class RedisSetZset extends PoolRedis {
    private static final Logger logger = LoggerFactory.getLogger(RedisSetZset.class);

    /**
     * string set sinter
     * get tow set inter
     */
    public void sinter(String key1, String key2){
        Jedis jedis = jedisPool.getResource();
        if(jedis == null) return;
        try {
            logger.info("{}: {}, size: {}", key1, jedis.smembers(key1), jedis.scard(key1));
            logger.info("{}: {}, size: {}", key2, jedis.smembers(key2), jedis.scard(key2));
            Set<String> results = jedis.sinter(key1, key2);
            logger.info("set {} {} sinter result: {}", key1, key2, results);
        }catch (Exception e){}
        finally {
            if(jedis != null){
                jedis.close();
            }
        }

    }

    /**
     * string set sunion
     * union two set and return
     */
    public void sunion(String key1, String key2){
        Jedis jedis = jedisPool.getResource();
        if(jedis == null) return;
        try {
            logger.info("{}: {}, size: {}", key1, jedis.smembers(key1), jedis.scard(key1));
            logger.info("{}: {}, size: {}", key2, jedis.smembers(key2), jedis.scard(key2));
            Set<String> results = jedis.sunion(key1, key2);
            logger.info("set {} {} sunion result: {}", key1, key2, results);
        }catch (Exception e){}
        finally {
            if(jedis != null){
                jedis.close();
            }
        }

    }

    /**
     * string set sunionstore
     * union two set to new set
     */
    public void sunionstore(String dest, String key1, String key2){
        Jedis jedis = jedisPool.getResource();
        if(jedis == null) return;
        try {
            logger.info("{}: {}, size: {}", key1, jedis.smembers(key1), jedis.scard(key1));
            logger.info("{}: {}, size: {}", key2, jedis.smembers(key2), jedis.scard(key2));
            long result = jedis.sunionstore(dest, key1, key2);
            logger.info("set {} {} sunionstore dest set size: {}", key1, key2, result);
            logger.info("dest: {}", jedis.smembers(dest));
        }catch (Exception e){}
        finally {
            if(jedis != null){
                jedis.close();
            }
        }

    }

    /**
     * string set srandmember
     * get num random elements from set
     */
    public void srandnumber(String key1, int num){
        Jedis jedis = jedisPool.getResource();
        if(jedis == null) return;
        try {
            logger.info("{}: {}, size: {}", key1, jedis.smembers(key1), jedis.scard(key1));
            List<String> randoms = jedis.srandmember(key1, num);
            logger.info("set {} srandmember result: {}", key1, randoms);
        }catch (Exception e){}
        finally {
            if(jedis != null){
                jedis.close();
            }
        }

    }

    /**
     * string zset zremrange
     * remove range ele from zset
     */
    public void zremrange(String key1, int start, int end){
        Jedis jedis = jedisPool.getResource();
        if(jedis == null) return;
        try {
            logger.info("{}: {}, size: {}", key1, jedis.zrange(key1, 0, -1), jedis.zcard(key1));
            long result = jedis.zremrangeByRank(key1, start, end);
            logger.info("set {} zremrangeByScore result: {}", key1, result == 1?"success":"failed");
            logger.info("{}: {}, size: {}", key1, jedis.zrange(key1, 0, -1), jedis.zcard(key1));
        }catch (Exception e){}
        finally {
            if(jedis != null){
                jedis.close();
            }
        }

    }

    /**
     * string zset zadd
     * update zset ele score
     */
    public void zupdate(String key1, String field, int score){
        Jedis jedis = jedisPool.getResource();
        if(jedis == null) return;
        try {
            logger.info("{}: {}, size: {}", key1, jedis.zrange(key1, 0, -1), jedis.zcard(key1));
            long result = jedis.zadd(key1, score, field);
            logger.info("set {} update result: {}", key1, result == 1?"success":"failed");
            logger.info("{}: {}, size: {}", key1, jedis.zrange(key1, 0, -1), jedis.zcard(key1));
        }catch (Exception e){}
        finally {
            if(jedis != null){
                jedis.close();
            }
        }

    }

    public static void main(String[] args) {
        RedisSetZset redisSet = new RedisSetZset();
        redisSet.sinter("deck", "deck1");
        redisSet.sunion("deck", "deck1");
        redisSet.sunionstore("dest", "deck", "deck1");
        redisSet.srandnumber("deck", ThreadLocalRandom.current().nextInt(1, 3));
        redisSet.zremrange(BaseConstants.REDIS_ZSET_KEY, 0, 1);
        redisSet.destroy();
    }
}
