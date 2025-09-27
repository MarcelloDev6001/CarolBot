package com.marc.discordbot.carol.website.backend;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.EnumSet;

@SpringBootApplication
public class CarolBackendApplication {
    public static JDA jda = null;

    public static void main(String[] args) {
        String token;
        try {
            token = System.getenv("CAROL_DISCORD_TOKEN");
        } catch (Exception e) {
            System.out.println("Token not found or not provided!");
            throw new RuntimeException(e);
        }

        EnumSet<GatewayIntent> intents = EnumSet.allOf(GatewayIntent.class);

        jda = buildJDA(token, intents);

        SpringApplication.run(CarolBackendApplication.class, args);
    }

    public static JDA buildJDA(String token, EnumSet<GatewayIntent> intents)
    {
        JDABuilder jdaBuilder = JDABuilder.createDefault(token, intents);

        //jdaBuilder.enableCache(CacheFlag.MEMBER_OVERRIDES);
        return jdaBuilder.build();
    }
}