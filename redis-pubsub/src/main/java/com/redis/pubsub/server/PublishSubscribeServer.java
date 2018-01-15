package com.redis.pubsub.server;

import com.redis.pubsub.RedisOpt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * pubsub server.
 */
public class PublishSubscribeServer {

    private static final Logger logger = LoggerFactory.getLogger(PublishSubscribeServer.class);

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ct = new ClassPathXmlApplicationContext("classpath:/spring/*.xml");
        JedisConnectionFactory jf = (JedisConnectionFactory) ct.getBean("jedisConnFactory");
        RedisOpt tr = (RedisOpt) ct.getBean("trr");

        String channel = "springdata";
        String message = "010_京u8264".trim();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    tr.convertAndSend(channel, message);
                    logger.info("PPPPP+++++: publish message, channel: {}, message: {}", channel, message);
                    Thread.sleep(2000);
                }
                break;
            } catch (Exception e) {
                e.printStackTrace();
                jf.destroy();//重连
                jf.afterPropertiesSet();
            }
        }
    }

}
