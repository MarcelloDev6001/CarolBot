package com.marc.discordbot.carol.listeners.interaction;

import com.marc.discordbot.carol.commands.CarolCommand;
import com.marc.discordbot.carol.commands.CarolSubcommand;
import com.marc.discordbot.carol.database.CarolDatabaseManager;
import com.marc.discordbot.carol.database.entities.user.CarolDatabaseUser;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CarolSlashCommandInteractionListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event)
    {
        CarolDatabaseUser dbUser = CarolDatabaseManager.getOrCreateUser(event.getUser().getIdLong());

        String subcommandName = event.getSubcommandName();
        if (subcommandName != null)
        {
            try {
                CarolSubcommand subcommand = CarolCommand.getSubcommandByName(event.getName(), subcommandName);
                subcommand.onCommandExecuted(event.getInteraction());
                return;
            } catch (NullPointerException _) {}
        }
        CarolCommand.dispatchInteraction(event.getInteraction());
        dbUser.setAmountOfCommandsUsed(dbUser.getAmountOfCommandsUsed() + 1);
    }
}
