package combinedmq.spring.boot.consumer.service;

import com.github.combinedmq.spring.annotation.Consumer;
import combinedmq.spring.boot.queue.GreetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @author xiaoyu
 */
@Service
@Consumer
public class GreetingServiceImpl implements GreetingService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void sayHi(String name) {
        logger.info("接收消息: {}", name);
    }
}
