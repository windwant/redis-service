import com.redis.webapp.service.TestRedis;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by aayongche on 2016/2/19.
 */
public class TestR {

    @Test
    public void test() throws InterruptedException {
        ClassPathXmlApplicationContext ct = new ClassPathXmlApplicationContext("classpath:/spring/*.xml");
        TestRedis tr = (TestRedis) ct.getBean("trr");

        for (int i = 0; i < 10; i++) {
            tr.convertAndSend("cctv5", "message: " + String.valueOf(i));
            Thread.sleep(2000);
        }

    }
}
