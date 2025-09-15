package com.marc.discordbot.carol.listeners;

import com.marc.discordbot.carol.experience.CarolExperienceManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CarolMessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();

        if (!user.isBot() && !event.isFromType(ChannelType.PRIVATE))
        {
            try {
                CarolExperienceManager.updateUserXPFromGuild(user, event.getGuild());
            } catch (Exception e) {
                System.out.println("Error on CarolMessageListener: " + e);
            }
        }
    }
}
