package com.redis.disconf.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by windwant on 2017/6/8.
 */
public class JedisUtil {

    private static JedisPool jp;

    public static Jedis getJedis(String host, Integer port){
        if(jp == null){
            jp = new JedisPool(host, port);
        }
        return jp.getResource();
    }


}
