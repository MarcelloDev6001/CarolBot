package com.marc.discordbot.carol.commands;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;

public record CarolBaseCommandOption(
        @NotNull String _name,
        String _description,
        OptionType _type,
        boolean _required,
        boolean _autocomplete
) {
    public String getName() { return _name; }
    public String getDescription() { return _description; }
    public OptionType getOptionType() { return _type; }
    public boolean getRequired() { return _required; }
    public boolean getAutoComplete() { return _autocomplete; }
}
