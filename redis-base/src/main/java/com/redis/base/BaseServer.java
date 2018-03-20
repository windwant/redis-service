package com.redis.base;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 18-3-20.
 */
public class BaseServer {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("spring/*.xml");
        ctx.start();
        ctx.registerShutdownHook();
    }
}
