package combinedmq.spring.boot.consumer;

import com.github.combinedmq.spring.annotation.EnableCombinedMq;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * @author xiaoyu
 */
@EnableCombinedMq({"combinedmq.spring.boot", "combinedmq"})
@SpringBootApplication
public class ConsumerMain {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ConsumerMain.class).web(false).run(args);
    }
}
