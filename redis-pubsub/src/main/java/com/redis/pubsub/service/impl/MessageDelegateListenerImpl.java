package com.redis.pubsub.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;

/**
 * Spring 消息接口实现
 */
public class MessageDelegateListenerImpl implements MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(MessageDelegateListenerImpl.class);

    @PostConstruct
    public void postConstruct(){

    }

    public void onMessage(Message message, byte[] bytes) {
        try {
            logger.info("CCCCC-----{}: subscribe message, channel: {}, message: {}",
                    getClass().getSimpleName(), new String(message.getChannel()),new String(message.getBody(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
