package dev.javarush.rabbitmq_tutorials;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class RoutingDemoConsumer {
    private static final String EXCHANGE_NAME = "direct_log";
    
    private static final String hostName = System.getenv("RABBITMQ_HOST");
    private static final int port = Integer.parseInt(System.getenv("RABBITMQ_PORT"));
    private static final String username = System.getenv("RABBITMQ_USERNAME");
    private static final String password = System.getenv("RABBITMQ_PASSWORD");
    public static void main(String[] args) throws IOException, TimeoutException {
        Scanner scanner = new Scanner(System.in);
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(hostName);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");
        String queueName = channel.queueDeclare().getQueue();

        System.out.print("Which severity you are working with: ");
        String[] severities = scanner.nextLine().split(",");

        for (String severity: severities) {
            channel.queueBind(queueName, EXCHANGE_NAME, severity);
        }

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received | [" + delivery.getEnvelope().getRoutingKey() + "] " + message);
        };

        channel.basicConsume(queueName, true, deliverCallback, cosumerTag -> {});
    }
}
