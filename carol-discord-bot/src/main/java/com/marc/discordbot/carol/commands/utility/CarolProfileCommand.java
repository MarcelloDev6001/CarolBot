package com.marc.discordbot.carol.commands.utility;

import com.marc.discordbot.carol.commands.CarolCommand;
import com.marc.discordbot.carol.database.CarolDatabaseManager;
import com.marc.discordbot.carol.database.entities.user.CarolDatabaseUser;
import com.marc.discordbot.carol.economy.CarolEconomyManager;
import com.marc.discordbot.carol.experience.CarolExperienceManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.interactions.DiscordLocale;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.sql.Timestamp;
import java.util.Map;

public class CarolProfileCommand extends CarolCommand {
    public CarolProfileCommand() {
        super("profile", "Veja o lindo perfil de alguém! (ou o seu próprio perfil)");
        setGuildOnly(false);

        addOption(OptionType.USER, "pessoa", "A pessoa que você vai ver o perfil dela!", false, null);
    }

    public int getGlobalXPOfUser(User user)
    {
        CarolDatabaseUser dbUser = CarolDatabaseManager.getOrCreateUser(user.getIdLong());
        if (dbUser == null) { dbUser = CarolDatabaseUser.getDefault(user.getIdLong()); }
        Map<String, Integer> guildsXPs = dbUser.getXpInGuilds();

        int globalXP = 0;
        for (Map.Entry<String, Integer> entry : guildsXPs.entrySet()) {
            globalXP += entry.getValue();
        }
        return globalXP;
    }

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction)
    {
        List<MessageEmbed> embeds = new ArrayList<>();

        User userToSeeProfile = interaction.getUser();
        Member userAsMemberToSeeProfile = interaction.getMember();
        try {
            userToSeeProfile = interaction.getOption("pessoa").getAsUser();
            userAsMemberToSeeProfile = interaction.getOption("pessoa").getAsMember();
        } catch (Exception _) {}
        User.Profile userProfile = userToSeeProfile.retrieveProfile().complete();

        assert userAsMemberToSeeProfile != null;
        EmbedBuilder userProfileEmbed = new EmbedBuilder();
        userProfileEmbed.setAuthor("Informações sobre o usuário");
        userProfileEmbed.setTitle("Perfil de " + userToSeeProfile.getName());
        if (userToSeeProfile.getAvatar() != null)
        {
            userProfileEmbed.setThumbnail(userToSeeProfile.getAvatarUrl());
        }
        if (userProfile.getBanner() != null)
        {
            userProfileEmbed.setImage(userProfile.getBanner().getUrl(1024));
        }
        userProfileEmbed.addField("ID do Discord", userToSeeProfile.getId(), true);
        userProfileEmbed.addField("Tag do Discord", userToSeeProfile.getAsTag(), true);

        long userTimeCreatedTimestamp = userToSeeProfile.getTimeCreated().toInstant().toEpochMilli() / 1000;

        userProfileEmbed.addField("Data de Criação da Conta",
                "<t:" + userTimeCreatedTimestamp + ":f> (<t:" + userTimeCreatedTimestamp + ":R>)", false);
        userProfileEmbed.addField("Moedas", Integer.toString(CarolEconomyManager.getUserMoney(userToSeeProfile)), false);
        userProfileEmbed.addField("XP Global", Integer.toString(getGlobalXPOfUser(userToSeeProfile)), false);
        if (userProfile.getAccentColor() != null) {
            userProfileEmbed.setColor(userProfile.getAccentColor());
        } else {
            userProfileEmbed.setColor(new Color(0x292b2f));
        }
        embeds.add(userProfileEmbed.build());

        if (interaction.getChannel().getType() != ChannelType.PRIVATE) {
            EmbedBuilder memberProfileEmbed = new EmbedBuilder();
            if (userToSeeProfile.getAvatar() != null)
            {
                memberProfileEmbed.setThumbnail(userToSeeProfile.getAvatarUrl());
            }
            memberProfileEmbed.setAuthor("Informações sobre o membro");
            memberProfileEmbed.setTitle("Perfil de " + userAsMemberToSeeProfile.getEffectiveName());

            long userTimeJoinedTimestamp = userAsMemberToSeeProfile.getTimeJoined().toInstant().toEpochMilli() / 1000;

            memberProfileEmbed.addField("Data de Entrada no Servidor",
                    "<t:" + userTimeJoinedTimestamp + ":f> (<t:" + userTimeJoinedTimestamp + ":R>)", true);
            if (!userAsMemberToSeeProfile.getRoles().isEmpty()) {
                memberProfileEmbed.addField("Maior cargo", userAsMemberToSeeProfile.getRoles().getFirst().getAsMention(), true);
                memberProfileEmbed.setColor(userAsMemberToSeeProfile.getRoles().getFirst().getColor());
            } else {
                memberProfileEmbed.setColor(new Color(0x292b2f));
            }
            memberProfileEmbed.addField("XP (em " + interaction.getGuild().getName() + ")",
                    Integer.toString(CarolExperienceManager.getUserXPFromGuild(userToSeeProfile, interaction.getGuild())),
                    true);

            embeds.add(memberProfileEmbed.build());
        }

        ReplyCallbackAction replyCallbackAction = interaction.reply("").setEphemeral(true);
        for (MessageEmbed embed : embeds)
        {
            replyCallbackAction.addEmbeds(embed);
        }
        replyCallbackAction.queue();
    }
}
