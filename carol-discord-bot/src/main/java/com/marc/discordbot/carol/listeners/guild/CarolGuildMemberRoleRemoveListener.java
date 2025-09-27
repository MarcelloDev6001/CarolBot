package com.marc.discordbot.carol.listeners.guild;

import com.marc.discordbot.carol.database.CarolDatabaseManager;
import com.marc.discordbot.carol.database.entities.guild.CarolDatabaseGuild;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CarolGuildMemberRoleRemoveListener extends ListenerAdapter {
    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event)
    {
        CarolDatabaseGuild dbGuild = CarolDatabaseManager.getOrCreateGuild(event.getGuild().getIdLong());
        if (!dbGuild.getLogMessageId().isEmpty())
        {
            TextChannel logChannel = event.getGuild().getChannelById(TextChannel.class, dbGuild.getLogMessageId());
            if (logChannel != null)
            {
                EmbedBuilder roleRemovedEmbed = getEmbedBuilder(event);
                logChannel.sendMessage("").setEmbeds(roleRemovedEmbed.build()).queue();
            }
        }
    }

    @NotNull
    private static EmbedBuilder getEmbedBuilder(GuildMemberRoleRemoveEvent event) {
        EmbedBuilder roleRemovedEmbed = new EmbedBuilder();
        roleRemovedEmbed.setTitle("Cargo(s) removidos(s) de " + event.getUser().getName());
        StringBuilder messageDescription = new StringBuilder();
        for (Role roleRemoved : event.getRoles())
        {
            messageDescription.append(roleRemoved.getAsMention());
        }
        roleRemovedEmbed.setColor(Color.RED);
        roleRemovedEmbed.setDescription(messageDescription);
        return roleRemovedEmbed;
    }
}
