import com.redis.webapp.service.TestRedis;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * Created by aayongche on 2016/2/19.
 */
public class TestPS {

    @Test
    public void test() throws InterruptedException {
        ClassPathXmlApplicationContext ct = new ClassPathXmlApplicationContext("classpath:/spring/*.xml");
        TestRedis tr = (TestRedis) ct.getBean("trr");
        RedisMessageListenerContainer rmlc;
        // ctx.getBean(RedisMessageListenerContainer.class);
        rmlc = (RedisMessageListenerContainer) ct.getBean("redisContainer");
        while (true) {
            if (rmlc.isRunning()) {
                System.out
                        .println("RedisMessageListenerContainer is running..");
            }
            Thread.sleep(5000);
        }

    }
}
