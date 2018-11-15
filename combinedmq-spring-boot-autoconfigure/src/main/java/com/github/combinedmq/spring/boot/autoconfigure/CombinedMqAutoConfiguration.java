package com.github.combinedmq.spring.boot.autoconfigure;

import com.alibaba.fastjson.JSON;
import com.github.combinedmq.AbstractConfigurationFactory;
import com.github.combinedmq.activemq.ActiveMqConfiguration;
import com.github.combinedmq.kafka.KafkaConfiguration;
import com.github.combinedmq.rabbitmq.RabbitMqConfiguration;
import com.github.combinedmq.spring.ConfigBean;
import com.github.combinedmq.spring.annotation.CombinedMqScanRegistrar;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.type.AnnotationMetadata;

import java.util.List;

/**
 * spring boot automatic configuration support.
 *
 * @author xiaoyu
 * @see CombinedMqProperties
 * @since 1.0.0
 */

@Configuration
@ConditionalOnClass({AbstractConfigurationFactory.class})
@EnableConfigurationProperties(CombinedMqProperties.class)
public class CombinedMqAutoConfiguration {

    private CombinedMqProperties properties;

    public CombinedMqAutoConfiguration(CombinedMqProperties properties) {
        this.properties = properties;
    }

    @Configuration
    @Import(AutoConfiguredCombinedMqScanRegistrar.class)
    @EnableConfigurationProperties(CombinedMqProperties.class)
    protected static class CombinedMqScanRegistrarConfiguration {
        private CombinedMqProperties properties;

        public CombinedMqScanRegistrarConfiguration(CombinedMqProperties properties) {
            this.properties = properties;
        }

        private com.github.combinedmq.configuration.Configuration checkConfig() {
            CombinedMqProperties.Activemq activemqConfig = properties.getActivemq();
            CombinedMqProperties.Rabbitmq rabbitmqConfig = properties.getRabbitmq();
            CombinedMqProperties.Kafka kafkaConfig = properties.getKafka();

            if (activemqConfig == null && rabbitmqConfig == null && kafkaConfig == null) {
                throw new IllegalStateException("CombinedMq的Spring Boot配置不存在");
            }

            if ((rabbitmqConfig != null && activemqConfig != null)
                    || (rabbitmqConfig != null && kafkaConfig != null)
                    || (activemqConfig != null && kafkaConfig != null)) {
                throw new IllegalStateException("配置重复，Spring Boot配置的rabbitmq、activemq、kafka只能存在一种");
            }
            com.github.combinedmq.configuration.Configuration configuration = null;
            if (rabbitmqConfig != null) {
                configuration = JSON.parseObject(JSON.toJSONString(rabbitmqConfig), RabbitMqConfiguration.class);
            } else if (activemqConfig != null) {
                configuration = JSON.parseObject(JSON.toJSONString(activemqConfig), ActiveMqConfiguration.class);
            } else if (kafkaConfig != null) {
                configuration = JSON.parseObject(JSON.toJSONString(kafkaConfig), KafkaConfiguration.class);
            }
            return configuration;
        }

        @Bean
        @ConditionalOnMissingBean
        public ConfigBean combinedMqConfiguration() {
            ConfigBean configBean = new ConfigBean();
            configBean.setConfiguration(checkConfig());
            return configBean;
        }
    }

    public static class AutoConfiguredCombinedMqScanRegistrar extends CombinedMqScanRegistrar implements BeanFactoryAware {
        private BeanFactory beanFactory;

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            List<String> packagesToScan = AutoConfigurationPackages.get(this.beanFactory);
            for (String s : packagesToScan) {
                registerQueueBeanDefinitions(registry, s);
            }
            registerConsumerListenerProcessor(registry);
            registerProducerProcessor(registry);
        }

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }
    }
}
