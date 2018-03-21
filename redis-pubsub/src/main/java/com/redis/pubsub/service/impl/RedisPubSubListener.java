package com.redis.pubsub.service.impl;

import redis.clients.jedis.BinaryJedisPubSub;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 18-3-20.
 */
public class RedisPubSubListener extends BinaryJedisPubSub {
    @Override
    public void onMessage(byte[] channel, byte[] message) {
        try {
            System.out.println("RedisPubSubListener onMessage---channel: " + new String(channel, "utf-8") + ", message: " + new String(message, "utf-8"));
        } catch (UnsupportedEncodingException e) {

        }
    }
}
