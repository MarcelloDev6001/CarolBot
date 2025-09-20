package com.marc.discordbot.carol.listeners;

import com.marc.discordbot.carol.CarolSettings;
import com.marc.discordbot.carol.database.CarolDatabaseManager;
import com.marc.discordbot.carol.utils.CarolDiscordUtils;
import com.marc.discordbot.carol.utils.CarolNoTextChannelAvalaibleOnGuild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CarolGuildJoinListener extends ListenerAdapter {
    @Override
    public void onGuildJoin(GuildJoinEvent event)
    {
        // why getOrCreateGuild() instead of addGuildToDatabase()?
        // because maybe someone could accidentally remove Carol and added back, and it would try to create a guild, but it would already exist in database.
        // also, this needs to be on the top of the function
        CarolDatabaseManager.getOrCreateGuild(event.getGuild().getIdLong());

        // probably this is a little unnecessary because Carol needs admin permission to be added on a guild
        // so Carol will be available to talk on ANY text channel
        TextChannel availableChannelToTalk;
        try {
            availableChannelToTalk = CarolDiscordUtils.getFirstChannelAvailableToTalkOfAGuild(event.getGuild());
        } catch (CarolNoTextChannelAvalaibleOnGuild exception) {
            return;
        } catch (Exception e) {
            System.out.println("Unknown error: " + e);
            return;
        }

        Member guildOwner = event.getGuild().getOwner();
        availableChannelToTalk.sendMessage(
                (guildOwner != null) ? CarolSettings.DEFAULT_MESSAGE_ON_ADDED_TO_A_NEW_GUILD.replace("${owner}", guildOwner.getAsMention()) :
                        CarolSettings.DEFAULT_MESSAGE_ON_ADDED_TO_A_NEW_GUILD_WITHOUT_OWNER_MENTION
        ).queue();
    }
}
