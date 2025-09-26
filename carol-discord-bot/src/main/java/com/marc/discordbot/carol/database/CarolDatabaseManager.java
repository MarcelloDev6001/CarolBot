package com.marc.discordbot.carol.database;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.marc.discordbot.carol.CarolSettings;
import com.marc.discordbot.carol.cache.CarolCacheManager;
import com.marc.discordbot.carol.cache.CarolDatabaseCache;
import com.marc.discordbot.carol.database.entities.guild.CarolDatabaseGuild;
import com.marc.discordbot.carol.database.entities.user.CarolDatabaseUser;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;

// i admit, this was made by me but i used ChatGPT to optimize this a LOT
// Originally this project would use Supabase as main database, but this database is a little confuse for me ;-;
public class CarolDatabaseManager {
    private static Firestore db;

    public static InputStream getDatabaseKeyThing()
    {
        try { // try to found the file
            return new FileInputStream(CarolSettings.FIREBASE_KEY_PATH);
        } catch (FileNotFoundException e) { // if not found, then use an environment variable
            String firebaseKeyJson = System.getenv("FIREBASE_KEY");
            if (firebaseKeyJson == null || firebaseKeyJson.isEmpty()) {
                throw new RuntimeException("FIREBASE_KEY variable not defined!");
            }

            return new ByteArrayInputStream(firebaseKeyJson.getBytes(StandardCharsets.UTF_8));
        }
    }

    public static void initialize() throws IOException {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(getDatabaseKeyThing()))
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

    private static CarolDatabaseGuild getCachedGuild(long id)
    {
        for (CarolDatabaseGuild dbGuildCached : CarolDatabaseCache.dbGuildsCached)
        {
            if (dbGuildCached != null && dbGuildCached.getId() == id)
            {
                return dbGuildCached;
            }
        }

        return null;
    }

    private static void setCachedGuild(long id, CarolDatabaseGuild guild)
    {
        CarolDatabaseGuild dbGuildCachedObject = null;
        for (CarolDatabaseGuild dbGuildCached : CarolDatabaseCache.dbGuildsCached)
        {
            if (dbGuildCached.getId() == id)
            {
                dbGuildCachedObject = dbGuildCached;
                break;
            }
        }

        if (dbGuildCachedObject == null)
        {
            CarolDatabaseCache.dbGuildsCached.add(guild);
        }
        else
        {
            CarolDatabaseCache.dbGuildsCached.set(CarolDatabaseCache.dbGuildsCached.indexOf(dbGuildCachedObject), guild);
        }
    }

    private static CarolDatabaseUser getCachedUser(long id)
    {
        for (CarolDatabaseUser dbUserCached : CarolDatabaseCache.dbUsersCached)
        {
            if (dbUserCached != null && dbUserCached.getId() == id)
            {
                return dbUserCached;
            }
        }

        return null;
    }

    private static void setCachedUser(long id, CarolDatabaseUser user)
    {
        CarolDatabaseUser dbUserCachedObject = null;
        for (CarolDatabaseUser dbUserCached : CarolDatabaseCache.dbUsersCached)
        {
            if (dbUserCached != null && dbUserCached.getId() == id)
            {
                dbUserCachedObject = dbUserCached;
                break;
            }
        }

        if (dbUserCachedObject == null)
        {
            CarolDatabaseCache.dbUsersCached.add(user);
        }
        else
        {
            CarolDatabaseCache.dbUsersCached.set(CarolDatabaseCache.dbUsersCached.indexOf(dbUserCachedObject), user);
        }
    }

    // ---------- User wrappers ----------
    public static CarolDatabaseUser getUserFromDatabase(long id) {
        CarolDatabaseUser cachedUser = getCachedUser(id);
        if (cachedUser != null)
        {
            return cachedUser;
        }
        return getEntity(CarolDatabaseTables.USERS_TABLE, String.valueOf(id), CarolDatabaseUser.class);
    }

    public static void addUserToDatabase(CarolDatabaseUser user) {
        CarolDatabaseUser cachedUser = getCachedUser(user.getId());
        if (cachedUser != null)
        {
            setCachedUser(cachedUser.getId(), user);
            return;
        }
        addOrUpdateEntity(CarolDatabaseTables.USERS_TABLE, String.valueOf(user.getId()), user);
    }

    public static CarolDatabaseUser getOrCreateUser(long id) {
        CarolDatabaseUser cachedUser = getCachedUser(id);
        if (cachedUser != null)
        {
            return cachedUser;
        }
        return getOrCreateEntity(CarolDatabaseTables.USERS_TABLE, String.valueOf(id), CarolDatabaseUser.class,
                CarolDatabaseUser.getDefault(id));
    }

    public static void updateUser(long id, CarolDatabaseUser user) {
        setCachedUser(id, user);

        // addOrUpdateEntity(CarolDatabaseTables.USERS_TABLE, String.valueOf(id), user);
    }

    public static void updateUserOnDatabase(long id, CarolDatabaseUser user)
    {
        addOrUpdateEntity(CarolDatabaseTables.USERS_TABLE, String.valueOf(id), user);
    }

    // ---------- Guild wrappers ----------
    public static CarolDatabaseGuild getGuildFromDatabase(long id) {
        CarolDatabaseGuild cachedGuild = getCachedGuild(id);
        if (cachedGuild != null)
        {
            return cachedGuild;
        }
        return getEntity(CarolDatabaseTables.GUILDS_TABLE, String.valueOf(id), CarolDatabaseGuild.class);
    }

    public static void addGuildToDatabase(CarolDatabaseGuild guild) {
        CarolDatabaseGuild cachedGuild = getCachedGuild(guild.getId());
        if (cachedGuild != null)
        {
            setCachedGuild(cachedGuild.getId(), guild);
            return;
        }
        addOrUpdateEntity(CarolDatabaseTables.GUILDS_TABLE, String.valueOf(guild.getId()), guild);
    }

    public static CarolDatabaseGuild getOrCreateGuild(long id) {
        CarolDatabaseGuild cachedGuild = getCachedGuild(id);
        if (cachedGuild != null)
        {
            return cachedGuild;
        }
        return getOrCreateEntity(CarolDatabaseTables.GUILDS_TABLE, String.valueOf(id), CarolDatabaseGuild.class,
                CarolDatabaseGuild.getDefault(id));
    }

    public static void updateGuild(long id, CarolDatabaseGuild guild) {
        setCachedGuild(id, guild);

        // addOrUpdateEntity(CarolDatabaseTables.GUILDS_TABLE, String.valueOf(id), guild);
    }

    public static void updateGuildOnDatabase(long id, CarolDatabaseGuild guild)
    {
        addOrUpdateEntity(CarolDatabaseTables.GUILDS_TABLE, String.valueOf(id), guild);
    }
}
