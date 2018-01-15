package com.redis.pubsub;

import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.UnsupportedEncodingException;

/**
 */
public class TestServer extends TestCase {

    @Autowired
    RedisOpt opt;
    @Test
    public void testJedisBlpop() throws InterruptedException, UnsupportedEncodingException {
        opt.blockPull("test");
    }

    @Test
    public void testJedisSetValue() throws InterruptedException, UnsupportedEncodingException {
        opt.setJedisValue("test", "test");
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        ClassPathXmlApplicationContext ct = new ClassPathXmlApplicationContext("classpath:/spring/*.xml");
        opt = (RedisOpt) ct.getBean("trr");
    }

    @Test
    public void testPub(){
        opt.jedisPublish("test");
    }

    @Test
    public void testSub(){
        opt.jedisSubscribe("test");
    }
}
