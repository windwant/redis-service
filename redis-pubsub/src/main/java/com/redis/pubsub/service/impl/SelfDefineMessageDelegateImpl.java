package com.redis.pubsub.service.impl;

import com.redis.pubsub.service.SelfDefineMessageDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;

/**
 * 自定义消息接口实现
 */
public class SelfDefineMessageDelegateImpl implements SelfDefineMessageDelegate {
    private static final Logger logger = LoggerFactory.getLogger(SelfDefineMessageDelegateImpl.class);

    public void handleMessage(String message) {
        logger.info("handleMessage(String message): {}", message);
    }

    public void handleMessage(Map<?, ?> message) {
        logger.info("handleMessage(Map<?, ?> message): {}", message);
    }

    public void handleMessage(byte[] message) {
        logger.info("CCCCC-----: " + getClass().getSimpleName() + " handleMessage(byte[] message):"
                + new String(message));
    }

    public void handleMessage(Serializable message) {
        logger.info("CCCCC-----: {} handleMessage(Serializable message):", getClass().getSimpleName(), message.toString());
    }

    public void handleMessage(Serializable message, String channel) {
        logger.info("CCCCC-----: {}, handleMessage(Serializable message, String channel): {}, channel: {}",
                getClass().getSimpleName(), message.toString(), channel);
    }
}
