package dev.javarush.rabbitmq_tutorials;

import java.util.Scanner;

import com.rabbitmq.client.ConnectionFactory;

public class LogsProducer {
    private static final String EXCHANGE_NAME = "logs";

    private static final String hostName = System.getenv("RABBITMQ_HOST");
    private static final int port = Integer.parseInt(System.getenv("RABBITMQ_PORT"));
    private static final String username = System.getenv("RABBITMQ_USERNAME");
    private static final String password = System.getenv("RABBITMQ_PASSWORD");
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Hello world!");
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(hostName);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        try (var connection = connectionFactory.newConnection(); var channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            while (true) {
                System.out.print("Enter your message: ");
                String message = scanner.nextLine();
                channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + message + "'");
            }
        }
    }
}