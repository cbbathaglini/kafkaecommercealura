package ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.regex.Pattern;

public class EmailService {
    public static void main(String[] args) throws IOException {
        var emailService = new EmailService();
        try(var service = new KafkaService(EmailService.class.getSimpleName(),"ECOMMERCE_SENDEMAIL_NEW_ORDER", emailService::parse)) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, String> record){
        System.out.println("<-----------------------------------------");
        System.out.println("Enviando email...");
        System.out.println("key: " + record.key());
        System.out.println("value: " + record.value());
        System.out.println("partição: " + record.partition());
        System.out.println("offset: " + record.offset());
        System.out.println("----------------------------------------->");
    }
}
