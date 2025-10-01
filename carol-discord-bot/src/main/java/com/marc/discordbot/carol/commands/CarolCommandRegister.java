package com.marc.discordbot.carol.commands;

import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.Map;

public class CarolCommandRegister {

    public static void registerCommand(CarolCommand command, CommandListUpdateAction commands) {
        SlashCommandData slashCommand = Commands.slash(command.getName(), command.getDescription());

        addCommandLocalizations(command, slashCommand);

        if (command.getOptions() != null) {
            for (CarolBaseCommandOption optionOnCommand : command.getOptions()) {
                addCommandOptions(command, optionOnCommand, slashCommand);
            }
        }

        System.out.println("Command built (not yet registered): " + slashCommand.getName());

        if (command.getSubcommands() != null) {
            for (CarolSubcommand subcommand : command.getSubcommands()) {
                SubcommandData subData = new SubcommandData(subcommand.subcommandName, subcommand.subcommandDescription);

                addSubcommandLocalizations(subcommand, subData);

                if (subcommand.getOptions() != null) {
                    for (CarolBaseCommandOption optionOnCommand : subcommand.getOptions()) {
                        addCommandOptions(subcommand, optionOnCommand, subData);
                    }
                }

                slashCommand.addSubcommands(subData);

                System.out.println("SubCommand prepared for " + slashCommand.getName() + ": " + subcommand.subcommandName);
            }
        }

        try {
            commands.addCommands(slashCommand);
            System.out.println("Command queued to register: " + slashCommand.getName());
        } catch (IllegalArgumentException ex) {
            System.err.println("fail to add '" + slashCommand.getName() +
                    "' to CommandListUpdateAction: " + ex.getMessage());
        }
    }

    private static void addCommandLocalizations(CarolCommand command, SlashCommandData slashCommand) {
        if (command.getLocaleNameMap() != null) {
            for (Map.Entry<DiscordLocale, String> entry : command.getLocaleNameMap().entrySet()) {
                slashCommand.setNameLocalization(entry.getKey(), entry.getValue());
            }
        }

        if (command.getLocaleDescriptionMap() != null) {
            for (Map.Entry<DiscordLocale, String> entry : command.getLocaleDescriptionMap().entrySet()) {
                slashCommand.setDescriptionLocalization(entry.getKey(), entry.getValue());
            }
        }
    }

    private static void addSubcommandLocalizations(CarolSubcommand subcommand, SubcommandData subData) {
        if (subcommand.getLocaleNameMap() != null) {
            for (Map.Entry<DiscordLocale, String> entry : subcommand.getLocaleNameMap().entrySet()) {
                subData.setNameLocalization(entry.getKey(), entry.getValue());
            }
        }

        if (subcommand.getLocaleDescriptionMap() != null) {
            for (Map.Entry<DiscordLocale, String> entry : subcommand.getLocaleDescriptionMap().entrySet()) {
                subData.setDescriptionLocalization(entry.getKey(), entry.getValue());
            }
        }
    }

    private static void addCommandOptions(CarolCommand command, CarolBaseCommandOption optionOnCommand, SlashCommandData slashCommand) {
        OptionData option = new OptionData(
                optionOnCommand.getOptionType(),
                optionOnCommand.getName(),
                optionOnCommand.getDescription()
        ).setRequired(optionOnCommand.getRequired())
                .setAutoComplete(optionOnCommand.getAutoComplete() != null && !optionOnCommand.getAutoComplete().isEmpty());

        Map<DiscordLocale, String> optionLocaleNameMap = null;
        Map<DiscordLocale, String> optionLocaleDescriptionMap = null;
        if (command.getLocaleOptionsNameMap() != null) {
            optionLocaleNameMap = command.getLocaleOptionsNameMap().get(optionOnCommand.getName());
        }
        if (command.getLocaleOptionsDescriptionMap() != null) {
            optionLocaleDescriptionMap = command.getLocaleOptionsDescriptionMap().get(optionOnCommand.getName());
        }

        if (optionLocaleNameMap != null) {
            for (Map.Entry<DiscordLocale, String> entry : optionLocaleNameMap.entrySet()) {
                option.setNameLocalization(entry.getKey(), entry.getValue());
            }
        }

        if (optionLocaleDescriptionMap != null) {
            for (Map.Entry<DiscordLocale, String> entry : optionLocaleDescriptionMap.entrySet()) {
                option.setDescriptionLocalization(entry.getKey(), entry.getValue());
            }
        }

        slashCommand.addOptions(option);
    }

    private static void addCommandOptions(CarolCommand command, CarolBaseCommandOption optionOnCommand, SubcommandData subData) {
        OptionData option = new OptionData(
                optionOnCommand.getOptionType(),
                optionOnCommand.getName(),
                optionOnCommand.getDescription()
        ).setRequired(optionOnCommand.getRequired())
                .setAutoComplete(optionOnCommand.getAutoComplete() != null && !optionOnCommand.getAutoComplete().isEmpty());

        Map<DiscordLocale, String> optionLocaleNameMap = null;
        Map<DiscordLocale, String> optionLocaleDescriptionMap = null;
        if (command.getLocaleOptionsNameMap() != null) {
            optionLocaleNameMap = command.getLocaleOptionsNameMap().get(optionOnCommand.getName());
        }
        if (command.getLocaleOptionsDescriptionMap() != null) {
            optionLocaleDescriptionMap = command.getLocaleOptionsDescriptionMap().get(optionOnCommand.getName());
        }

        if (optionLocaleNameMap != null) {
            for (Map.Entry<DiscordLocale, String> entry : optionLocaleNameMap.entrySet()) {
                option.setNameLocalization(entry.getKey(), entry.getValue());
            }
        }

        if (optionLocaleDescriptionMap != null) {
            for (Map.Entry<DiscordLocale, String> entry : optionLocaleDescriptionMap.entrySet()) {
                option.setDescriptionLocalization(entry.getKey(), entry.getValue());
            }
        }

        subData.addOptions(option);
    }
}
