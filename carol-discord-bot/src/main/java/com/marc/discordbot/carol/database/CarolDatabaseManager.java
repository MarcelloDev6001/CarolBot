package com.marc.discordbot.carol.database;

import com.marc.discordbot.carol.file.JsonUtils;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// i admit, this was made by me but i used ChatGPT to optimize this a LOT
public class CarolDatabaseManager {
    public static String databaseURL = "";
    public static String anonKey = "";

    private static final HttpClient CLIENT = HttpClient.newHttpClient();

    private static HttpRequest.Builder baseRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", anonKey)
                .header("Authorization", "Bearer " + anonKey);
    }

    private static <T> @Nullable T decodeSingle(Class<T> clazz, String body) {
        try {
            if (body == null || body.equals("[]")) return null;
            return JsonUtils.decodeFromString(clazz, body.substring(1, body.length() - 1));
        } catch (Exception e) {
            System.out.println("Error decoding JSON: " + e);
            return null;
        }
    }

    private static <T> String encode(T obj) {
        try {
            return JsonUtils.encodeFromObject(obj);
        } catch (Exception e) {
            System.out.println("Failed to encode object: " + e);
            return null;
        }
    }

    private static String tableUrl(String table, long id, boolean select) {
        return databaseURL + "/rest/v1/" + table + "?id=eq." + id + (select ? "&select=*" : "");
    }

    // ---------- Generic Methods ----------
    public static <T> @Nullable T getEntity(String table, long id, Class<T> clazz) {
        String url = tableUrl(table, id, true);

        HttpRequest request = baseRequest(url)
                .header("Accept", "application/json")
                .build();

        try {
            HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            return decodeSingle(clazz, response.body());
        } catch (Exception e) {
            System.out.println("Error fetching entity: " + e);
            return null;
        }
    }

    public static <T> T addEntity(String table, T entity, T defaultEntity) throws IOException, InterruptedException {
        String insertUrl = databaseURL + "/rest/v1/" + table;

        String json = encode(defaultEntity);
        if (json == null) return entity;

        HttpRequest request = baseRequest(insertUrl)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        return defaultEntity;
    }

    public static <T> T getOrCreateEntity(String table, long id, Class<T> clazz, T defaultEntity) {
        T entity = getEntity(table, id, clazz);
        if (entity == null) {
            try {
                return addEntity(table, defaultEntity, defaultEntity);
            } catch (Exception e) {
                System.out.println("Error creating entity: " + e);
            }
        }
        return entity;
    }

    public static <T> void updateEntity(String table, long id, T entity) throws IOException, InterruptedException {
        String json = encode(entity);
        if (json == null) return;

        String url = tableUrl(table, id, false);

        HttpRequest request = baseRequest(url)
                .header("Content-Type", "application/json")
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                .build();

        CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
    }

    // ---------- User wrappers ----------
    public static CarolDatabaseUser getUserFromDatabase(long id) {
        return getEntity(CarolDatabaseTables.USERS_TABLE, id, CarolDatabaseUser.class);
    }

    public static CarolDatabaseUser addUserToDatabase(CarolDatabaseUser user) throws IOException, InterruptedException {
        return addEntity(CarolDatabaseTables.USERS_TABLE, user, CarolDatabaseUser.getDefault(user.getId()));
    }

    public static CarolDatabaseUser getOrCreateUser(long id) {
        return getOrCreateEntity(CarolDatabaseTables.USERS_TABLE, id, CarolDatabaseUser.class, CarolDatabaseUser.getDefault(id));
    }

    public static void updateUser(long id, CarolDatabaseUser user) throws IOException, InterruptedException {
        updateEntity(CarolDatabaseTables.USERS_TABLE, id, user);
    }

    // ---------- Guild wrappers ----------
    public static CarolDatabaseGuild getGuildFromDatabase(long id) {
        return getEntity(CarolDatabaseTables.GUILDS_TABLE, id, CarolDatabaseGuild.class);
    }

    public static CarolDatabaseGuild addGuildToDatabase(CarolDatabaseGuild guild) throws IOException, InterruptedException {
        return addEntity(CarolDatabaseTables.GUILDS_TABLE, guild, CarolDatabaseGuild.getDefault(guild.getId()));
    }

    public static CarolDatabaseGuild getOrCreateGuild(long id) {
        return getOrCreateEntity(CarolDatabaseTables.GUILDS_TABLE, id, CarolDatabaseGuild.class, CarolDatabaseGuild.getDefault(id));
    }

    public static void updateGuild(long id, CarolDatabaseGuild guild) throws IOException, InterruptedException {
        updateEntity(CarolDatabaseTables.GUILDS_TABLE, id, guild);
    }
}
