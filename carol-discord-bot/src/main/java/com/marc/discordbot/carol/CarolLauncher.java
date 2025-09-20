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

public class CarolLauncher {
    public static void main(String[] args) {
        System.out.println("Initializing Carol...");

        CarolDiscordProperties discordProperties = JsonUtils.getFromFile(CarolDiscordProperties.class, "carol.properties.json");

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

        assert discordProperties != null;
        JDA jda = buildJDA(discordProperties.getToken(), intents);

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

        // TBA: System to change Loritta status every 24 hours.
        initDiscordActivity(jda);

        System.out.println("Carol initialized successfully!");
    }

    public static JDA buildJDA(String token, EnumSet<GatewayIntent> intents)
    {
        switch (CarolSettings.JDA_BUILDER_TYPE)
        {
            case 0 -> { return JDABuilder.createDefault(token, intents).build(); }
            case 2 -> { return JDABuilder.create(token, intents).build(); }
            default -> { return JDABuilder.createLight(token, intents).build(); } // default = light.
        }
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
        listeners.add(new CarolSlashCommandInteractionListener());
        return listeners;
    }
}