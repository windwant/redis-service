package com.redis.disconf.service;

import com.redis.disconf.conf.JedisConfig;
import com.redis.disconf.util.JedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.PreDestroy;

/**
 * Created by windwant on 2017/6/8.
 */
@Service
public class RedisService {

    private Jedis jedis;

    @Autowired
    private JedisConfig jedisConfig;

    @PreDestroy
    private void destroy(){
        if(jedis != null){
            jedis.close();
        }
    }


    /**
     * 获取一个值
     *
     * @param key
     * @return
     */
    public String getKey(String key) {
        if (jedis == null) {
            jedis = JedisUtil.getJedis(jedisConfig.getHost(), jedisConfig.getPort());
        }
        return jedis.get(key);
    }

    //设置锁的lua脚本
    private static final String SETNX_EXPIRE_SCRIPT = "if redis.call('setnx', KEYS[1], KEYS[2]) == 1 then\n"
            + "return redis.call('expire', KEYS[1], KEYS[3]);\n" + "end\n" + "return nil;";

    /**
     * 设置锁的lua脚本
     * private static final String SETNX_EXPIRE_SCRIPT = "if redis.call('setnx', KEYS[1], KEYS[2]) == 1 then\n"
     * "return redis.call('expire', KEYS[1], KEYS[3]);\n" + "end\n" + "return nil;";
     *
     * @param key
     * @return
     */
    public boolean setLockKey(String key, String value, Integer seconds) {
        if (jedis == null) {
            jedis = JedisUtil.getJedis(jedisConfig.getHost(), jedisConfig.getPort());
        }
        return jedis.eval(SETNX_EXPIRE_SCRIPT, 3, key, value, String.valueOf(seconds)) != null;
    }
}
