CombinedMq Spring Boot
========================
[![Build Status](https://travis-ci.com/combinedmq/combinedmq-spring-boot.svg?branch=master)](https://travis-ci.com/combinedmq/combinedmq-spring-boot)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.combinedmq/combinedmq-spring-boot.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.combinedmq%22%20AND%20a:%22combinedmq-spring-boot%22)

[English](https://github.com/combinedmq/combinedmq-spring-boot/blob/master/README.md)

CombinedMq Spring Boot可以帮助您将CombinedMq集成到Spring Boot中，通过application.yml配置和使用注解的方式，能够非常方便的完成集成工作。

### 概述
使用注解的方式来使用CombinedMq，会涉及到四个注解:
1. @Queue - 标注在一个接口类型上
2. @Producer - 标注在类的成员变量上，该变量应该是一个由@Queue标注的接口,用来发送消息
3. @Consumer - 标注在一个由@Queue标注的接口的实现类上，用来接收消息
4. @EnableCombinedMq - 指定basePackages，开启对@Queue注解进行扫描的功能
## 使用步骤
### Step 1: Maven依赖

```xml
<dependency>
  <groupId>com.github.combinedmq</groupId>
  <artifactId>combinedmq-spring-boot-starter</artifactId>
  <version>1.0.2</version>
</dependency>
```
### Step 2: 创建一个接口

该接口的所有方法返回类型都只能是void类型:
```java
@Queue(name = "x.y.z")
public interface GreetingService {
    void sayHi(String name);
}
```
### Step 3: 实现消费者功能
- application.yml配置

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

- 实现GreetingService接口

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

- 启动Spring Boot Consumer程序

```java
@EnableCombinedMq({"combinedmq"})
@SpringBootApplication
public class ConsumerMain {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ConsumerMain.class).web(false).run(args);
    }
}
```
### Step 4: 实现生产者功能
- application.yml配置

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
- 使用@Producer注入GreetingService

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
- 启动Spring Boot Producer程序

```java
@EnableCombinedMq({"combinedmq"})
@SpringBootApplication
public class ProducerMain {
    public static void main(String[] args) {
        SpringApplication.run(ProducerMain.class, args);
    }
}
```
## 项目模块
CombinedMq Spring Boot 由以下几个子模块组成:

- [combinedmq-spring-boot-autoconfigure](https://github.com/combinedmq/combinedmq-spring-boot/tree/master/combinedmq-spring-boot-autoconfigure)

- [combinedmq-spring-boot-samples](https://github.com/combinedmq/combinedmq-spring-boot/tree/master/combinedmq-spring-boot-samples)

- [combinedmq-spring-boot-starter](https://github.com/combinedmq/combinedmq-spring-boot/tree/master/combinedmq-spring-boot-starter)