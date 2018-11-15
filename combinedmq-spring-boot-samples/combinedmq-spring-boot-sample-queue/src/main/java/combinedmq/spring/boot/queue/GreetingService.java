package combinedmq.spring.boot.queue;

import com.github.combinedmq.spring.annotation.Queue;

/**
 * @author xiaoyu
 */
@Queue(name = "x.y.z")
public interface GreetingService {
    void sayHi(String name);
}
