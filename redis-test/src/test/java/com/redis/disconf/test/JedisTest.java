package com.redis.disconf.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;

import java.util.HashSet;
import java.util.Set;

/**
 * Unit test for simple JedisOpt.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/applicationContext.xml",
        "classpath:spring/jedis_sharded_context.xml",
        "classpath:spring/jedis_sentinel_context.xml",
        "classpath:spring/jedis_sentinel_spring.xml",
        "classpath:spring/jedis_pool_context.xml"})
public class JedisTest
{

    @Autowired
    JedisOpt jedisOpt;

    @Test
    public void testShardedJedis(){
        ShardedJedis shardedJedis = jedisOpt.getShardedRedisClient();
//        shardedJedis.set("name", "lilei");
        System.out.println(shardedJedis.get("name"));
        shardedJedis.append("name", "&hanmeimei");
        System.out.println(shardedJedis.get("name"));
    }

    @Test
    public void testJedis(){
        Jedis jedis = jedisOpt.getRedisClient();
        jedis.set("name", "lilei");
        System.out.println(jedis.get("name"));
        jedis.append("name", "&hanmeimei");
        System.out.println(jedis.get("name"));
    }

    @Test
    public void testSentinelJedis(){
        Jedis jedis = jedisOpt.getRedisClient();
        jedis.set("name", "lilei");
        System.out.println(jedis.get("name"));
        jedis.append("name", "&hanmeimei");
        System.out.println(jedis.get("name"));
    }

    @Test
    public void test() {
        Set<String> sentinels = new HashSet<String>();
        sentinels.add("127.0.0.1:6377");

        /**
         * masterName 分片的名称
         * sentinels Redis Sentinel 服务地址列表
         */
        JedisSentinelPool poolA = new JedisSentinelPool("mymaster", sentinels);
        //获取Jedis主服务器客户端实例
        Jedis jedisA = poolA.getResource();
        jedisA.set("key", "abc");


        System.out.println("jedisA key:"+jedisA.get("key"));
        //输出结果
        //jedisA key:abc
    }

    @Test
    public void testSentinel(){
        StringRedisTemplate st = jedisOpt.getSentinelClientOne();
        st.opsForValue().set("word", "hello");
        System.out.println(st.opsForValue().get("word"));
    }
}
