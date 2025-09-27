package com.marc.discordbot.carol.listeners.message;

import com.marc.discordbot.carol.database.CarolDatabaseManager;
import com.marc.discordbot.carol.database.entities.guild.CarolDatabaseGuild;
import com.marc.discordbot.carol.experience.CarolExperienceManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CarolMessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        CarolDatabaseGuild dbGuild = CarolDatabaseManager.getOrCreateGuild(event.getGuild().getIdLong());
        if (user.isBot()) { return; }

        String messageContent = event.getMessage().getContentRaw().toLowerCase();
        for (String unauthorizedWord : dbGuild.getUnauthorizedWords())
        {
            if (messageContent.contains(unauthorizedWord))
            {
                event.getMessage().delete().queue();
                event.getChannel().sendMessage(user.getAsMention() + " essa palavra não é legal de se falar, sabia?").queue();
            }
        }

        if (!event.isFromType(ChannelType.PRIVATE))
        {
            try {
                CarolExperienceManager.updateUserXPFromGuild(user, event.getGuild());
            } catch (Exception e) {
                System.out.println("Error on CarolMessageListener: " + e);
            }
        }
    }
}
