package com.redis.pubsub;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;


import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * Redis 基本操作
 */
@Component("trr")
public class RedisOpt {


    // inject the template as ListOperations
    // can also inject as Value, Set, ZSet, and HashOperations
    @Resource(name="redisTemplate")
    public RedisTemplate redisTemplate;

    /**
     * 添加 k v
     * @param userId
     * @param name
     */
    public void add(String userId, String name) {
        redisTemplate.opsForValue().set(userId, name);
        System.out.println(redisTemplate.opsForValue().get(userId));
    }

    /**
     * 获取 k v
     * @param userId
     * @return
     */
    public Object get(String userId) {
        return redisTemplate.opsForValue().get(userId);
    }

    /**
     * 检验存在
     * @param userId
     */
    public void check(String userId) {
        System.out.println(redisTemplate.hasKey(userId));
    }

    /**
     * 删除
     * @param userId
     */
    public void del(String userId) {
        redisTemplate.delete(userId);
    }

    /**
     * 过期
     * @param userId
     * @param name
     * @param time
     */
    public void addExpire(String userId, String name,  int time) {
        redisTemplate.opsForValue().set(userId, name);
        redisTemplate.expire(userId, time, TimeUnit.SECONDS);
    }
    public void flushAll() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    public void multiSet(Map userId) {
        redisTemplate.opsForValue().multiSet(userId);
    }

    public void multiGet(List userId) {
        List s = redisTemplate.opsForValue().multiGet(userId);
        System.out.println(s.toString());
    }

    public void opsListLPush(String userId, String name) {
        redisTemplate.opsForList().leftPush(userId, name);
    }

    public List opsListRange(String userId, int start, int end) {
        return redisTemplate.opsForList().range(userId, start, end);
    }

    public Object opsListIndex(String userId, int index) {
        return redisTemplate.opsForList().index(userId, index);
    }

    public void opsListRemove(String userId, int index, String value) {
        redisTemplate.opsForList().remove(userId, index, value);
    }

    public void opsListSet(String userId, Collection s) {
        redisTemplate.opsForList().leftPushAll(userId, s);
    }

    public void convertAndSend(String channel, Object message){
        redisTemplate.convertAndSend(channel, message);
    }

    JedisPool jedisPool;

    @Value("${redis.master.host}")
    public String redisHost;

    @Value("${redis.master.port}")
    public Integer redisport;

    public JedisConnectionFactory getJedisConnectionFactory() {
        if(jedisConnectionFactory == null){
            jedisConnectionFactory = new JedisConnectionFactory();
            //TODO
        }
        return jedisConnectionFactory;
    }

    public void setJedisConnectionFactory(JedisConnectionFactory jedisConnectionFactory) {
        this.jedisConnectionFactory = jedisConnectionFactory;
    }

    public JedisConnectionFactory jedisConnectionFactory;


    public JedisPool getJedisPool() {
        if(jedisPool == null){
            jedisPool = new JedisPool(new JedisPoolConfig(), redisHost, redisport);
        }
        return jedisPool;
    }

    /**
     * jedis blpop 阻塞取redis中队列头部值 超时3s
     */
    public void blockPull(String queue){
        Jedis jedis = getJedisPool().getResource();
        while (true) {
            System.out.println(jedis.blpop(3000, queue));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * jedis 写入redis队列值
     */
    public void setJedisValue(String queue, String value){
        Jedis jedis = getJedisPool().getResource();
        while (true) {
//            String value = String.valueOf(ThreadLocalRandom.current().nextInt(100));
            jedis.lpush(queue, value);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(queue + " value: " + value);
        }
    }

    /**
     * 发布主题
     * @param topic
     */
    public void jedisPublish(String topic){
        Jedis jedis = getJedisPool().getResource();
        while (true) {
            String value = String.valueOf(ThreadLocalRandom.current().nextInt(100));
            jedis.publish(topic, value);
            System.out.println("publish value: " + value);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 订阅主题
     */
    public void jedisSubscribe(String topic){
        Jedis jedis = getJedisPool().getResource();
        jedis.subscribe(new JedisPubSub() {
            @Override
            public void onMessage(String channel, String message) {
                super.onMessage(channel, message);
                System.out.println("subscribe channel: " + channel + ", message: " + message);
            }
        }, topic);
    }
}
