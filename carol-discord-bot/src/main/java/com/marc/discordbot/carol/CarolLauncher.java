package com.marc.discordbot.carol;

import com.marc.discordbot.carol.commands.CarolBaseCommandOption;
import com.marc.discordbot.carol.commands.CarolCommand;
import com.marc.discordbot.carol.commands.CarolCommandInitializer;
import com.marc.discordbot.carol.database.CarolDatabaseManager;
import com.marc.discordbot.carol.file.JsonUtils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import com.marc.discordbot.carol.listeners.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.managers.Presence;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class CarolLauncher {
    public static void main(String[] args) {
        System.out.println("Initializing Carol...");

        EnumSet<GatewayIntent> intents = EnumSet.of(
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.DIRECT_MESSAGES,
                GatewayIntent.MESSAGE_CONTENT,
                GatewayIntent.GUILD_MESSAGE_REACTIONS,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_VOICE_STATES
        );

        String token;
        try {
            token = System.getenv("CAROL_DISCORD_TOKEN");
        } catch (Exception e) {
            System.out.println("Token not found or not provided!");
            throw new RuntimeException(e);
        }
        JDA jda = buildJDA(token, intents);

        for (ListenerAdapter listener : getAllListeners())
        {
            jda.addEventListener(listener);
        }

        CommandListUpdateAction commands = jda.updateCommands();

        CarolCommandInitializer.initializeCommands();
        try {
            CarolDatabaseManager.initialize();
        } catch (Exception e) {
            System.out.println("Failed to initialize database: " + e);
        }


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

        // TBA: System to change Carol status every 24 hours.
        initDiscordActivity(jda);

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

        jdaBuilder.enableCache(CacheFlag.VOICE_STATE);
        return jdaBuilder.build();
    }

    public static void initDiscordActivity(JDA jda)
    {
        Presence jdaPresences = jda.getPresence();

        Activity activity;
        switch (CarolSettings.ACTIVITY_TYPE)
        {
            case PLAYING -> activity = Activity.playing(CarolSettings.ACTIVITY_INFO);
            case LISTENING -> activity = Activity.listening(CarolSettings.ACTIVITY_INFO);
            case WATCHING -> activity = Activity.watching(CarolSettings.ACTIVITY_INFO);
            case COMPETING -> activity = Activity.competing(CarolSettings.ACTIVITY_INFO);
            case STREAMING -> activity = Activity.streaming(CarolSettings.ACTIVITY_INFO, "");
            default -> activity = Activity.customStatus(CarolSettings.ACTIVITY_INFO); // default = ActivityType.CUSTOM_STATUS
        }

        jdaPresences.setPresence(CarolSettings.ONLINE_STATUS, activity);
    }

    public static List<ListenerAdapter> getAllListeners()
    {
        List<ListenerAdapter> listeners = new ArrayList<>();
        listeners.add(new CarolButtonInteractionListener());
        listeners.add(new CarolCommandAutoCompleteInteractionListener());
        listeners.add(new CarolGuildJoinListener());
        listeners.add(new CarolGuildMemberJoinListener());
        listeners.add(new CarolGuildMemberRemoveListener());
        listeners.add(new CarolMessageListener());
        listeners.add(new CarolMessageReactionAddListener());
        listeners.add(new CarolOnStringSelectInteractionListener());
        listeners.add(new CarolSlashCommandInteractionListener());
        return listeners;
    }
}