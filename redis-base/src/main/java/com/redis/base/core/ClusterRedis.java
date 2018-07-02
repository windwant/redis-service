package com.redis.base.core;

import com.redis.base.BaseConstants;
import redis.clients.jedis.*;

import java.io.IOException;

/**
 * Created by Administrator on 18-6-26.
 */
public class ClusterRedis {
    public static JedisCluster jedisCluster;

    static {
        if(jedisCluster == null) {
            synchronized (ClusterRedis.class) {
                if (jedisCluster == null) {
                    jedisCluster = new JedisCluster(BaseConstants.CLUSTER_HOST_AND_PORTS);
                }
            }
        }
    }

    public void destroy(){
        if(jedisCluster != null){
            try {
                jedisCluster.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
