package com.redis.pubsub;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.UnsupportedEncodingException;

/**
 * Created by aayongche on 2016/2/19.
 */
public class TestSubscribe {

    @Test
    public void test() throws InterruptedException, UnsupportedEncodingException {
        ClassPathXmlApplicationContext ct = new ClassPathXmlApplicationContext("classpath:/spring/*.xml");
        RedisOpt tr = (RedisOpt) ct.getBean("trr");

        for (int i = 0; i < 10; i++) {
            tr.convertAndSend("CLBA", "010_京u8264".trim());
            Thread.sleep(2000);
        }

    }
}
