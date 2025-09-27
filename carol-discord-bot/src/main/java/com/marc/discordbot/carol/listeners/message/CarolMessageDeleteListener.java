package com.marc.discordbot.carol.listeners.message;

import com.marc.discordbot.carol.database.CarolDatabaseManager;
import com.marc.discordbot.carol.database.entities.guild.CarolDatabaseGuild;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CarolMessageDeleteListener extends ListenerAdapter {
    @Override
    public void onMessageDelete(MessageDeleteEvent event)
    {
        // funfact: I was going to use this and CarolMessageUpdateListener to log messages edited and deleted
        // but for some reason you can't access the old content of an edited message and a deleted message ;-;
    }
}
