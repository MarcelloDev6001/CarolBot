package com.marc.discordbot.carol;

import com.marc.discordbot.carol.file.JsonUtils;

import java.io.File;
import java.io.IOException; // Required for handling potential exceptions
import java.util.Collections;
import java.util.EnumSet;

import com.marc.discordbot.carol.listeners.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class CarolLauncher {
    public static void main(String[] args) {
        System.out.println("Initing Commands...");

        CarolProperties carolProperties = JsonUtils.getFromFile(CarolProperties.class, "carol.properties.json");

        EnumSet<GatewayIntent> intents = EnumSet.of(
                // Enables MessageReceivedEvent for guild (also known as servers)
                GatewayIntent.GUILD_MESSAGES,
                // Enables the event for private channels (also known as direct messages)
                GatewayIntent.DIRECT_MESSAGES,
                // Enables access to message.getContentRaw()
                GatewayIntent.MESSAGE_CONTENT,
                // Enables MessageReactionAddEvent for guild
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                // Enables MessageReactionAddEvent for private channels
                GatewayIntent.DIRECT_MESSAGE_REACTIONS
        );

        JDA jda = JDABuilder.createLight(carolProperties.getToken(), intents)
                .addEventListeners(new CarolGuildMemberJoinListener())
                .addEventListeners(new CarolGuildMemberRemoveListener())
                .addEventListeners(new CarolMessageListener())
                .addEventListeners(new CarolMessageReactionAddListener())
                .addEventListeners(new CarolSlashCommandInteractionListener())
                .build();
    }
}