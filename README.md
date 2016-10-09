# redies
结合Spring 简单 订阅/发布系统

使用Spring Data Redis操作Redis

1. Redis的Pub/Sub命令

Redis的订阅和发布服务有如下图6个命令，下面分别对每个命令做简单说明。

pubsub.png
publish: 向指定的channel(频道)发送message(消息)

subscribe:订阅指定channel，可以一次订阅多个

psubscribe:订阅指定pattern(模式，具有频道名的模式匹配)的频道

unsubscribe:取消订阅channel,可以一次取消多个订阅

punsubscribe:取消指定pattern的订阅

pubsub: 是一个查看订阅与发布系统状态的内省命令,它由数个不同格式的子命令组成(具体可参见：

http://redis.io/commands/pubsub)

在SDR（Spring Data Redis）中频道对应Topic类，Top类是一个接口有Channel和Pattern两个实现类，分别是指定名称的频道和模式匹配的频道。关于订阅信息由Subscription接口定义。

2.Redis消息监听容器声明和消息监听器注册

在SDR中可以用两种方式来实现消息监听容器的声明，一种是通过Redis的命名空间，一种是定义Bean。

这里主要涉及到RedisMessageListenerContainer，MessageListenerAdapter，MessageListener几个类。

2.1使用Redis命名空间的方式配置

<bean id="mdpListener" class="secondriver.spring.redis.MyMessageListener" />
  <bean id="mdelegateListener" class="secondriver.spring.redis.DefaultMessageDelegate" />

  <redis:listener-container connection-factory="jedisConnectionFactory">
    <redis:listener ref="mdpListener" topic="spring*" />
    <redis:listener ref="mdelegateListener" method="handleMessage"
      topic="cctv5 cctv6 nbtv hello*" />
  </redis:listener-container>
说明:

定义topic可以是具体的channel的名字也可以是Pattern，多个频道（主题）用空格隔开即可。

这里定义了两个Listner，MyMessageListener实现了MessageaListener接口,DefaultMessageDelegate实现了MessageDelegate接口。

MyMessageListener:

package secondriver.spring.redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class MyMessageListener implements MessageListener {

  @Override
  public void onMessage(Message message, byte[] pattern) {
    System.out.println("channel:" + new String(message.getChannel())
        + ",message:" + new String(message.getBody()));
  }
}
MessageDelegate接口：

package secondriver.spring.redis;

import java.io.Serializable;
import java.util.Map;

public interface MessageDelegate {

  public void handleMessage(String message);

  public void handleMessage(Map<?, ?> message);

  public void handleMessage(byte[] message);

  public void handleMessage(Serializable message);

  // pass the channel/pattern as well
  public void handleMessage(Serializable message, String channel);
}
DefaultMessageDelegate类型：

package secondriver.spring.redis;

import java.io.Serializable;
import java.util.Map;

public class DefaultMessageDelegate implements MessageDelegate {

  @Override
  public void handleMessage(String message) {
    System.out.println("handleMessage(String message):" + message);
  }

  @Override
  public void handleMessage(Map<?, ?> message) {
    System.out.println("handleMessage(Map<?, ?> message):" + message);
  }

  @Override
  public void handleMessage(byte[] message) {
    System.out.println("handleMessage(byte[] message):"
        + new String(message));
  }

  @Override
  public void handleMessage(Serializable message) {
    System.out.println("handleMessage(Serializable message):"
        + message.toString());
  }

  @Override
  public void handleMessage(Serializable message, String channel) {
    System.out
        .println("handleMessage(Serializable message, String channel):"
            + message.toString() + ", channel:" + channel);
  }
}
这种定义消息监听的方式不依赖于Redis，其被设计为一个message-driven POJOs (MDPs)，MessageListenerAdapter实现了MessageListener接口，它将会把Message委托给目标监听器（Target Listener）DefalutMessageDelegate的方法，对Message参数的适当转换，然后通过反射来调用方法。

2.2定义Bean的方式配置

<!-- Bean Configuration -->
  <bean id="messageListener"
    class="org.springframework.data.redis.listener.adapter.MessageListenerAdapter">
    <constructor-arg>
      <bean class="secondriver.spring.redis.MyMessageListener" />
    </constructor-arg>
  </bean>

  <bean id="redisContainer"
    class="org.springframework.data.redis.listener.RedisMessageListenerContainer">
    <property name="connectionFactory" ref="jedisConnectionFactory" />
    <property name="messageListeners">
      <map>
        <entry key-ref="messageListener">
          <list>
            <bean class="org.springframework.data.redis.listener.ChannelTopic">
              <constructor-arg value="springtv" />
            </bean>
            <bean class="org.springframework.data.redis.listener.PatternTopic">
              <constructor-arg value="hello*" />
            </bean>
            <bean class="org.springframework.data.redis.listener.PatternTopic">
              <constructor-arg value="tv*" />
            </bean>
          </list>
        </entry>
      </map>
    </property>
  </bean>
3.模拟消息的发布和接收

// 简单测试RedisMessageListener
  @Ignore
  @Test
  public void test10() throws InterruptedException {
    RedisMessageListenerContainer rmlc;
    // ctx.getBean(RedisMessageListenerContainer.class);
    rmlc = (RedisMessageListenerContainer) ctx.getBean("redisContainer");
    while (true) {
      if (rmlc.isRunning()) {
        System.out
            .println("RedisMessageListenerContainer is running..");
      }
      Thread.sleep(5000);
    }
  }
由于这里是测试，通过一个死循环来保持程序一直运行，然后向Redis服务的指定频道发布消息，则符合订阅的频道的消息将被客户端连接接收到，并且MyMessageListener对象中的onMessage方法被调用。

下图是模拟过程：

pubsubmessage.png
发布了四条消息，分别是spring,springtv，hello， nono四个频道，根据定义bean这种配置中的Topic名称，只有springtv,hello符合模式匹配，并且也同样收到了这两条消息。

除了这种模拟外，实际在应用开发中是通过JedisConnection的Pub/Sub相关的方法来向Redis服务发布消息的或者RedisTemplate的convertAndSend方法。

4.最后

SDR正处于发展阶段的项目，更多特性阅读源代码，一步步挖掘。