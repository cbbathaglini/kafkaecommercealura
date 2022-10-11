package ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class KafkaService<T> implements Closeable {
    private final KafkaConsumer<String,T> consumer;
    private final ConsumerFunction parse;

    KafkaService(String groupId, String topic, ConsumerFunction parse, Class<T> type, Map<String,String> extraproperties) {
        this.parse = parse;
        this.consumer = new KafkaConsumer<String, T>(properties(groupId, type,extraproperties));
        consumer.subscribe(Collections.singletonList(topic)); //escutando apenas um tópico

    }

    KafkaService(String groupId, Pattern topic, ConsumerFunction parse,  Class<T> type,Map<String,String> extraproperties) {
        this.parse = parse;
        this.consumer = new KafkaConsumer<String, T>(properties(groupId,type,extraproperties));
        consumer.subscribe(topic); //escutando apenas um tópico

    }



    void run() {
        while(true) {
            var records = consumer.poll(Duration.ofMillis(100)); //tem mensagem ai dentro?

            if (!records.isEmpty()) {
                System.out.println("Encontrados "+records.count()+ " registros");
            }

            for (var record : records) {
                try {
                    parse.consume(record);
                }catch (Exception e){
                    //catch é exception para pegar qualquer tipo de exceção
                    e.printStackTrace();
                }
            }
        }
    }


    private Properties properties(String groupId, Class<T> type,Map<String,String> extraproperties) {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());
        properties.putAll(extraproperties);
        return properties;



    }


    @Override
    public void close() throws IOException {
        consumer.close();
    }
}
