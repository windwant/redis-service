package com.redis.disconf.util;

import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.Jedis;

/**
 * Created by windwant on 2017/6/8.
 */
public class JedisUtil {

    private static JedisConnectionFactory jf;

    private static String host;

    private static Integer port;

    public static void init(String host, Integer port){
        jf = new JedisConnectionFactory();
        jf.setHostName(host);
        jf.setPort(port);
        JedisUtil.host = host;
        JedisUtil.port = port;
    }

    public static Jedis getJedis(){
        if(jf == null){
            init(JedisUtil.host, JedisUtil.port);
        }
        return jf.getConnection().getNativeConnection();
    }


}
