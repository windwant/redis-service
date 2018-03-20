package com.redis.base;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 18-3-20.
 */
public class BaseServer {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String[]{"spring/applicationContext.xml",
                        "spring/jedis_sharded_context.xml"});
        ctx.start();
        ctx.registerShutdownHook();
    }
}
