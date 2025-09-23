package com.marc.discordbot.carol.commands.test;

import com.marc.discordbot.carol.achievements.CarolAchievementsManager;
import com.marc.discordbot.carol.commands.CarolCommand;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class CarolTestCommand extends CarolCommand {
    public long testAchievementID = 0;
    public CarolTestCommand()
    {
        super("teste", "Apenas um comando de teste");
        setGuildOnly(false);
    }

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction) {
        interaction.reply("Isso é um comando de teste!").queue();

        CarolAchievementsManager.checkAndAwardAchievementToUser(interaction.getUser(), testAchievementID, interaction.getHook());
    }
}
