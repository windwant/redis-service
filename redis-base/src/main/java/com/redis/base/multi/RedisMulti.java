package com.redis.base.multi;

import com.redis.base.BaseConstants;
import com.redis.base.core.PRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Redis multi 事务执行，执行一系列命令，每次命令返回QUEUED，表示执行命令已经放入队列等待执行
 * Created by Administrator on 18-3-20.
 */
public class RedisMulti extends PRedis{
    private static final Logger logger = LoggerFactory.getLogger(RedisMulti.class);

    public void multiLPush(String key, List<String> values){
        Jedis jedis = jedisPool.getResource();

        if(jedis == null) return;
        try {
            Transaction t = jedis.multi();
            values.stream().forEach(value->t.lpush(key, value));
            List<Response<?>> responses = t.execGetResponse();
            logger.info("multi opt result list {} size {}", key, jedis.llen(key));
        } finally {
            if(jedis != null){
                jedis.close();
            }
        }
    }

    public static void main(String[] args) {
        RedisMulti multi = new RedisMulti();
        multi.multiLPush(BaseConstants.REDIS_LIST_KEY, new ArrayList() {{
            add("a");
            add("b");
        }});
        multi.destroy();
    }
}
