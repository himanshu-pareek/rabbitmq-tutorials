package dev.javarush.rabbitmq_tutorials;

import com.rabbitmq.client.ConnectionFactory;

import java.util.Scanner;

public class RabbitMQProducer {
    private static final String QUEUE_NAME = "hello";

    private static final String hostName = System.getenv("RABBITMQ_HOST");
    private static final int port = Integer.parseInt(System.getenv("RABBITMQ_PORT"));
    private static final String username = System.getenv("RABBITMQ_USERNAME");
    private static final String password = System.getenv("RABBITMQ_PASSWORD");

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(hostName);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        try (var connection = factory.newConnection();
                var channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            while (true) {
                System.out.print("Enter message to send: ");
                String message = scanner.nextLine();
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println("Sent ✅");
            }
        }
    }
}
