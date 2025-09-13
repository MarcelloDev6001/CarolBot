package com.marc.discordbot.carol;

import com.marc.discordbot.carol.commands.CarolBaseCommandOption;
import com.marc.discordbot.carol.commands.CarolCommand;
import com.marc.discordbot.carol.commands.CarolCommandInitializer;
import com.marc.discordbot.carol.commands.test.CarolTestCommand;
import com.marc.discordbot.carol.commands.utility.CarolIMCCommand;
import com.marc.discordbot.carol.file.JsonUtils;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.marc.discordbot.carol.listeners.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

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

        assert carolProperties != null;
        JDA jda = JDABuilder.createLight(carolProperties.getToken(), intents).build();

        for (ListenerAdapter listener : getAllListeners())
        {
            jda.addEventListener(listener);
        }

        CommandListUpdateAction commands = jda.updateCommands();

        CarolCommandInitializer.initializeCommands();

        for (CarolCommand command : CarolCommand.allCommands)
        {
            SlashCommandData slashCommand = Commands.slash(command.getName(), command.getDescription());
            // WHY THIS IS DEPRECATED???? 😭😭😭😭😭
            // slashCommand.setGuildOnly(command.getGuildOnly());

            if (command.getOptions() != null)
            {
                for (CarolBaseCommandOption optionOnCommand : command.getOptions())
                {
                    slashCommand.addOption(
                            optionOnCommand.getOptionType(),
                            optionOnCommand.getName(),
                            optionOnCommand.getDescription(),
                            optionOnCommand.getRequired()
                    );
                }
            }

            commands.addCommands(slashCommand);
        }

        commands.queue();
    }

    public static List<ListenerAdapter> getAllListeners()
    {
        List<ListenerAdapter> listeners = new ArrayList<>();
        listeners.add(new CarolButtonInteractionListener());
        listeners.add(new CarolGuildMemberJoinListener());
        listeners.add(new CarolGuildMemberRemoveListener());
        listeners.add(new CarolMessageListener());
        listeners.add(new CarolMessageReactionAddListener());
        listeners.add(new CarolSlashCommandInteractionListener());
        return listeners;
    }
}