package ecommerce;

import org.apache.kafka.common.protocol.types.Field;

public class Email {

    private final String subject,body;

    public Email(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    @Override
    public String toString() {
        return "Email{" +
                "subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
