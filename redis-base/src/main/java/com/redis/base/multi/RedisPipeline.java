package com.redis.base.multi;

import com.redis.base.BaseConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis multi 事务执行，执行一系列命令，每次命令返回QUEUED，表示执行命令已经放入队列等待执行
 * Created by Administrator on 18-3-20.
 */
public class RedisPipeline {
    private static final Logger logger = LoggerFactory.getLogger(RedisPipeline.class);

    static JedisPool jedisPool = null;
    static {
        List<JedisShardInfo> shards = new ArrayList<>();
        jedisPool = new JedisPool("localhost", 6379);
    }

    public void pipelineOpt(){
        Jedis jedis = jedisPool.getResource();

        if(jedis == null) return;
        int i = 0;
        try {
            Transaction t = jedis.multi();
            while (i < 1000){
                t.lpush(BaseConstants.REDIS_LIST_KEY, String.valueOf(i));
                i++;
            }
            List<Response<?>> responses = t.execGetResponse();
            responses.forEach(item-> System.out.println(item.get()));
        } finally {
            if(jedis != null){
                jedis.close();
            }
            if(jedisPool != null){
                jedisPool.close();
            }
        }
    }

    public static void main(String[] args) {
        new RedisPipeline().pipelineOpt();
    }
}
