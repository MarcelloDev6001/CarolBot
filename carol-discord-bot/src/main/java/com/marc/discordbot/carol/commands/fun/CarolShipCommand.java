package com.marc.discordbot.carol.commands.fun;

import com.marc.discordbot.carol.commands.CarolCommand;
import com.marc.discordbot.carol.database.CarolDatabaseManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public class CarolShipCommand extends CarolCommand {
    // we need better ship command messages...
    public String[] shipMessages = new String[]{
            "Nem tentem.", // 0%
            "Acho que esse não vai dar tãaaaao certo assim, infelizmente :sob:", // 1-15%
            "Casal ruim, não compensa tanto tentar", // 16-40%
            "Casal médio, acredito que vai dar certo", // 40-60%
            "Tentem... Acredito que esse casal irá dar certo", // 60-75%
            "Será que temos a nova Dama e o \"Vagueador\" aqui? hehehe...", // 75-90%
            "Que casal mais lindo, tão lindo quanto a mim :melting_face: ", // 90-96%
            "POR FAVOR ME CONVIDEM PARA O CASAMENTO DE VOCÊS!!! :sob: ", // 97-100%
            "Nah, eu recuso, prefiro a vida de solteiro :3 ", // when you ship you with Carol
            "Você deve ter amor próprio, sabia?", // when you ship you with yourself
            "Você (e essa pessoa) devem ter amor próprio, sabia?", // when you ship someone with himself
            "Meh, não precisa de ship... Afinal, eles já são casados!", // when you ship two peoples married
            "Não sei como um bot vai aceitar seu pedido de namoro, mas ok...\n-# Você tem gostos peculiares, viu..."
    };

    public String baseMessage = "Hmmm, será que temos um novo casal aqui?\n" +
            "Vamos ver o que o Cupido tem a dizer...\n" +
            "\n" +
            ":heart: `USER_1` + `USER_2` = `NAMES_COMBINATION`! (SHIP_PERCENTAGE) :heart:\n" +
            "SHIP_MESSAGE";

    public CarolShipCommand() {
        super("ship", "Shippe você (ou qualquer outra pessoa) com alguém!");
        setGuildOnly(true);

        addOption(OptionType.USER, "user1", "O marido (ou esposa)", false, null);
        addOption(OptionType.USER, "user2", "A esposa (ou marido)", false, null);
    }

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction)
    {
        interaction.deferReply(false).queue();
        User husband;
        try {
            husband = interaction.getOption("user1").getAsUser();
        } catch (NullPointerException nullPointerException) { // null pointer exception most of the cases will be thrown because option is optional
            // then we use the own user as husband if we don't find the user
            husband = interaction.getUser();
        }

        if (interaction.getOption("user2") == null) {
            husband = interaction.getUser();
        }

        User wife;
        try {
            OptionMapping user2 = interaction.getOption("user2");
            OptionMapping user1 = interaction.getOption("user1");

            wife = user2 != null ? user2.getAsUser() : user1.getAsUser();
        } catch (NullPointerException nullPointerException) {
            // if not found the user2, we find a random user from the guild
            List<Member> members = interaction.getGuild().getMembers();

            Random random = new Random();
            int randomIndex = random.nextInt(members.size());
            wife = members.get(randomIndex).getUser();
        }

        // probably I will do a better random system for this command
        long seed = husband.getIdLong() + wife.getIdLong();
        Random random = new Random(seed);
        int shipPercentage = random.nextInt(0, 100);

        String namesCombination = "";
        namesCombination += husband.getName().substring(0, husband.getName().length() / 2);
        namesCombination += wife.getName().substring(wife.getName().length() / 2, wife.getName().length());

        String husbandSpouseID = CarolDatabaseManager.getOrCreateUser(husband.getIdLong()).getSpouseID();
        String wifeSpouseID = CarolDatabaseManager.getOrCreateUser(wife.getIdLong()).getSpouseID();

        String shipMessage = "";
        if (wife == interaction.getJDA().getSelfUser()) // shipping yourself with Carol
        {
            shipMessage = shipMessages[8];
        } else if (husband == interaction.getUser() && wife == interaction.getUser()) { // shipping you with yourself
            shipPercentage = 100;
            shipMessage = shipMessages[9];
        } else if (husband == wife) { // shipping someone with himself
            shipPercentage = 100;
            shipMessage = shipMessages[10];
        } else if(husband.isBot() || wife.isBot()) { // shipping someone with a bot
            shipMessage = shipMessages[12];
        } else if(husbandSpouseID.equals(wife.getId()) && wifeSpouseID.equals(husband.getId())) { // married
            shipPercentage = 100;
            shipMessage = shipMessages[11];
        } else if (shipPercentage > 97) {
            shipMessage = shipMessages[7];
        } else if (shipPercentage > 90) {
            shipMessage = shipMessages[6];
        } else if (shipPercentage > 75) {
            shipMessage = shipMessages[5];
        } else if (shipPercentage > 60) {
            shipMessage = shipMessages[4];
        } else if (shipPercentage > 40) {
            shipMessage = shipMessages[3];
        } else if (shipPercentage > 16) {
            shipMessage = shipMessages[2];
        } else if (shipPercentage > 0) {
            shipMessage = shipMessages[1];
        } else if (shipPercentage == 0) {
            shipMessage = shipMessages[0];
        }

        // TODO: make a system to generate an image with those two "lovebirds"
        String messageContent = baseMessage
                .replace("USER_1", husband.getName())
                .replace("USER_2", wife.getName())
                .replace("NAMES_COMBINATION", namesCombination)
                .replace("SHIP_PERCENTAGE", Integer.toString(shipPercentage))
                .replace("SHIP_MESSAGE", shipMessage);

        interaction.getHook().editOriginal(messageContent).queue();
    }
}
