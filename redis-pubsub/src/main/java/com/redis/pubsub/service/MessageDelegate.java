package com.redis.pubsub.service;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by aayongche on 2016/2/19.
 */
public interface MessageDelegate {
    public void handleMessage(String message);

    public void handleMessage(Map<?, ?> message);

    public void handleMessage(byte[] message);

    public void handleMessage(Serializable message);

    // pass the channel/pattern as well
    public void handleMessage(Serializable message, String channel);
}
