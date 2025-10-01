package com.marc.discordbot.carol.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class CarolCommand {
    public String name;
    public String description;
    public @Nullable List<CarolBaseCommandOption> options;
    public boolean guildOnly;

    public static final List<CarolCommand> allCommands = new ArrayList<>();
    private static SlashCommandInteraction interaction;

    private Map<DiscordLocale, String> localeNameMap;
    private Map<DiscordLocale, String> localeDescriptionMap;

    private Map<String, Map<DiscordLocale, String>> localeOptionsNameMap; // map of a map... i think this is rare to occur
    private Map<String, Map<DiscordLocale, String>> localeOptionsDescriptionMap;

    private List<CarolSubcommand> subcommands;

    @NotNull
    public static CarolCommand getCommandByName(String name) throws NullPointerException
    {
        for (CarolCommand command : allCommands)
        {
            if (command.getName().equals(name))
            {
                return command;
            }
        }
        throw new NullPointerException("Command " + name + " not found!");
    }

    @NotNull
    public static CarolSubcommand getSubcommandByName(String name, String subcommandName) throws NullPointerException
    {
        for (CarolCommand command : allCommands)
        {
            if (command.getName().equals(name))
            {
                for (CarolSubcommand subcommand : command.getSubcommands())
                {
                    if (subcommand.subcommandName.equals(subcommandName))
                    {
                        return subcommand;
                    }
                }
            }
        }
        throw new NullPointerException("Subcommand " + subcommandName + " not found!");
    }

    public void addSubcommand(CarolSubcommand subcommand)
    {
        subcommand.setParent(this);
        subcommands.add(subcommand);
    }

    public CarolCommand(@NotNull String name, String description) {
        this.name = name;
        this.description = description;
        options = new ArrayList<>();
        localeNameMap = new HashMap<>();
        localeDescriptionMap = new HashMap<>();
        localeOptionsNameMap = new HashMap<>();
        localeOptionsDescriptionMap = new HashMap<>();
        subcommands = new ArrayList<>();

        allCommands.add(this);
    }

    public CarolCommand(@NotNull String name, String description, boolean isSubCommand) {
        this.name = name;
        this.description = description;
        options = new ArrayList<>();
        localeNameMap = new HashMap<>();
        localeDescriptionMap = new HashMap<>();
        localeOptionsNameMap = new HashMap<>();
        localeOptionsDescriptionMap = new HashMap<>();
        subcommands = new ArrayList<>();

        if (!isSubCommand) {
            allCommands.add(this);
        }
    }

    public static void dispatchInteraction(SlashCommandInteraction interaction) {
        // System.out.println(allCommands.size());
        for (CarolCommand cmd : allCommands) {
            if (cmd.getName().equals(interaction.getName())) {
                cmd.setInteraction(interaction);
                cmd.onCommandExecuted(interaction);
                break;
            }
        }
    }

    public Map<DiscordLocale, String> getLocaleNameMap() { return localeNameMap; }
    public Map<DiscordLocale, String> getLocaleDescriptionMap() { return localeDescriptionMap; }
    public Map<String, Map<DiscordLocale, String>> getLocaleOptionsNameMap() { return localeOptionsNameMap; }
    public Map<String, Map<DiscordLocale, String>> getLocaleOptionsDescriptionMap() { return localeOptionsDescriptionMap; }

    public void addTranslation(DiscordLocale language, String name, String description) {
        localeNameMap.put(language, name);
        localeDescriptionMap.put(language, description);
    }

    public void addTranslationToOption(String optionName, DiscordLocale language, String name, String description) {
        if (localeOptionsNameMap.getOrDefault(optionName, null) == null) { localeOptionsNameMap.put(optionName, new HashMap<>()); }
        localeOptionsNameMap.get(optionName).put(language, name);

        if (localeOptionsDescriptionMap.getOrDefault(optionName, null) == null) { localeOptionsDescriptionMap.put(optionName, new HashMap<>()); }
        localeOptionsDescriptionMap.get(optionName).put(language, description);
    }

    public void setInteraction(SlashCommandInteraction newInteraction)
    {
        interaction = newInteraction;
    }

    public void onCommandExecuted(SlashCommandInteraction interaction) {
        // System.out.println("Command executed: " + interaction.getName());
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    @Nullable public List<CarolBaseCommandOption> getOptions() { return options; }
    public boolean getGuildOnly() { return guildOnly; }

    public void setGuildOnly(boolean guildOnly) { this.guildOnly = guildOnly; }

    public void addOption(OptionType optionType, String name, String description, boolean required, List<String> autoComplete)
    {
        assert options != null;
        options.add(new CarolBaseCommandOption(name, description, optionType, required, autoComplete));
    }

    // for OptionType.STRING
    public void addOption(OptionType optionType, String name, String description, boolean required, List<String> autoComplete, int minLength, int maxLength)
    {
        assert options != null;
        options.add(new CarolBaseCommandOption(name, description, optionType, required, autoComplete));
    }

    public void addOption(CarolBaseCommandOption option)
    {
        assert options != null;
        options.add(option);
    }

    public List<CarolSubcommand> getSubcommands() {
        return subcommands;
    }

    public void setSubcommands(List<CarolSubcommand> subcommands) {
        this.subcommands = subcommands;
    }
}
