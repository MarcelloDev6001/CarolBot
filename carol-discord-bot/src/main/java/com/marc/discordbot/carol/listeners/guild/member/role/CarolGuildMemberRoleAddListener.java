package com.marc.discordbot.carol.listeners.guild.member.role;

import com.marc.discordbot.carol.database.CarolDatabaseManager;
import com.marc.discordbot.carol.database.entities.guild.CarolDatabaseGuild;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class CarolGuildMemberRoleAddListener extends ListenerAdapter {
    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event)
    {
        CarolDatabaseGuild dbGuild = CarolDatabaseManager.getOrCreateGuild(event.getGuild().getIdLong());
        if (!dbGuild.getLogChannelId().isEmpty())
        {
            TextChannel logChannel = event.getGuild().getTextChannelById(dbGuild.getLogChannelId());
            if (logChannel != null && dbGuild.isLogRoleAddedToUser())
            {
                EmbedBuilder roleAddedEmbed = getEmbedBuilder(event);
                logChannel.sendMessage("").setEmbeds(roleAddedEmbed.build()).queue();
            }
        }
    }

    @NotNull
    private static EmbedBuilder getEmbedBuilder(GuildMemberRoleAddEvent event) {
        EmbedBuilder roleAddedEmbed = new EmbedBuilder();
        roleAddedEmbed.setTitle("Cargo(s) adicionado(s) para " + event.getUser().getName());
        StringBuilder messageDescription = new StringBuilder();
        for (Role roleAdded : event.getRoles())
        {
            messageDescription.append(roleAdded.getAsMention());
        }
        roleAddedEmbed.setColor(Color.GREEN);
        roleAddedEmbed.setDescription(messageDescription);
        return roleAddedEmbed;
    }
}
