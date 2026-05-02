package com.rodrigomoran.decisionplatform.audit_service.infrastructure.config;

import com.rodrigomoran.decisionplatform.audit_service.application.dto.DecisionMadeAuditMessage;
import com.rodrigomoran.decisionplatform.audit_service.application.dto.RulePublishedAuditMessage;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JacksonJsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.group-id}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, DecisionMadeAuditMessage> decisionMadeConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                commonConsumerProperties(DecisionMadeAuditMessage.class)
        );
    }

    @Bean
    public ConsumerFactory<String, RulePublishedAuditMessage> rulePublishedConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(
                commonConsumerProperties(RulePublishedAuditMessage.class)
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, DecisionMadeAuditMessage> decisionMadeKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, DecisionMadeAuditMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(decisionMadeConsumerFactory());

        return factory;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RulePublishedAuditMessage> rulePublishedKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, RulePublishedAuditMessage> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(rulePublishedConsumerFactory());

        return factory;
    }

    private Map<String, Object> commonConsumerProperties(Class<?> targetClass) {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JacksonJsonDeserializer.class);

        props.put(JacksonJsonDeserializer.TRUSTED_PACKAGES,
                "com.rodrigomoran.decisionplatform.audit_service.application.dto");

        props.put(JacksonJsonDeserializer.VALUE_DEFAULT_TYPE, targetClass.getName());
        props.put(JacksonJsonDeserializer.USE_TYPE_INFO_HEADERS, false);

        return props;
    }
}