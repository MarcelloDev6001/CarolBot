package com.marc.discordbot.carol.commands.administration;

import com.marc.discordbot.carol.CarolSettings;
import com.marc.discordbot.carol.commands.CarolCommand;
import com.marc.discordbot.carol.messages.components.button.CarolBaseMessageButton;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.jetbrains.annotations.NotNull;

public class CarolDashboardCommand extends CarolCommand {
    public CarolDashboardCommand() {
        super("dashboard", "Entre no site de dashboard e configure seus servidores por lá");
    }

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction)
    {
        CarolBaseMessageButton dashboardButton = new CarolBaseMessageButton(
                ButtonStyle.LINK,
                "dashboard_" + interaction.getId(),
                "Acessar Dashboard",
                null,
                CarolSettings.DASHBOARD_URL
        );

        interaction.reply("").setEphemeral(true).setActionRow(dashboardButton.toComponentButton()).queue();
    }
}
