package com.redis.base.pipeline;

import com.redis.base.BaseConstants;
import jdk.nashorn.internal.runtime.SharedPropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import redis.clients.jedis.*;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Redis pipeline 异步执行
 * Created by Administrator on 18-3-20.
 */
public class RedisPipeline {
    private static final Logger logger = LoggerFactory.getLogger(RedisPipeline.class);

    static ShardedJedisPool shardedJedisPool = null;
    static {
        List<JedisShardInfo> shards = new ArrayList<>();
        shards.add(new JedisShardInfo("localhost", 6379));
        shardedJedisPool = new ShardedJedisPool(new JedisPoolConfig(), shards);
    }


    /**
     * string set pipe
     * @param kvs
     */
    public void pipeSet(Map<String, String> kvs){
        ShardedJedis jedis = shardedJedisPool.getResource();
        if(jedis == null) return;
        try {
            ShardedJedisPipeline pipeline = jedis.pipelined();
            kvs.entrySet().forEach(entry -> {
                pipeline.set(entry.getKey(), entry.getValue());
                logger.info("pipe set: {}", entry.toString());
            });
            List<Object> results = pipeline.getResults();
            results.stream().forEach(item -> System.out.println(new String((byte[]) item)));
        }catch (Exception e){}
        finally {
            if(jedis != null){
                jedis.close();
            }
        }

    }

    /**
     * list lpush
     * @param key
     * @param values
     */
    public void pipeLpush(String key, List<String> values){
        ShardedJedis jedis = shardedJedisPool.getResource();
        if(jedis == null) return;
        try {
            ShardedJedisPipeline pipeline = jedis.pipelined();
            values.stream().forEach(item -> {
                pipeline.lpush(key, item);
            });
            logger.info("pipe list push key: {} values: {}", key, values.toString());
            List<Object> pipeResult = pipeline.getResults();
            logger.info("final list size: {}", pipeResult.get(pipeResult.size() - 1));
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }



    public void destroy(){
        if(shardedJedisPool != null){
            shardedJedisPool.close();
        }
    }

    /**
     * hash hset hexists hgetAll
     * @param key
     * @param kvs
     */
    public void pipeHset(String key, Map<String, String> kvs){
        ShardedJedis jedis = shardedJedisPool.getResource();
        if(jedis == null) return;
        try {
            ShardedJedisPipeline pipeline = jedis.pipelined();
            kvs.entrySet().forEach(entry -> {
                pipeline.hset(key, entry.getKey(), entry.getValue());
                logger.info("pipe hash mset: {}", entry.toString());
            });
            List<Object> pipeResult = pipeline.getResults();
            long success = pipeResult.stream().filter(item -> Long.parseLong(String.valueOf(item)) > 0).count();
            logger.info("pipe set sadd success: {}", success);
            if(success != kvs.size()){
                Iterator<String> it = kvs.keySet().iterator();
                for (int i = 0; i < pipeResult.size(); i++) {
                    if (Long.parseLong(String.valueOf(pipeResult.get(i))) == 0) {
                        String field = it.next();
                        if(jedis.hexists(key, field)){
                            logger.info("{} {} already exists!", field, kvs.get(field));
                        }else {
                            logger.warn("{} sadd failed!",field);
                        }
                    }else {
                        it.next();
                    }
                };
            }
            logger.info("pipe hash mset success: {}", pipeResult.stream().filter(item -> Long.parseLong(String.valueOf(item)) > 0).count());
            logger.info("{} entry: {}", key, jedis.hgetAll(key));
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    /**
     * set sadd simember smembers
     * @param key
     * @param values
     */
    public void pipeSadd(String key, List<String> values){
        ShardedJedis jedis = shardedJedisPool.getResource();
        if(jedis == null) return;
        try {
            ShardedJedisPipeline pipeline = jedis.pipelined();
            values.stream().forEach(item -> {
                pipeline.sadd(key, item);
            });
            logger.info("pipe set add key: {} values: {}", key, values.toString());
            List<Object> pipeResult = pipeline.getResults();
            long success = pipeResult.stream().filter(item -> Long.parseLong(String.valueOf(item)) > 0).count();
            logger.info("pipe set sadd success: {}", success);
            if(success != values.size()){
                for (int i = 0; i < pipeResult.size(); i++) {
                    if (Long.parseLong(String.valueOf(pipeResult.get(i))) == 0) {
                        if(jedis.sismember(key, values.get(i))){
                            logger.info("{} already exists!", values.get(i));
                        }else {
                            logger.warn("{} sadd failed!", values.get(i));
                        }
                    }
                };
            }
            logger.info("{} memebers: {}", key, jedis.smembers(key));
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    /**
     * zset zadd zrange
     * @param key
     * @param values
     */
    public void pipeZadd(String key, List<String> values){
        ShardedJedis jedis = shardedJedisPool.getResource();
        if(jedis == null) return;
        try {
            ShardedJedisPipeline pipeline = jedis.pipelined();
            values.stream().forEach(item -> {
                pipeline.zadd(key, ThreadLocalRandom.current().nextDouble(100), item);
            });
            logger.info("pipe zset add key: {} values: {}", key, values.toString());
            List<Object> pipeResult = pipeline.getResults();
            long success = pipeResult.stream().filter(item -> Long.parseLong(String.valueOf(item)) > 0).count();
            logger.info("pipe zset sadd success: {}", success);
            logger.info("{} memebers: {}", key, jedis.zrange(key, 0, -1));
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public static void main(String[] args) {
        RedisPipeline pipe = new RedisPipeline();
        pipe.pipeSet(new HashMap() {{
            put("a", "1");
            put("b", "2");
        }});
        pipe.pipeLpush(BaseConstants.REDIS_LIST_KEY, new ArrayList() {{
            add("a");
            add("b");
        }});
        pipe.pipeHset(BaseConstants.REDIS_HASH_KEY, new HashMap() {{
            put("a", "1");
            put("b", "2");
        }});

        pipe.pipeSadd(BaseConstants.REDIS_SET_KEY, new ArrayList() {{
            add("a");
            add("b");
        }});

        pipe.pipeZadd(BaseConstants.REDIS_ZSET_KEY, new ArrayList() {{
            add("a");
            add("b");
        }});
        pipe.destroy();
    }
}
