package com.redis.disconf.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class RedisServer
{
    public static void main( String[] args ) {
        ClassPathXmlApplicationContext ct = new ClassPathXmlApplicationContext("classpath:/*.xml");
        ct.registerShutdownHook();
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
