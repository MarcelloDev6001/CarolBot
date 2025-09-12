package com.marc.discordbot.carol.commands;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public record CarolBaseCommand(
        @NotNull String name,
        String description,
        @Nullable CarolBaseCommandOption[] options,
        boolean guildOnly
)
{
    public String getName() { return name; }
    public String getDescription() { return description; }
    @Nullable public CarolBaseCommandOption[] getOptions() { return options; }
    public boolean getGuildOnly() { return guildOnly; }
}

