package com.marc.discordbot.carol.listeners.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CarolReadyEventListener extends ListenerAdapter {
    @Override
    public void onReady(ReadyEvent event)
    {
        JDA jda = event.getJDA();

        System.out.println("Bot inited successfully as " + jda.getSelfUser().getName() + " (" + jda.getSelfUser().getId());
        System.out.println("Guilds count: " + jda.getGuilds().size());
        System.out.println("Available guilds: " + event.getGuildAvailableCount());
        System.out.println("Unavailable guilds: " + event.getGuildUnavailableCount());
        System.out.println("Total guilds: " + event.getGuildTotalCount());
        System.out.println("State: " + event.getState());
        System.out.println("Response number: " + event.getResponseNumber());
    }
}
