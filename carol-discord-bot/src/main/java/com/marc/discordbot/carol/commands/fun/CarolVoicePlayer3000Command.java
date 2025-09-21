package com.marc.discordbot.carol.commands.fun;

import com.marc.discordbot.carol.audio.CarolPlayerManager;
import com.marc.discordbot.carol.commands.CarolCommand;
import com.marc.discordbot.carol.messages.components.dropdown.CarolBaseDropdownMenuOption;
import com.marc.discordbot.carol.messages.components.dropdown.CarolStringSelectMenu;
import com.marc.discordbot.carol.voice.CarolVoiceMemeSound;
import com.marc.discordbot.carol.voice.CarolVoiceSoundList;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectInteraction;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CarolVoicePlayer3000Command extends CarolCommand {
    public CarolVoicePlayer3000Command() {
        super("voice-player-3000", "Toque sons únicos na sua call para divertir seus amigos! :D");
    }

    public void onMemeSoundButtonClicked(StringSelectInteraction interaction, User user, List<String> values)
    {
        String soundName = values.getFirst();
        if (soundName == null)
        {
            interaction.reply("Som não encontrado!").setEphemeral(true).queue();
            return;
        }

        GuildVoiceState memberVoiceState;
        try {
            memberVoiceState = interaction.getMember().getVoiceState();
        } catch (Exception e) {
            interaction.reply("Erro desconhecido: " + e).setEphemeral(true).queue();
            return;
        }

        if (memberVoiceState == null || !memberVoiceState.inAudioChannel())
        {
            interaction.reply("Você precisa estar em um canal de voz para tocar um som, bobinho!").setEphemeral(true).queue();
            return;
        }

        AudioChannelUnion voiceChannel = memberVoiceState.getChannel();

        // don't need to verify if getGuild() is null, because we already verify if the command channel is private or not on onCommandExecuted()
        AudioManager audioManager = interaction.getGuild().getAudioManager();
        assert voiceChannel != null;

        // Check if the bot already has an audio connection in this guild
        if (!audioManager.isConnected())
        {
            audioManager.openAudioConnection(voiceChannel);
        }

        if (!Objects.equals(audioManager.getConnectedChannel(), voiceChannel)) { // not in the voice channel
            audioManager.openAudioConnection(voiceChannel);
            interaction.reply("Tocando " + soundName.toLowerCase() + " !").queue();

            CarolPlayerManager.get().loadAndPlay(interaction.getGuild(), "src/main/resources/sounds/memes/" + soundName + ".ogg");
        } else {
            // Already in the voice channel
            interaction.reply("Já estou tocando um som!").setEphemeral(true).queue();
        }
    }

    @Override
    public void onCommandExecuted(SlashCommandInteraction interaction)
    {
        if (interaction.getChannel().getType() == ChannelType.PRIVATE)
        {
            interaction.reply("Esse comando pode ser usado apenas em Servidores!").setEphemeral(true).queue();
            return;
        }

        List<CarolBaseDropdownMenuOption> options = new ArrayList<>();

        for (CarolVoiceMemeSound option : CarolVoiceSoundList.sounds)
        {
            options.add(new CarolBaseDropdownMenuOption(option.name(), option.name().toLowerCase(), option.description(), this::onMemeSoundButtonClicked));
        }

        CarolStringSelectMenu menu = new CarolStringSelectMenu(
                "sound_select_menu_" + interaction.getId(),
                "Escolha um som...",options,1,1);
        interaction.reply("Escolha um dos abaixos:").setActionRow(menu.toSelectMenu()).queue();
    }
}
