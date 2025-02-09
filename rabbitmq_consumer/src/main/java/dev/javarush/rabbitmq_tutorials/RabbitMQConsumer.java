package dev.javarush.rabbitmq_tutorials;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class RabbitMQConsumer {
    private static final String QUEUE_NAME = "hello";

    private static final String hostName = System.getenv("RABBITMQ_HOST");
    private static final int port = Integer.parseInt(System.getenv("RABBITMQ_PORT"));
    private static final String username = System.getenv("RABBITMQ_USERNAME");
    private static final String password = System.getenv("RABBITMQ_PASSWORD");

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(hostName);
        factory.setPort(port);
        factory.setUsername(username);
        factory.setPassword(password);
        var connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message + "'");
        };
        channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });
    }
}