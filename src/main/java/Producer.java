import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.Random;

public class Producer {

    private static final String EXCHANGE_NAME = "logs";
    private static final String ROUTING_KEY = "#my_route";
    private static final String BROKER_HOST = System.getenv("broker_host");

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        Random random = new Random();
        double temperature = random.nextDouble() * 100;
        factory.setHost(BROKER_HOST);

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            String message = "La température est de: " + temperature + " degrés";
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
/*
            // Exemple de code pour récupérer les quantités d'eau et de café
            int quantite_eau = getQuantitéEau();
            int quantite_cafe = getQuantitéCafe();

            // Exemple de code pour déterminer les seuils
            int seuil = 10;

            // Calcul de la quantité restante d'eau et de café après avoir fait un café
            quantite_eau = quantite_eau -  quantité d'eau utilisée pour un café ;
            quantite_cafe = quantite_cafe -  quantité de café utilisée pour un café ;

            // Si la quantité d'eau est inférieure au seuil, envoi d'un message
            if (quantite_eau < seuil) {
                String messageEau = "Le seuil d'eau de la machine à café a été atteint, pensez à la remplir.";
                channel.basicPublish(EXCHANGE_NAME, "", null, messageEau.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + messageEau + "'");
            }

            // Si la quantité de café est inférieure au seuil, envoi d'un message
            if (quantite_cafe < seuil) {
                String messageCafe = "Faites attention, il ne vous reste du café que pour très peu de tasses encore, pensez à le remplir.";
                channel.basicPublish(EXCHANGE_NAME, "", null, messageCafe.getBytes("UTF-8"));
                System.out.println(" [x] Sent '" + messageCafe + "'");
            }
        }
    }

    // Méthode factice pour obtenir la quantité d'eau (à remplacer par votre logique)
    private static int getQuantitéEau() {
        return 50; // Exemple de valeur fictive
    

    // Méthode factice pour obtenir la quantité de café (à remplacer par votre logique)
    private static int getQuantitéCafe() {
        return 20; // Exemple de valeur fictive
    }*/

}
}
}
