package dev.javarush.rabbitmq_tutorials;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class TopicLogsProducer {

    private static final String EXCHANGE_NAME = "topic_logs";

    private static final String HOST = System.getenv("RABBITMQ_HOST");
    private static final int PORT = Integer.parseInt(System.getenv("RABBITMQ_PORT"));
    private static final String USERNAME = System.getenv("RABBITMQ_USERNAME");
    private static final String PASSWORD = System.getenv("RABBITMQ_PASSWORD");

    public static void main(String[] args) throws IOException, TimeoutException {
        Scanner scanner = new Scanner(System.in);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setPort(PORT);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        try (Connection connection = factory.newConnection(); Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "topic");
            while (true) {
                System.out.print("Routing key: ");
                String routingKey = scanner.nextLine();
                System.out.print("Message: ");
                String message = scanner.nextLine();
                channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes());
                System.out.println(" [x] Sent [" + routingKey + "] : '" + message + "'");
            }
        }
    }
}