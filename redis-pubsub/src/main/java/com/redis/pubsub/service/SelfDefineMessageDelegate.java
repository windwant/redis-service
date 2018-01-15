package com.redis.pubsub.service;

import java.io.Serializable;
import java.util.Map;

/**
 * 自定义消息接口
 */
public interface SelfDefineMessageDelegate {
    void handleMessage(String message);

    void handleMessage(Map<?, ?> message);

    void handleMessage(byte[] message);

    void handleMessage(Serializable message);

    // pass the channel/pattern as well
    void handleMessage(Serializable message, String channel);
}
