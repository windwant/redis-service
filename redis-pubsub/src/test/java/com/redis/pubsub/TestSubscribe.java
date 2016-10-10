package com.redis.pubsub;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by aayongche on 2016/2/19.
 */
public class TestSubscribe {

    @Test
    public void test() throws InterruptedException {
        ClassPathXmlApplicationContext ct = new ClassPathXmlApplicationContext("classpath:/spring/*.xml");
        RedisOpt tr = (RedisOpt) ct.getBean("trr");

        for (int i = 0; i < 10; i++) {
            tr.convertAndSend("spring", "message: " + String.valueOf(i));
            Thread.sleep(2000);
        }

    }
}
