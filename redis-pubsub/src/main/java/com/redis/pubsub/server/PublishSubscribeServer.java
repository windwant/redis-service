package com.redis.pubsub.server;

import com.redis.pubsub.RedisOpt;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by windwant on 2017/2/4.
 */
public class PublishSubscribeServer {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext ct = new ClassPathXmlApplicationContext("classpath:/spring/*.xml");
        RedisOpt tr = (RedisOpt) ct.getBean("trr");

        String channel = "springdata";
        String message = "010_京u8264".trim();
        for (int i = 0; i < 10; i++) {
            tr.convertAndSend(channel, message);
            System.out.println("publish message channel: " + channel + ", message: " + message);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
