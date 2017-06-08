package com.redis.disconf.pubsub.service;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by aayongche on 2016/2/19.
 */
public interface SelfDefineMessageDelegate {
    void handleMessage(String message);

    void handleMessage(Map<?, ?> message);

    void handleMessage(byte[] message);

    void handleMessage(Serializable message);

    // pass the channel/pattern as well
    void handleMessage(Serializable message, String channel);
}
