package com.redis.pubsub.service.impl;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.io.UnsupportedEncodingException;

/**
 * Created by aayongche on 2016/2/19.
 */
public class MessageDelegateListenerImpl implements MessageListener {

    public void onMessage(Message message, byte[] bytes) {
        try {
            System.out.println("channel:" + new String(message.getChannel())
                    + ",message:" + new String(message.getBody(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
