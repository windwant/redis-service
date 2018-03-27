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
import java.util.ArrayList;
import java.util.List;

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

    public void pipelineOpt(){
        ShardedJedis jedis = shardedJedisPool.getResource();
        if(jedis == null) return;
        int i = 0;
        try {
            ShardedJedisPipeline pipeline = jedis.pipelined();
            while (i < 1000){
                pipeline.lpush(BaseConstants.REDIS_LIST_KEY, String.valueOf(i));
                i++;
            }
            List<Object> pipeResult = pipeline.getResults();
            pipeResult.forEach(item->System.out.println("produce list ele: " + item));
        } finally {
            if(jedis != null){
                jedis.close();
            }
            if(shardedJedisPool != null){
                shardedJedisPool.close();
            }
        }
    }

    public static void main(String[] args) {
        new RedisPipeline().pipelineOpt();
    }
}
