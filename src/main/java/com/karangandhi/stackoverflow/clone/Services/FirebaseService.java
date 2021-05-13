package com.karangandhi.stackoverflow.clone.Services;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import java.io.FileInputStream;
import java.io.IOException;

public class FirebaseService {
    public static FirebaseApp app;
    public static final boolean verbose = true;
    
    public static void InitializeApp() throws IOException {
        FileInputStream serviceAccount = new FileInputStream("src/secrets/stackoverflow-clone-82cd2-firebase-adminsdk-zrtlk-521cad8fd0.json");
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setDatabaseUrl("https://stackoverflow-clone-82cd2.firebaseio.com")
                .build();
        app = FirebaseApp.initializeApp(options, "Stackoverflow-clone");
    }
}
