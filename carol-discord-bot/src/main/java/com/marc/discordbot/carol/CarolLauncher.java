package com.marc.discordbot.carol;

import com.marc.discordbot.carol.activity.CarolActivitiesManager;
import com.marc.discordbot.carol.cache.CarolCacheManager;
import com.marc.discordbot.carol.commands.CarolBaseCommandOption;
import com.marc.discordbot.carol.commands.CarolCommand;
import com.marc.discordbot.carol.commands.CarolCommandInitializer;
import com.marc.discordbot.carol.database.CarolDatabaseManager;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import com.marc.discordbot.carol.listeners.CarolListenersLoader;
import com.marc.discordbot.carol.listeners.guild.*;
import com.marc.discordbot.carol.listeners.interaction.*;
import com.marc.discordbot.carol.listeners.message.*;
import com.marc.discordbot.carol.listeners.message.components.*;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class CarolLauncher {
    public static void main(String[] args) {
        System.out.println("Initializing Carol...");

        String token;
        try {
            token = System.getenv("CAROL_DISCORD_TOKEN");
        } catch (Exception e) {
            System.out.println("Token not found or not provided!");
            throw new RuntimeException(e);
        }

        JDA jda = buildJDA(token, EnumSet.allOf(GatewayIntent.class));

        CommandListUpdateAction commands = jda.updateCommands();

        CarolCommandInitializer.initializeCommands();
        try {
            CarolDatabaseManager.initialize();
        } catch (Exception e) {
            System.out.println("Failed to initialize database: " + e);
        }

        CarolCacheManager.initialize();
        CarolListenersLoader.initListeners(jda);

        for (CarolCommand command : CarolCommand.allCommands)
        {
            SlashCommandData slashCommand = Commands.slash(command.getName(), command.getDescription());
            // WHY THIS IS DEPRECATED???? 😭😭😭😭😭
            // slashCommand.setGuildOnly(command.getGuildOnly());

            for (Map.Entry<DiscordLocale, String> entry : command.getLocaleNameMap().entrySet()) {
                slashCommand.setNameLocalization(entry.getKey(), entry.getValue());
            }

            for (Map.Entry<DiscordLocale, String> entry : command.getLocaleDescriptionMap().entrySet()) {
                slashCommand.setDescriptionLocalization(entry.getKey(), entry.getValue());
            }

            if (command.getOptions() != null)
            {
                for (CarolBaseCommandOption optionOnCommand : command.getOptions())
                {
                    SlashCommandData newOption = slashCommand.addOption(
                            optionOnCommand.getOptionType(),
                            optionOnCommand.getName(),
                            optionOnCommand.getDescription(),
                            optionOnCommand.getRequired(),
                            optionOnCommand.getAutoComplete() != null && !optionOnCommand.getAutoComplete().isEmpty()
                    );

                    Map<DiscordLocale, String> optionLocaleNameMap = command.getLocaleOptionsNameMap().getOrDefault(
                            optionOnCommand.getName(), null);
                    Map<DiscordLocale, String> optionLocaleDescriptionMap = command.getLocaleOptionsDescriptionMap().getOrDefault(
                            optionOnCommand.getName(), null);

                    if (optionLocaleNameMap != null)
                    {
                        for (Map.Entry<DiscordLocale, String> entry : optionLocaleNameMap.entrySet()) {
                            slashCommand.setNameLocalization(entry.getKey(), entry.getValue());
                        }

                        for (Map.Entry<DiscordLocale, String> entry : optionLocaleDescriptionMap.entrySet()) {
                            slashCommand.setDescriptionLocalization(entry.getKey(), entry.getValue());
                        }
                    }
                }
            }

            commands.addCommands(slashCommand);
            System.out.println("Command Loaded to Discord: " + slashCommand.getName());
        }

        commands.queue();

        CarolActivitiesManager.initDiscordActivity(jda);

        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            System.out.println("Error on initialize Carol: " + e);
            return;
        }
        System.out.println("Carol initialized successfully!");
    }

    public static JDA buildJDA(String token, EnumSet<GatewayIntent> intents)
    {
        JDABuilder jdaBuilder;
        switch (CarolSettings.JDA_BUILDER_TYPE)
        {
            case 0 -> { jdaBuilder = JDABuilder.createDefault(token, intents); }
            case 2 -> { jdaBuilder = JDABuilder.create(token, intents); }
            default -> { jdaBuilder = JDABuilder.createLight(token, intents); } // default = light.
        }

        // probably i'll not use all of those CacheFlags... but yeah, who cares? :3
        jdaBuilder.enableCache(
                CacheFlag.ACTIVITY,
                CacheFlag.CLIENT_STATUS,
                CacheFlag.EMOJI,
                CacheFlag.MEMBER_OVERRIDES,
                CacheFlag.ROLE_TAGS,
                CacheFlag.VOICE_STATE,
                CacheFlag.ONLINE_STATUS,
                CacheFlag.SCHEDULED_EVENTS
        );
        return jdaBuilder.build();
    }
}