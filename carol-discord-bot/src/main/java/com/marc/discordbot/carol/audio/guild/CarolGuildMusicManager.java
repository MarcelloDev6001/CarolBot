package com.marc.discordbot.carol.audio.guild;

import com.marc.discordbot.carol.audio.CarolAudioHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class CarolGuildMusicManager {
    public final AudioPlayer player;
    public final CarolAudioHandler handler;

    public CarolGuildMusicManager(AudioPlayerManager manager) {
        this.player = manager.createPlayer();
        this.handler = new CarolAudioHandler(player);
    }
}

