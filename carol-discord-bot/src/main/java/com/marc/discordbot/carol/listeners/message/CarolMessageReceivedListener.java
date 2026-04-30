package com.marc.discordbot.carol.listeners.message;

import com.marc.discordbot.carol.database.CarolDatabaseManager;
import com.marc.discordbot.carol.database.entities.guild.CarolDatabaseGuild;
import com.marc.discordbot.carol.database.entities.guild.CarolDatabaseGuildChannelSettings;
import com.marc.discordbot.carol.experience.CarolExperienceManager;
import com.marc.discordbot.carol.moderation.CarolModerationAntiLinkManager;
import com.marc.discordbot.carol.moderation.CarolModerationAntiSpamManager;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.TimeUnit;

public class CarolMessageReceivedListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        User user = event.getAuthor();
        CarolDatabaseGuild dbGuild = null;
        try {
            dbGuild = CarolDatabaseManager.getOrCreateGuild(event.getGuild().getIdLong());
        } catch (IllegalStateException _) { return;} // in most cases, this exception will occur when you send a message to the bot on his DM

        if (user.isBot()) { return; }

        String messageContent = event.getMessage().getContentRaw().toLowerCase();
        for (String unauthorizedWord : dbGuild.getUnauthorizedWords()) {
            if (messageContent.contains(unauthorizedWord)) {
                event.getMessage().delete().queue();
                event.getChannel().sendMessage(user.getAsMention() + " essa palavra não é legal de se falar, sabia?").queue();
                return;
            }
        }

        if (!event.isFromType(ChannelType.PRIVATE))
        {
            try {
                CarolExperienceManager.updateUserXPFromGuild(user, event.getGuild());
            } catch (Exception e) {
                System.out.println("Error on CarolMessageListener: " + e);
            }

            CarolDatabaseGuildChannelSettings channelSettings = null;
            try {
                channelSettings = dbGuild.getSpecificChannelSettingFromId(event.getChannel().getIdLong());
            } catch (NullPointerException _) {
                channelSettings = new CarolDatabaseGuildChannelSettings(0);
            }

            if (CarolModerationAntiLinkManager.messageContainsLink(event.getMessage().getContentRaw()) && !channelSettings.isLinksAllowed())
            {
                event.getMessage().delete().queue();
                event.getChannel().sendMessage(user.getAsMention() + " Não é permitido links aqui, bobinho!").queue();
            }
        }

        //listenForSpam(event, dbGuild);
    }

    private static void listenForSpam(MessageReceivedEvent event, CarolDatabaseGuild dbGuild) {
        CarolModerationAntiSpamManager.startListenerForMessage(event.getMessage(), dbGuild.getSpamMaxSecondsToVerify());

        if (dbGuild.getSpamTimeoutTime() > 0 &&
                CarolModerationAntiSpamManager.isUserSpamming(event.getMember(), dbGuild.getSpamMaxMessagesPerSecond()))
        {
            try {
                event.getMember().timeoutFor(dbGuild.getSpamTimeoutTime(), TimeUnit.SECONDS).queue();
            } catch (Exception _) {}

            event.getChannel().sendMessage(event.getMember().getAsMention() + " sem spammar por aqui, boboca!").queue();

            // not doing this for now because can cause lag on bot
            // CarolSpamMessageManager.deleteUserSpammedMessages(event.getMember());
        }
    }
}
