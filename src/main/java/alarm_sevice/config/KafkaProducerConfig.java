package alarm_sevice.config;

import alarm_sevice.domain.alarm.dto.CreateRequestDto;
import alarm_sevice.domain.alarm.kafkaDto.booking.RestaurantBookConfirmDto;
import alarm_sevice.domain.alarm.kafkaDto.booking.RestaurantBookDto;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    private final String SERVER = "localhost:9092";

    @Bean
    public Map<String, Object> getStringObjectMap() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return configProps;
    }

    @Bean
    public Map<String, Object> producerConfigs() {
        return getStringObjectMap();
    }

    @Bean
    public ProducerFactory<String, RestaurantBookDto> producerFactory1() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, RestaurantBookDto> kafkaTemplate1() {
        return new KafkaTemplate<>(producerFactory1());
    }


    @Bean
    public ProducerFactory<String, RestaurantBookConfirmDto> producerFactory2() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    @Bean
    public KafkaTemplate<String, RestaurantBookConfirmDto> kafkaTemplate2() {
        return new KafkaTemplate<>(producerFactory2());
    }


}
