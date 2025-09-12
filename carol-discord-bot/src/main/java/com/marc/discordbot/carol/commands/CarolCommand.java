package com.marc.discordbot.carol.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public abstract class CarolCommand {
    public String name;
    public String description;
    public @Nullable CarolBaseCommandOption[] options;
    public boolean guildOnly;

    private static final List<CarolCommand> allCommands = new ArrayList<>();
    private static SlashCommandInteraction interaction;

    public CarolCommand(@NotNull String name, String description, @Nullable CarolBaseCommandOption[] options, boolean guildOnly) {
        this.name = name;
        this.description = description;
        this.options = options;
        this.guildOnly = guildOnly;

        allCommands.add(this);
    }

    public static void dispatchInteraction(SlashCommandInteraction interaction) {
        System.out.println(allCommands.size());
        for (CarolCommand cmd : allCommands) {
            if (cmd.getName().equals(interaction.getName())) {
                cmd.setInteraction(interaction);
                cmd.onCommandExecuted(interaction);
                break;
            }
        }
    }

    public void setInteraction(SlashCommandInteraction newInteraction)
    {
        interaction = newInteraction;
    }

    public void onCommandExecuted(SlashCommandInteraction interaction) {
        // System.out.println("Command executed: " + interaction.getName());
    }

    public String getName() { return name; }
    public String description() { return description; }
    @Nullable public CarolBaseCommandOption[] getOptions() { return options; }
    public boolean getGuildOnly() { return guildOnly; }
}
