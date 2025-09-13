package com.marc.discordbot.carol.commands.utility;

import com.marc.discordbot.carol.commands.CarolBaseCommandOption;
import com.marc.discordbot.carol.commands.CarolCommand;
import com.marc.discordbot.carol.messages.components.CarolBaseMessageButton;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.text.DecimalFormat;

public class CarolIMCCommand extends CarolCommand {
    public CarolIMCCommand() {
        super("calculadora-de-imc", "Veja o seu IMC e confira se vc está abaixo, igual ou acima do peso!", new CarolBaseCommandOption[]{
                new CarolBaseCommandOption("peso", "O seu peso (em KG)", OptionType.NUMBER, true, false),
                new CarolBaseCommandOption("altura", "A sua altura (em CM)", OptionType.NUMBER, true, false)
        }, false);
    }

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction)
    {
        double weight = 0;
        try {
            weight = interaction.getOption("peso").getAsDouble();
        } catch (Exception e) {
            interaction.reply("Peso não encontrado!").setEphemeral(true).queue();
            return;
        }

        double height = 0;
        try {
            height = interaction.getOption("altura").getAsDouble() / 100;
        } catch (Exception e) {
            interaction.reply("Altura não encontrada!").setEphemeral(true).queue();
            return;
        }

        String finalContent = getIMCFinalMessage(weight, height);
        Button imcTableButton = new CarolBaseMessageButton(
                ButtonStyle.PRIMARY,
                "imc_table_" + interaction.getId(),
                "Ver tabela de IMC",
                this::onIMCTableButtonClicked,
                null
        ).toComponentButton();

        interaction.reply(finalContent).addActionRow(imcTableButton).setEphemeral(true).queue();
    }

    @NotNull
    private static String getIMCFinalMessage(double weight, double height) {
        double IMC = weight / (height * height);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        String finalContent = "Seu IMC: " + decimalFormat.format(IMC);

        if (IMC < 18.5) {
            finalContent += "\n\nClassificação: Abaixo do peso.";
        } else if (IMC < 25) {
            finalContent += "\n\nClassificação: Peso normal.";
        } else if (IMC < 30) {
            finalContent += "\n\nClassificação: Sobrepeso.";
        } else if (IMC < 35) {
            finalContent += "\n\nClassificação: Obesidade grau I.";
        } else if (IMC < 40) {
            finalContent += "\n\nClassificação: Obesidade grau II.";
        } else {
            finalContent += "\n\nClassificação: Obesidade grau III.";
        }
        return finalContent;
    }

    public void onIMCTableButtonClicked(ButtonInteraction interaction)
    {
        String imcTableString = "Classificações:" +
                "\n" +
                "\nMenor que 18.5: Abaixo do peso." +
                "\nEntre 18.5 e 25: Peso normal." +
                "\nEntre 25 e 30: Sobrepeso." +
                "\nEntre 30 e 35: Obesidade grau I." +
                "\nEntre 35 e 40: Obesidade grau II." +
                "\nMaior que 40: Obesidade grau III.";

        interaction.deferReply(true).queue();
        interaction.getHook().sendMessage(imcTableString).setEphemeral(true).queue();
    }
}
