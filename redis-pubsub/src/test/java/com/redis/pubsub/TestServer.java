package com.redis.pubsub;

import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.UnsupportedEncodingException;

/**
 * Created by aayongche on 2016/2/19.
 */
public class TestServer extends TestCase {

    @Autowired
    RedisOpt opt;
    @Test
    public void testJedisBlpop() throws InterruptedException, UnsupportedEncodingException {
        opt.blockPull();
    }

    @Test
    public void testJedisSetValue() throws InterruptedException, UnsupportedEncodingException {
        opt.setJedisValue();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ClassPathXmlApplicationContext ct = new ClassPathXmlApplicationContext("classpath:/spring/*.xml");
        opt = (RedisOpt) ct.getBean("trr");
    }
}
