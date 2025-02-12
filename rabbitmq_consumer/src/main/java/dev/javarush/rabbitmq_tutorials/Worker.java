package dev.javarush.rabbitmq_tutorials;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.nio.charset.StandardCharsets;

public class Worker {
  private static final String QUEUE_NAME = "task_queue";
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
    channel.basicQos(1);

    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
      String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
      System.out.println(" [x] Received '" + message + "'");
      try {
        doWork(message);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      } finally {
        System.out.println(" [x] Done");
        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
      }
    };
    channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {});
  }

  private static void doWork(String task) throws InterruptedException {
    for (char ch : task.toCharArray()) {
      if (ch == '.') {
        Thread.sleep(1000);
      }
    }
  }
}
