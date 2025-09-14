package com.marc.discordbot.carol.utils;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

public class CarolDiscordUtils {
    public static TextChannel getFirstChannelAvailableToTalkOfAGuild(Guild guild) throws CarolNoTextChannelAvalaibleOnGuild {
        for (TextChannel channel : guild.getTextChannels())
        {
            if (channel.canTalk())
            {
                return channel;
            }
        }

        // when reach here, probally there's no channel available to talk on this server
        throw new CarolNoTextChannelAvalaibleOnGuild();
    }
}
