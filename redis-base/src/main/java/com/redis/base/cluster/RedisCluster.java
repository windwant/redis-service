package com.redis.base.cluster;

import com.redis.base.core.ClusterRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Redis multi 事务执行，执行一系列命令，每次命令返回QUEUED，表示执行命令已经放入队列等待执行
 * Created by Administrator on 18-3-20.
 */
public class RedisCluster extends ClusterRedis {
    private static final Logger logger = LoggerFactory.getLogger(RedisCluster.class);

    public void set(String key, String value){
        if(jedisCluster == null) return;
        String result = jedisCluster.set(key, value);
        logger.info("cluster jedis set key: {} value: {} result: {}", key, value, result);
    }

    public void get(String key){
        if(jedisCluster == null) return;
        String result = jedisCluster.get(key);
        logger.info("cluster jedis get key: {} result: {}", key, result);
    }

    public static void main(String[] args) {
        RedisCluster cluster= new RedisCluster();
        cluster.set("test", "test" + ThreadLocalRandom.current().nextInt(155));
        cluster.get("test");
        cluster.destroy();
    }
}
