package com.redis.disconf.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by windwant on 2017/6/8.
 */
public class JedisUtil {

    private static volatile JedisPool jp;

    /**
     * 初始化redis pool
     * @param host
     * @param port
     */
    public static void initJedisPool(String host, Integer port){
        if(jp == null){
            synchronized (JedisUtil.class) {
                jp = new JedisPool(host, port);
            }
        }
    }

    /**
     * 获取Jedis实例
     * @return
     */
    public static Jedis getJedis(){
        if(jp != null){
            return jp.getResource();
        }
        return null;
    }

    /**
     * 注销pool
     */
    public static void destory(){
        if(jp != null) {
            synchronized (JedisUtil.class) {
                if(jp != null){
                    jp.close();
                    jp = null;
                }
            }
        }
    }

    /**
     * 释放
     * @param jedis
     */
    public static void returnResource(Jedis jedis){
        if(jedis != null){
            jedis.close();
        }
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
    public static boolean tryLock(String key, String value, Integer seconds) {
        Jedis jedis = getJedis();
        if (jedis == null) return false;
        boolean lock = false;
        try{
            lock = jedis.eval(SETNX_EXPIRE_SCRIPT, 3, key, value, String.valueOf(seconds)) != null;
        }finally {
            returnResource(jedis);
        }
        return lock;
    }

    /**
     * 释放锁
     * @param key
     * @return
     */
    public static boolean unLock(String key) {
        Jedis jedis = getJedis();
        if (jedis == null) return false;
        boolean unlock = false;
        try{
            unlock = jedis.del(key) != null;
        }finally {
            returnResource(jedis);
        }
        return unlock;
    }

}
