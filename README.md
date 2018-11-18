CombinedMq Spring Boot
========================
[![Build Status](https://travis-ci.com/combinedmq/combinedmq-spring-boot.svg?branch=master)](https://travis-ci.com/combinedmq/combinedmq-spring-boot)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.combinedmq/combinedmq-spring-boot.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.combinedmq%22%20AND%20a:%22combinedmq-spring-boot%22)

[中文版文档](https://github.com/combinedmq/combinedmq-spring-boot/blob/master/README_zh.md)

CombinedMq Spring Boot can help you integrate CombinedMq into Spring Boot. The application.yml configuration and annotations make it easy to integrate.
### Overview
Using annotations to use Combined Mq involves four annotations:
1. @Queue - Annotated on an interface type
2. @Producer - Annotated on the member variable of the class, which should be an interface marked by @Queue to send the message
3. @Consumer - Annotation on an implementation class of an interface annotated by @Queue to receive messages
4. @EnableCombinedMq - Specify basePackages to enable scanning of @Queue annotations
## Steps for usage
### Step 1: Maven dependency

```xml
<dependency>
  <groupId>com.github.combinedmq</groupId>
  <artifactId>combinedmq-spring-boot-starter</artifactId>
  <version>1.0.2</version>
</dependency>
```
### Step 2: Create an interface

All method return types of this interface can only be void type:
```java
@Queue(name = "x.y.z")
public interface GreetingService {
    void sayHi(String name);
}
```
### Step 3: Implement consumer function
- application.yml configuration

```yaml
combinedmq:
  rabbitmq:
    host: 10.1.7.22
    port: 5672
    username: xiaoyu
    password: xiaoyu
    virtualHost: /
    consumer-listener:
      concurrency: 5
```

- Implement the GreetingService interface

```java
@Slf4j
@Service
@Consumer
public class GreetingServiceImpl implements GreetingService {
 
    @Override
    public void sayHi(String name) {
        log.info("接收消息: {}", name);
    }
}
```

- Start the Spring Boot Consumer program

```java
@EnableCombinedMq({"combinedmq"})
@SpringBootApplication
public class ConsumerMain {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ConsumerMain.class).web(false).run(args);
    }
}
```
### Step 4: Implement producer function
- application.yml configuration

```yaml
combinedmq:
  rabbitmq:
    host: 10.1.7.22
    port: 5672
    username: xiaoyu
    password: xiaoyu
    virtualHost: /
    producer-pool:
      maxTotal: 100
      maxIdle: 20
      minIdle: 10
      maxWaitMillis: 30000
      minEvictableIdleTimeMillis: 60000
      timeBetweenEvictionRunsMillis: 30000
      testOnBorrow: false
      testOnReturn: false
      testWhileIdle: true
```
- Injecting Greeting Service with @Producer

```java
@Slf4j
@RestController
public class GreetingController {
 
    @Producer(delayMillis = 5000)
    private GreetingService greetingService;

    @RequestMapping("sayHi/{name}")
    public String sayHi(@PathVariable String name) {
        log.info("准备发送消息");
        greetingService.sayHi(name);
        log.info("消息发送成功: {}", name);
        return "发送成功";
    }
}
```
- Start the Spring Boot Producer program

```java
@EnableCombinedMq({"combinedmq"})
@SpringBootApplication
public class ProducerMain {
    public static void main(String[] args) {
        SpringApplication.run(ProducerMain.class, args);
    }
}
```
## Project modules
CombinedMq Spring Boot consists of the following submodules:

- [combinedmq-spring-boot-autoconfigure](https://github.com/combinedmq/combinedmq-spring-boot/tree/master/combinedmq-spring-boot-autoconfigure)

- [combinedmq-spring-boot-samples](https://github.com/combinedmq/combinedmq-spring-boot/tree/master/combinedmq-spring-boot-samples)

- [combinedmq-spring-boot-starter](https://github.com/combinedmq/combinedmq-spring-boot/tree/master/combinedmq-spring-boot-starter)