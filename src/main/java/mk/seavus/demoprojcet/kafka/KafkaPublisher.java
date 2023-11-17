package mk.seavus.demoprojcet.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
@Slf4j
public class KafkaPublisher {

    private KafkaPublisherConfig kafkaPublisherConfig;

    public KafkaPublisher(KafkaPublisherConfig kafkaProducerConfig) {
        this.kafkaPublisherConfig = kafkaProducerConfig;
    }

    @Value("${spring.kafka.template.default-topic}")
    private String kafkaTopic;

    private KafkaProducer<String, String> kafkaProducer;

    @Bean
    private void build() {
        Properties kafkaProperties = kafkaPublisherConfig.getKafkaProperties();
        kafkaProducer = new KafkaProducer<>(kafkaProperties);
    }

    public void sendMessage(String message) {
        try {
            kafkaProducer.send(new ProducerRecord<>(kafkaTopic, message));
            log.info("The message send to topic '{}'", kafkaTopic);
        } catch (Exception e) {
            log.error("Cannot send a message to topic '{}'. {}", kafkaTopic, e.getMessage());
        }
    }
}
