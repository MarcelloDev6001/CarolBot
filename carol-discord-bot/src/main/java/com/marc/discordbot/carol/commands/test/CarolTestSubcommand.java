package com.marc.discordbot.carol.commands.test;

import com.marc.discordbot.carol.commands.CarolCommand;
import com.marc.discordbot.carol.commands.CarolSubcommand;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.jetbrains.annotations.NotNull;

public class CarolTestSubcommand extends CarolSubcommand {
    public CarolTestSubcommand() {
        super("subtest", "Testando sub-comandos!");
    }

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction)
    {
        interaction.reply("teste de subcommand").queue();
    }
}
