import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.Firestore;

import java.io.FileInputStream;
import java.io.IOException;

// made by ChatGPT (that's why comments are in Brazillian Portuguese)
public class FirestoreTest {
    public static void main(String[] args) {
        try {
            // Inicializa o FirebaseApp
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase-key.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            FirebaseApp.initializeApp(options); // ⚠️ Isso é essencial

            // Agora pega a instância do Firestore
            Firestore db = FirestoreClient.getFirestore();

            // Teste: listar coleções
            db.listCollections().forEach(c -> System.out.println("Coleção: " + c.getId()));

            System.out.println("Firestore está ativo!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
