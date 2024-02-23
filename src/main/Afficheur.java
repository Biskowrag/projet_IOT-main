import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;

public class Afficheur {
    public static void main(String[] args) {
        try (MongoClient mongoClient = MongoClients.create("mongodb://mon_mongo:27017")) {
            // Choisir la base de données et la collection
            MongoDatabase database = mongoClient.getDatabase("bd_cafe");
            MongoCollection<Document> collection = database.getCollection("ma_collection");

            // Récupérer tous les documents de la collection
            FindIterable<Document> documents = collection.find();

            // Parcourir les documents et les afficher
            MongoCursor<Document> cursor = documents.iterator();
            while (cursor.hasNext()) {
                System.out.println(cursor.next().toJson());
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des données : " + e.getMessage());
        }
    }
}
