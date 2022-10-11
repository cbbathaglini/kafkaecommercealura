package ecommerce;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {

        try(var orderdispatcher = new OrderKafkaDispatcher<Order>()) {
            try(var emaildispatcher = new OrderKafkaDispatcher<Email>()) {
                for (var i = 0; i < 30; i++) {

                    var userId = UUID.randomUUID().toString();
                    var orderId = UUID.randomUUID().toString();
                    var amount = Math.random() * 5000 + 1;
                    var emailPerson = Math.random()+1000+"@email.com";

                    var order = new Order(userId, orderId, new BigDecimal(amount),emailPerson);
                    orderdispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);

                    var emailvalue = new Email("Processing Order","Thank you for your order! We are processing your order!");
                    emaildispatcher.send("ECOMMERCE_SENDEMAIL_NEW_ORDER", userId, emailvalue);
                }
            }
        }
    }




}
