package com.marc.discordbot.carol.listeners.guild.channel;

import com.marc.discordbot.carol.database.CarolDatabaseManager;
import com.marc.discordbot.carol.database.entities.guild.CarolDatabaseGuild;
import com.marc.discordbot.carol.database.entities.guild.CarolDatabaseGuildChannelSettings;
import net.dv8tion.jda.api.events.channel.ChannelCreateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CarolGuildChannelCreatedListener extends ListenerAdapter {
    @Override
    public void onChannelCreate(ChannelCreateEvent event) {
        CarolDatabaseGuild dbGuild = CarolDatabaseManager.getOrCreateGuild(event.getGuild().getIdLong());
        dbGuild.addSpecificChannelSetting(new CarolDatabaseGuildChannelSettings(event.getChannel().getIdLong()));
    }
}
