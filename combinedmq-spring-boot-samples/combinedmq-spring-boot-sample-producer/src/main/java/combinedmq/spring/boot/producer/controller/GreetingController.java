package combinedmq.spring.boot.producer.controller;

import com.github.combinedmq.spring.annotation.Producer;
import combinedmq.spring.boot.queue.GreetingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiaoyu
 */
@RestController
public class GreetingController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Producer(delayMillis = 5000)
    private GreetingService greetingService;

    @RequestMapping("sayHi/{name}")
    public String sayHi(@PathVariable String name) {
        logger.info("发送");
        greetingService.sayHi(name);
        logger.info("发送消息: " + name);
        return "发送成功";
    }
}
