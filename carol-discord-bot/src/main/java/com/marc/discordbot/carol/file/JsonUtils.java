package com.marc.discordbot.carol.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class JsonUtils {
    public static <T> T getFromFile(Class<T> tClass, String path) {
        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(path); // Replace with your file path

        try {
            return objectMapper.readValue(jsonFile, tClass);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to convert json to " + tClass.getName() + " from path: " + path);
            return null;
        }
    }
}