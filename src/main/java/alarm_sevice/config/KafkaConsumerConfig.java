package alarm_sevice.config;

import alarm_sevice.domain.alarm.dto.CreateRequestDto;
import alarm_sevice.domain.alarm.kafkaDto.backoffice.BackofficeRegisterDto;
import alarm_sevice.domain.alarm.kafkaDto.booking.*;
import alarm_sevice.domain.alarm.kafkaDto.register.ServiceRegisterRequestDto;
import alarm_sevice.domain.alarm.kafkaDto.waiting.CustomerFromSellerCancelDto;
import alarm_sevice.domain.alarm.kafkaDto.waiting.CustomerFromSellerDto;
import alarm_sevice.domain.alarm.kafkaDto.waiting.CustomerWaitingDto;
import alarm_sevice.domain.alarm.kafkaDto.waiting.SellerDto;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    private final String SERVER = "localhost:9092";

    public Map<String, Object> getStringObjectMap() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);

        configProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
        configProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class);
        configProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return configProps;
    }

    @Bean
    public Map<String, Object> consumerConfig() {
        return getStringObjectMap();
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BackofficeRegisterDto> kafkaListenerContainerFactoryBOR() {
        ConcurrentKafkaListenerContainerFactory<String, BackofficeRegisterDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfig()));
        return factory;
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookingCancelRequestDto> kafkaListenerContainerFactoryBCR() {
        ConcurrentKafkaListenerContainerFactory<String, BookingCancelRequestDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfig()));
        return factory;
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, BookingRequestDto> kafkaListenerContainerFactoryBR() {
        ConcurrentKafkaListenerContainerFactory<String, BookingRequestDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfig()));
        return factory;
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RestaurantBookCancelDto> kafkaListenerContainerFactoryRBCC() {
        ConcurrentKafkaListenerContainerFactory<String, RestaurantBookCancelDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfig()));
        return factory;
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RestaurantBookConfirmDto> kafkaListenerContainerFactoryRBCF() {
        ConcurrentKafkaListenerContainerFactory<String, RestaurantBookConfirmDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfig()));
        return factory;
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RestaurantBookDto> kafkaListenerContainerFactoryRtB() {
        ConcurrentKafkaListenerContainerFactory<String, RestaurantBookDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfig()));
        return factory;
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ServiceRegisterRequestDto> kafkaListenerContainerFactorySRR() {
        ConcurrentKafkaListenerContainerFactory<String, ServiceRegisterRequestDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfig()));
        return factory;
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CustomerFromSellerCancelDto> kafkaListenerContainerFactoryCFSC() {
        ConcurrentKafkaListenerContainerFactory<String, CustomerFromSellerCancelDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfig()));
        return factory;
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CustomerFromSellerDto> kafkaListenerContainerFactoryCFS() {
        ConcurrentKafkaListenerContainerFactory<String, CustomerFromSellerDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfig()));
        return factory;
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CustomerWaitingDto> kafkaListenerContainerFactoryCW() {
        ConcurrentKafkaListenerContainerFactory<String, CustomerWaitingDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfig()));
        return factory;
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SellerDto> kafkaListenerContainerFactoryS() {
        ConcurrentKafkaListenerContainerFactory<String, SellerDto> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfig()));
        return factory;
    }

    @Bean
    public StringJsonMessageConverter jsonMessageConverter() {
        return new StringJsonMessageConverter();
    }
}
