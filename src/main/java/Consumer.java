import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.mongodb.client.MongoClients;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import java.util.Arrays;

import org.bson.Document;
import org.bson.UuidRepresentation;

public class Consumer {

  private static final String EXCHANGE_NAME = "logs";
  private static final String BROKER_HOST = System.getenv("broker_host");

  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost(BROKER_HOST);
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();

    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

    String queueName = channel.queueDeclare().getQueue();
    channel.queueBind(queueName, EXCHANGE_NAME, "");
    channel.queueDeclare("pile_Quantite_eau", true, false, false, null);
    channel.queueDeclare("pile_Quantite_cafe", true, false, false, null);

    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

    // Connexion à MongoDB

    // Callback pour traiter les messages reçus
    DeliverCallback deliverCallback = (consumerTag, delivery) -> {

      String message = new String(delivery.getBody(), "UTF-8");
      System.out.println(" [x] Received '" + message + "'");
      try (MongoClient mongoClient = MongoClients.create("mongodb://mon_mongo:27017")) {

        // Choisir la base de données et la collection
        MongoDatabase database = mongoClient.getDatabase("bd_cafe");
        MongoCollection<Document> collection = database.getCollection("ma_collection");
        // Insertion dans MongoDB
        try {
          Document document = new Document("temperature", message);
          collection.insertOne(document);
          System.out.println("Donnée insérée avec succès.");
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

    };

    // Consommer les messages depuis les files d'attente
    channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
    });
    channel.basicConsume("pile_Quantite_eau", true, deliverCallback, consumerTag -> {
    });
    channel.basicConsume("pile_Quantite_cafe", true, deliverCallback, consumerTag -> {
    });

  }
}
