package com.marc.discordbot.carol.commands.fun;

import com.marc.discordbot.carol.commands.CarolBaseCommandOption;
import com.marc.discordbot.carol.commands.CarolCommand;
import com.marc.discordbot.carol.messages.components.CarolBaseMessageButton;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.ItemComponent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.requests.restaction.WebhookMessageCreateAction;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CarolTicTacToeCommand extends CarolCommand {
    private final Map<String, Message> games = new HashMap<>();

    public CarolTicTacToeCommand() {
        super("tic-tac-toe", "Jogue jogo da idosa com seus amigos! (idosa pq eu tenho respeito e não chamo de velha)");
        setGuildOnly(true);

        addOption(OptionType.USER, "oponente", "A pessoa que você vai desafiar!", true, null);
        addOption(OptionType.STRING, "modo-de-jogo", "O modo se vai ser 3x3, 4x4 ou 5x5", true, List.of("3x3", "4x4", "5x5"));
    }

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction) {
        User opponent = Objects.requireNonNull(interaction.getOption("oponente")).getAsUser();
        String mode = Objects.requireNonNull(interaction.getOption("modo-de-jogo")).getAsString();

        if (opponent.getIdLong() == interaction.getJDA().getSelfUser().getIdLong())
        {
            interaction.reply("Huh... Acha que pode me vencer, é?\nPois saiba que eu não jogo com humanos.").setEphemeral(true).queue();
            return;
        }

        if (opponent.isBot())
        {
            interaction.reply("Você não consegue jogar contra um robô, bobinho!").setEphemeral(true).queue();
            return;
        }

        if (opponent.getId().equals(interaction.getUser().getId()))
        {
            interaction.reply("Você não pode jogar com si mesmo, bobinho!\nMas... O que aconteceria se vc vencesse de si mesmo?").setEphemeral(true).queue();
            return;
        }

        switch (mode) {
            case "3x3" -> makeTicTacToe(interaction, interaction.getUser(), opponent, 3, 3);
            case "4x4" -> makeTicTacToe(interaction, interaction.getUser(), opponent, 4, 4);
            case "5x5" -> makeTicTacToe(interaction, interaction.getUser(), opponent, 5, 5);
            default -> interaction.reply("Modo de jogo inválido!").setEphemeral(true).queue();
        }
    }

    public List<Button> getButtonsFromMessage(Message message) {
        List<Button> buttons = new ArrayList<>();

        for (ActionRow actionRow : message.getActionRows()) {
            for (ItemComponent component : actionRow.getComponents()) {
                if (component instanceof Button button) {
                    buttons.add(button);
                }
            }
        }
        return buttons;
    }

    private String checkWinner(List<ActionRow> rows, int size) {
        String[][] board = new String[size][size];

        for (int r = 0; r < size; r++) {
            List<ItemComponent> components = rows.get(r).getComponents();
            for (int c = 0; c < size; c++) {
                Button btn = (Button) components.get(c);
                board[r][c] = btn.getLabel();
            }
        }

        for (int r = 0; r < size; r++) {
            String first = board[r][0];
            if (!first.equals("⬜")) {
                boolean allEqual = true;
                for (int c = 1; c < size; c++) {
                    if (!board[r][c].equals(first)) {
                        allEqual = false;
                        break;
                    }
                }
                if (allEqual) return first; // X ou O
            }
        }

        for (int c = 0; c < size; c++) {
            String first = board[0][c];
            if (!first.equals("⬜")) {
                boolean allEqual = true;
                for (int r = 1; r < size; r++) {
                    if (!board[r][c].equals(first)) {
                        allEqual = false;
                        break;
                    }
                }
                if (allEqual) return first;
            }
        }

        String firstDiag = board[0][0];
        if (!firstDiag.equals("⬜")) {
            boolean allEqual = true;
            for (int i = 1; i < size; i++) {
                if (!board[i][i].equals(firstDiag)) {
                    allEqual = false;
                    break;
                }
            }
            if (allEqual) return firstDiag;
        }

        String secondDiag = board[0][size - 1];
        if (!secondDiag.equals("⬜")) {
            boolean allEqual = true;
            for (int i = 1; i < size; i++) {
                if (!board[i][size - 1 - i].equals(secondDiag)) {
                    allEqual = false;
                    break;
                }
            }
            if (allEqual) return secondDiag;
        }

        boolean full = true;
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board[r][c].equals("⬜")) {
                    full = false;
                    break;
                }
            }
        }
        if (full) return "draw";

        return "";
    }

    public void makeTicTacToe(SlashCommandInteraction interaction, User player, User opponent, int rowSize, int columnSize) {
        User[] whoPlaysNow = {player};
        String[] messageContent = {"🎮 Batalha de titãs!\n"
                + player.getAsMention() + " VS " + opponent.getAsMention()
                + "\n👉 Vez de " + whoPlaysNow[0].getAsMention()};

        List<ActionRow> rows = new ArrayList<>();

        BiConsumer<ButtonInteraction, User> logMessage = (btnInteraction, user) -> new Runnable() {
            @Override
            public void run()
            {
                String[] rowAndColumn = btnInteraction.getComponentId().split("_");
                String buttonId = rowAndColumn[0] + "_" + rowAndColumn[1] + "_" + interaction.getId();

                List<ActionRow> currentRows = btnInteraction.getMessage().getActionRows();
                List<ActionRow> updatedRows = new ArrayList<>();

                for (ActionRow row : currentRows) {
                    List<Button> updatedButtons = new ArrayList<>();
                    for (ItemComponent comp : row.getComponents()) {
                        if (!(comp instanceof Button btn)) continue;

                        if (btn.getId().equals(buttonId)) {
                            if (whoPlaysNow[0].getIdLong() == interaction.getJDA().getSelfUser().getIdLong()) {
                                btnInteraction.deferReply(true).queue();
                                btnInteraction.getHook().sendMessage("Esse jogo já acabou!").setEphemeral(true).queue();
                                return;
                            }

                            if (!btn.getLabel().equals("⬜")) {
                                btnInteraction.deferReply(true).queue();
                                btnInteraction.getHook().sendMessage("Esse botão já foi usado!").setEphemeral(true).queue();
                                return;
                            }

                            if (whoPlaysNow[0].getIdLong() != user.getIdLong()) {
                                btnInteraction.deferReply(true).queue();
                                btnInteraction.getHook().sendMessage("Não é a sua vez de jogar!").setEphemeral(true).queue();
                                return;
                            }

                            String symbol = (whoPlaysNow[0].equals(player)) ? "X" : "O";
                            updatedButtons.add(
                                    new CarolBaseMessageButton(
                                            ButtonStyle.PRIMARY,
                                            buttonId,
                                            symbol,
                                            null,
                                            ""
                                    ).toComponentButton().withDisabled(true)
                            );

                            whoPlaysNow[0] = (whoPlaysNow[0].equals(player)) ? opponent : player;
                        } else {
                            updatedButtons.add(btn);
                        }
                    }
                    updatedRows.add(ActionRow.of(updatedButtons));
                }

                messageContent[0] = "🎮 Batalha de titãs!\n"
                        + player.getAsMention() + " VS " + opponent.getAsMention()
                        + "\n👉 Vez de " + whoPlaysNow[0].getAsMention();

                btnInteraction.deferReply(true).queue();
                btnInteraction.getHook().sendMessage("Feito!").setEphemeral(true).queue();
                interaction.getHook().editOriginal(messageContent[0]).queue();
                interaction.getHook().editOriginalComponents(updatedRows).queue();

                String winner = checkWinner(updatedRows, rowSize);
                if (!winner.isEmpty())
                {
                    // setting the current player to be the Bot instead of null just to don't need to always put "if (whoPlaysNow[0] == null)"
                    whoPlaysNow[0] = interaction.getJDA().getSelfUser();

                    for (ActionRow actionRow : updatedRows)
                    {
                        for (ItemComponent itemComponent : actionRow.getComponents())
                        {
                            if (!(itemComponent instanceof Button btn)) continue;

                            itemComponent = btn.asDisabled();
                        }
                    }

                    interaction.getHook().editOriginalComponents(updatedRows).queue();

                    switch (winner) {
                        case "X" ->
                                interaction.getHook().sendMessage(player.getAsMention() + " Ganhou o jogo!").setEphemeral(false).queue();
                        case "O" ->
                                interaction.getHook().sendMessage(opponent.getAsMention() + " Ganhou o jogo!").setEphemeral(false).queue();
                        case "draw" -> interaction.getHook().sendMessage("Empate!").setEphemeral(false).queue();
                    }
                }
            };
        }.run();

        for (int row = 0; row < rowSize; row++) {
            List<Button> buttons = new ArrayList<>();
            for (int col = 0; col < columnSize; col++) {
                String id = row + "_" + col + "_" + interaction.getId();
                CarolBaseMessageButton btn = new CarolBaseMessageButton(
                        ButtonStyle.PRIMARY,
                        id,
                        "⬜",
                        logMessage::accept,
                        ""
                );
                buttons.add(btn.toComponentButton());
            }
            rows.add(ActionRow.of(buttons));
        }

        interaction.reply(messageContent[0])
                .addComponents(rows)
                .queue();
    }
}
