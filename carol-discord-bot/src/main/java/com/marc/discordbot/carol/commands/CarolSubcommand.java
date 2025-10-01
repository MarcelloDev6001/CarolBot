package com.marc.discordbot.carol.commands;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class CarolSubcommand extends CarolCommand {
    private CarolCommand parent = null;

    public String subcommandName;
    public String subcommandDescription;
    public @Nullable List<CarolBaseCommandOption> subcommandOptions;
    public boolean subcommandGuildOnly;

    public CarolSubcommand(@NotNull String name, String description) {
        super(name, description, true);
        subcommandName = name;
        subcommandDescription = description;
    }

    public CarolCommand getParent() {
        return parent;
    }

    public void setParent(CarolCommand parent) {
        this.parent = parent;
    }
}
