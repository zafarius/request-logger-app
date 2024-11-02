package app.jms;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.annotation.PostConstruct;
import jakarta.jms.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jms.artemis.ArtemisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.annotation.Validated;

@Validated
@Slf4j
@Configuration
@EnableJms
@EnableAutoConfiguration
@EnableTransactionManagement(proxyTargetClass = true)
@ComponentScan(basePackageClasses = JMSConfiguration.class)
@AutoConfigureAfter(ArtemisAutoConfiguration.class)
public class JMSConfiguration {
    public static final String JMS_MESSAGE_TYPE_ID_PROPERTY_NAME = "_type";

    @PostConstruct
    public void init() {
        log.info("Initializing Audit Message Adapter JMS");
    }

    @Bean
    public MessageConverter jmsMessageConverter() {
        val converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName(JMS_MESSAGE_TYPE_ID_PROPERTY_NAME);
        converter.setObjectMapper(jmsObjectMapper());

        return converter;
    }

    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper jmsObjectMapper() {
        return new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    @Primary
    JmsTemplate jmsMessageTemplate(final ConnectionFactory connectionFactory) {
        val jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setMessageConverter(jmsMessageConverter());
        jmsTemplate.setPubSubDomain(true);
        return jmsTemplate;
    }


}
