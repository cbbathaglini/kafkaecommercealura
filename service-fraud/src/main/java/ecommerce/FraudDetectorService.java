package ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class FraudDetectorService {
    public static void main(String[] args) throws IOException {

        var fraudService = new FraudDetectorService();
        try(var service = new KafkaService<Order>(
                FraudDetectorService.class.getSimpleName(),
                "ECOMMERCE_NEW_ORDER",
                fraudService::parse,
                Order.class,
                new HashMap<>())) {
            service.run();
        }

    }

    private final OrderKafkaDispatcher<Order> orderOrderKafkaDispatcher= new OrderKafkaDispatcher<Order>();

    private void parse(ConsumerRecord<String, Order> record) throws ExecutionException, InterruptedException {
        System.out.println("<-----------------------------------------");
        System.out.println("Processando se houve fraude...");
        System.out.println("key: " + record.key());
        System.out.println("value: " + record.value());
        System.out.println("partição: " + record.partition());
        System.out.println("offset: " + record.offset());

        var order = record.value();
        if(isFraud(order)){
            //pretending that the fraud happens when the amount is >= 4500
            System.out.println("Order is a fraud");
            orderOrderKafkaDispatcher.send("ECOMMERCE_ORDER_DENIED",order.getEmail(), order);
        }else{
            System.out.println("Order approved: " + order.toString());
            orderOrderKafkaDispatcher.send("ECOMMERCE_ORDER_APPROVED",order.getEmail(), order);
        }
        System.out.println("----------------------------------------->");
    }

    private boolean isFraud(Order order){
        return order.getAmount().compareTo(new BigDecimal("4500")) >= 0;
    }


//    private static Properties properties() {
//        var properties = new Properties();
//        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"127.0.0.1:9092");
//        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
//        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, FraudDetectorService.class.getSimpleName());
//        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, FraudDetectorService.class.getSimpleName()+"-"+ UUID.randomUUID().toString());
//        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
//        return properties;
//    }
}
