package com.marc.discordbot.carol.commands.achievements;

import com.marc.discordbot.carol.achievements.CarolAchievementObject;
import com.marc.discordbot.carol.achievements.CarolAchievementsManager;
import com.marc.discordbot.carol.commands.CarolCommand;
import com.marc.discordbot.carol.messages.components.dropdown.CarolBaseDropdownMenuOption;
import com.marc.discordbot.carol.messages.components.dropdown.CarolStringSelectMenu;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectInteraction;

import java.util.ArrayList;
import java.util.List;

public class CarolSeeAchievementsCommand extends CarolCommand {
    public CarolSeeAchievementsCommand()
    {
        super("ver-conquistas", "Veja as suas conquistas adquiridas!");
    }

    public void onAchievementButtonClicked(StringSelectInteraction interaction, User user, List<String> values)
    {
        interaction.deferReply(true).queue();
        long id = 0L;
        try {
            long num = Long.parseLong(values.getFirst());
            // System.out.println("ID: " + num);
        } catch (NumberFormatException e) {
            interaction.getHook().sendMessage("ID de achievement inválido!").setEphemeral(true).queue();
            return;
        } catch (Exception e) {
            interaction.getHook().sendMessage("Erro desconhecido: " + e).setEphemeral(true).queue();
            return;
        }

        CarolAchievementObject achievementObject = CarolAchievementsManager.getAchievementFromID(id);
        if (achievementObject == null)
        {
            interaction.getHook().sendMessage("Conquista não encontrada!").setEphemeral(true).queue();
            return;
        }

        EmbedBuilder achievementEmbed = new EmbedBuilder();
        achievementEmbed.setTitle("Conquista: " + achievementObject.name);
        achievementEmbed.setDescription(achievementObject.description);
        achievementEmbed.addField("Requisito (s)", achievementObject.requirement, false);

        interaction.getHook().editOriginal("").setEmbeds(achievementEmbed.build()).queue();
    }

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction)
    {
        List<String> achievementsNames = new ArrayList<>();

        List<CarolBaseDropdownMenuOption> options = new ArrayList<>();

        for (CarolAchievementObject option : CarolAchievementsManager.getAchievementsObjectsFromUser(interaction.getUser()))
        {
            options.add(
                    new CarolBaseDropdownMenuOption(option.name, Long.toString(option.id), "Veja sobre a conquista " + option.name, this::onAchievementButtonClicked)
            );
        }

        CarolStringSelectMenu achievementsMenu = new CarolStringSelectMenu(
                "achievements_menu_" + interaction.getId(),
                "Escolha uma conquista...", options,1,1);

        interaction.reply("Suas conquistas:").setActionRow(achievementsMenu.toSelectMenu()).queue();
    }
}
