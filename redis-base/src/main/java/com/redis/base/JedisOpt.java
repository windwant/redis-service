package com.redis.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

/**
 * Hello world!
 *
 */
@Component("jedisOpt")
public class JedisOpt
{

    @Autowired
    private ShardedJedisPool shardedJedisPool;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private JedisSentinelPool jedisSentinelPool;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public ShardedJedis getShardedRedisClient() {
        ShardedJedis shardJedis = null;
        try {
            shardJedis = shardedJedisPool.getResource();
            return shardJedis;
        } catch (Exception e) {
            if (null != shardJedis)
                shardJedis.close();
        }
        return null;
    }

    public void returnShardedResource(ShardedJedis shardedJedis) {
        shardedJedis.close();
    }

    public void returnShardedResource(ShardedJedis shardedJedis, boolean broken) {
        shardedJedis.close();
    }

    public Jedis getRedisClient() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis;
        } catch (Exception e) {
            if (null != jedis)
                jedis.close();
        }
        return null;
    }

    public void returnResource(Jedis jedis) {
        jedis.close();
    }

    public void returnResource(Jedis jedis, boolean broken) {
        jedis.close();
    }

    public Jedis getRedisSentinelClient() {
        Jedis jedis = null;
        try {
            jedis = jedisSentinelPool.getResource();
            return jedis;
        } catch (Exception e) {
            if (null != jedis)
                jedis.close();
        }
        return null;
    }

    public void returnSentinelResource(Jedis jedis) {
        jedis.close();
    }

    public void returnSentinelResource(Jedis jedis, boolean broken) {
        jedis.close();
    }

    public StringRedisTemplate getSentinelClientOne(){
        return stringRedisTemplate;
    }
}
