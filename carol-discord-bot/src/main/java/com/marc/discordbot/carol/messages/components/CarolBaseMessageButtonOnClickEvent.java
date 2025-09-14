package com.marc.discordbot.carol.messages.components;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;

@FunctionalInterface
public interface CarolBaseMessageButtonOnClickEvent { // it's a long name, isn't?
    void run(ButtonInteraction interaction, User user);
}
