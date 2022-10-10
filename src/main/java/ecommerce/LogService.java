package ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.regex.Pattern;

public class LogService {
    public static void main(String[] args) {
        var consumer = new KafkaConsumer<String, String>(properties());

        consumer.subscribe(Pattern.compile("ECOMMERCE.*")); //escutando apenas um tópico

        while(true) {
            var records = consumer.poll(Duration.ofMillis(100)); //tem mensagem ai dentro?

            if (!records.isEmpty()) {
                System.out.println("Encontrados "+records.count()+ " registros");
            }

            for (var record : records) {
                System.out.println("<-----------------------------------------");
                System.out.println("TÓPICO: "+ record.topic());
                System.out.println("key: " + record.key());
                System.out.println("value: " + record.key());
                System.out.println("partição: " + record.partition());
                System.out.println("offset: " + record.offset());
                System.out.println("----------------------------------------->");
            }
        }
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, ecommerce.EmailService.class.getSimpleName());
        return properties;
    }
}

