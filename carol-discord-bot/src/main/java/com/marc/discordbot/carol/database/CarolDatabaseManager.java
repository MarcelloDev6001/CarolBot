package com.marc.discordbot.carol.database;

import com.marc.discordbot.carol.file.JsonUtils;
import kotlinx.serialization.json.Json;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;

public class CarolDatabaseManager {
    public static String databaseURL = "";
    public static String anonKey = "";

    @Nullable
    public static CarolDatabaseUser getUserFromDatabase(long id) throws IOException, InterruptedException {
        String url = databaseURL + "/rest/v1/" + CarolDatabaseTables.USERS_TABLE + "?id=eq." + id + "&select=*";
        String apiKey = anonKey;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", apiKey)
                .header("Authorization", "Bearer " + apiKey)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        try {
            System.out.println(response.body());
            return JsonUtils.decodeFromString(CarolDatabaseUser.class, response.body().substring(1, response.body().length() - 1));
        } catch (Exception e) {
            System.out.println("Error on decode user from database: " + e);
            return null;
        }
    }

    public static CarolDatabaseUser addUserToDatabase(CarolDatabaseUser user) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();

        String insertUrl = databaseURL + "/rest/v1/" + CarolDatabaseTables.USERS_TABLE;

        String newUserJson = "";
        try {
            newUserJson = JsonUtils.encodeFromObject(CarolDatabaseUser.getDefault(user.getId()));
        } catch (Exception e) {
            System.out.println("Failed to convert user to json: " + e);
            return user;
        }

        HttpRequest postRequest = HttpRequest.newBuilder()
                .uri(URI.create(insertUrl))
                .header("apikey", anonKey)
                .header("Authorization", "Bearer " + anonKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(newUserJson))
                .build();

        HttpResponse<String> postResponse = client.send(postRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(postResponse.body());
        return CarolDatabaseUser.getDefault(user.getId());
    }

    public static CarolDatabaseUser getOrCreateUser(long id) throws IOException, InterruptedException {
        String url = databaseURL + "/rest/v1/" + CarolDatabaseTables.USERS_TABLE + "?id=eq." + id + "&select=*";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("apikey", anonKey)
                .header("Authorization", "Bearer " + anonKey)
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        String json = getResponse.body();

        if (json.equals("[]")) {
            return addUserToDatabase(new CarolDatabaseUser(id, 0, new HashMap<String, Integer>()));
        }

        return JsonUtils.decodeFromString(CarolDatabaseUser.class, json.substring(1, json.length() - 1));
    }
}
