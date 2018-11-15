package combinedmq.spring.boot.consumer;

import com.github.combinedmq.spring.annotation.EnableCombinedMq;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author xiaoyu
 */
@EnableCombinedMq({"combinedmq.spring.boot", "combinedmq"})
@SpringBootApplication
public class ConsumerMain {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerMain.class, args);
    }
}
