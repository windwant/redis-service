package com.redis.pubsub;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by aayongche on 2016/2/19.
 */
@Component("trr")
public class RedisOpt {


    // inject the template as ListOperations
    // can also inject as Value, Set, ZSet, and HashOperations
    @Resource(name="redisTemplate")
    public RedisTemplate redisTemplate;

    public void add(String userId, String name) {
        redisTemplate.opsForValue().set(userId, name);
        System.out.println(redisTemplate.opsForValue().get(userId));
    }
    public Object get(String userId) {
        return redisTemplate.opsForValue().get(userId);
    }

    public void check(String userId) {
        System.out.println(redisTemplate.hasKey(userId));
    }
    public void del(String userId) {
        redisTemplate.delete(userId);
    }
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

    public void blockPull(){
    }
}
