package com.redis.pubsub.service.impl;

import com.redis.pubsub.service.SelfDefineMessageDelegate;

import java.io.Serializable;
import java.util.Map;

/**
 * 自定义消息接口实现
 */
public class DefaultSelfDefineMessageDelegate implements SelfDefineMessageDelegate {
    public void handleMessage(String message) {
        System.out.println("handleMessage(String message):" + message);
    }

    public void handleMessage(Map<?, ?> message) {
        System.out.println("handleMessage(Map<?, ?> message):" + message);
    }

    public void handleMessage(byte[] message) {
        System.out.println("CCCCC-----: " + getClass().getSimpleName() + " handleMessage(byte[] message):"
                + new String(message));
    }

    public void handleMessage(Serializable message) {
        System.out.println("CCCCC-----: " + getClass().getSimpleName() + " handleMessage(Serializable message):"
                + message.toString());
    }

    public void handleMessage(Serializable message, String channel) {
        System.out
                .println("CCCCC-----: " + getClass().getSimpleName() + " handleMessage(Serializable message, String channel):"
                        + message.toString() + ", channel:" + channel);
    }
}
