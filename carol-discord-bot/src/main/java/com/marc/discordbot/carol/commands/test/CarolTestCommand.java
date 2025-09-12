package com.marc.discordbot.carol.commands.test;

import com.marc.discordbot.carol.commands.CarolCommand;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class CarolTestCommand extends CarolCommand {
    public CarolTestCommand()
    {
        super("teste", "Apenas um comando de teste", null, false);
    }

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction) {
        interaction.reply("Isso é um comando de teste!").queue();
    }
}
