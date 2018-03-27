package com.redis.base.list;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by Administrator on 18-3-20.
 */
public class RedisListPCServer {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(
                new String[]{"spring/applicationContext.xml",
                        "spring/jedis_sharded_context.xml"});
        ctx.start();
        ctx.registerShutdownHook();
    }
}
