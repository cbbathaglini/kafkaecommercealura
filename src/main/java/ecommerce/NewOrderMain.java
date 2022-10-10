package ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        try(var dispatcher = new KafkaDispatcher()) {
            for (var i = 0; i < 30; i++) {

                var key = UUID.randomUUID().toString();
                var value = key + ",800,457.4";
                dispatcher.send("ECOMMERCE_NEW_ORDER", key, value);

                var emailvalue = "Thank you for your order! We are processing your order!";
                dispatcher.send("ECOMMERCE_SENDEMAIL_NEW_ORDER", key, emailvalue);
            }
        }
    }




}
