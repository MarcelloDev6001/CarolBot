package com.marc.discordbot.carol.audio;

import com.marc.discordbot.carol.audio.guild.CarolGuildMusicManager;
import com.marc.discordbot.carol.audio.listeners.CarolTrackEndListener;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import net.dv8tion.jda.api.entities.Guild;

import java.util.HashMap;
import java.util.Map;

public class CarolPlayerManager {
    private static CarolPlayerManager INSTANCE;

    private final AudioPlayerManager playerManager;
    private final Map<Long, CarolGuildMusicManager> musicManagers;

    private CarolPlayerManager() {
        this.playerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(playerManager);
        AudioSourceManagers.registerLocalSource(playerManager);
        this.musicManagers = new HashMap<>();
    }

    public static synchronized CarolPlayerManager get() {
        if (INSTANCE == null) {
            INSTANCE = new CarolPlayerManager();
        }
        return INSTANCE;
    }

    private synchronized CarolGuildMusicManager getGuildMusicManager(Guild guild) {
        return musicManagers.computeIfAbsent(guild.getIdLong(), id -> {
            CarolGuildMusicManager manager = new CarolGuildMusicManager(playerManager);
            manager.player.addListener(new CarolTrackEndListener(guild));
            guild.getAudioManager().setSendingHandler(manager.handler);
            return manager;
        });
    }

    public void loadAndPlay(Guild guild, String path) {
        CarolGuildMusicManager musicManager = getGuildMusicManager(guild);

        playerManager.loadItem(path, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.player.playTrack(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                musicManager.player.playTrack(playlist.getTracks().getFirst());
            }

            @Override
            public void noMatches() {
                System.out.println("Nenhuma faixa encontrada para " + path);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                exception.printStackTrace();
            }
        });
    }
}

