package com.redis.pubsub.server;

import com.redis.pubsub.service.impl.RedisPubSubListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * pubsub server.
 */
public class PubSubWithoutSpringServer {
    static JedisPool jedisPool = null;
    static String channel = "springdata";
    static{
        jedisPool = new JedisPool("localhost", 6379);
    }

    private static final Logger logger = LoggerFactory.getLogger(PubSubWithoutSpringServer.class);

    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                jedisPool.getResource().subscribe(new RedisPubSubListener(), channel.getBytes());
            }
        }).start();

        String message = "010_京u8264".trim();
        Jedis jedis = jedisPool.getResource();
        try {
            while (!Thread.currentThread().isInterrupted()) {

                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    jedis.publish(channel, message);
                    logger.info("PPPPP+++++: publish message, channel: {}, message: {}", channel, message);
                    Thread.sleep(2000);
                }
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            jedis.close();//重连
            jedisPool.close();
        }
    }

}
