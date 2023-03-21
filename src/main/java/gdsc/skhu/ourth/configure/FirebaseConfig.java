package gdsc.skhu.ourth.configure;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import org.apache.tomcat.util.json.ParseException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    // firebase init
    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        InputStream firebaseKey = new ClassPathResource("firebase.json").getInputStream();

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(firebaseKey))
                .build();

        return FirebaseApp.initializeApp(options);
    }

    @Bean
    public FirebaseAuth getFirebaseAuth() throws IOException {
        return FirebaseAuth.getInstance(firebaseApp());
    }
}
