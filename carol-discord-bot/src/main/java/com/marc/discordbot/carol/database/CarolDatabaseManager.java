package com.marc.discordbot.carol.database;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.marc.discordbot.carol.CarolSettings;
import com.marc.discordbot.carol.database.entities.CarolDatabaseGuild;
import com.marc.discordbot.carol.database.entities.CarolDatabaseUser;
import org.jetbrains.annotations.Nullable;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

// i admit, this was made by me but i used ChatGPT to optimize this a LOT
// Originally this project would use Supabase as main database, but this database is a little confuse for me ;-;
public class CarolDatabaseManager {
    private static Firestore db;

    public static void initialize() throws IOException {
        FileInputStream serviceAccount = new FileInputStream(CarolSettings.FIREBASE_KEY_PATH);

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
        db = FirestoreClient.getFirestore();
    }

    // ---------- Generic Methods ----------
    private static <T> @Nullable T getEntity(String collection, String id, Class<T> clazz) {
        try {
            DocumentReference docRef = db.collection(collection).document(id);
            ApiFuture<DocumentSnapshot> future = docRef.get();
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                return document.toObject(clazz);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static <T> void addOrUpdateEntity(String collection, String id, T entity) {
        try {
            DocumentReference docRef = db.collection(collection).document(id);
            ApiFuture<WriteResult> future = docRef.set(entity);
            //System.out.println("Updated at: " + future.get().getUpdateTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> T getOrCreateEntity(String collection, String id, Class<T> clazz, T defaultEntity) {
        T entity = getEntity(collection, id, clazz);
        if (entity == null) {
            addOrUpdateEntity(collection, id, defaultEntity);
            return defaultEntity;
        }
        return entity;
    }

    // ---------- User wrappers ----------
    public static CarolDatabaseUser getUserFromDatabase(long id) {
        return getEntity(CarolDatabaseTables.USERS_TABLE, String.valueOf(id), CarolDatabaseUser.class);
    }

    public static void addUserToDatabase(CarolDatabaseUser user) {
        addOrUpdateEntity(CarolDatabaseTables.USERS_TABLE, String.valueOf(user.getId()), user);
    }

    public static CarolDatabaseUser getOrCreateUser(long id) {
        return getOrCreateEntity(CarolDatabaseTables.USERS_TABLE, String.valueOf(id), CarolDatabaseUser.class,
                CarolDatabaseUser.getDefault(id));
    }

    public static void updateUser(long id, CarolDatabaseUser user) {
        addOrUpdateEntity(CarolDatabaseTables.USERS_TABLE, String.valueOf(id), user);
    }

    // ---------- Guild wrappers ----------
    public static CarolDatabaseGuild getGuildFromDatabase(long id) {
        return getEntity(CarolDatabaseTables.GUILDS_TABLE, String.valueOf(id), CarolDatabaseGuild.class);
    }

    public static void addGuildToDatabase(CarolDatabaseGuild guild) {
        addOrUpdateEntity(CarolDatabaseTables.GUILDS_TABLE, String.valueOf(guild.getId()), guild);
    }

    public static void getOrCreateGuild(long id) {
        getOrCreateEntity(CarolDatabaseTables.GUILDS_TABLE, String.valueOf(id), CarolDatabaseGuild.class,
                CarolDatabaseGuild.getDefault(id));
    }

    public static void updateGuild(long id, CarolDatabaseGuild guild) {
        addOrUpdateEntity(CarolDatabaseTables.GUILDS_TABLE, String.valueOf(id), guild);
    }
}
