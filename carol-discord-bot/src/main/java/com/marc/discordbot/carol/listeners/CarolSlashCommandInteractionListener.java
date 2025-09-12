package com.marc.discordbot.carol.listeners;

import com.marc.discordbot.carol.commands.CarolCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class CarolSlashCommandInteractionListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        switch (event.getName()) {
            default: {
                CarolCommand.dispatchInteraction(event.getInteraction());
                break;
            }
        }
    }
}
