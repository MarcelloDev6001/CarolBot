package com.marc.discordbot.carol.audio.listeners;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.entities.Guild;

public class CarolTrackEndListener extends AudioEventAdapter {
    private final Guild guild;

    public CarolTrackEndListener(Guild guild) {
        this.guild = guild;
    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
        // System.out.println("Started: " + track.getInfo().title);
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.mayStartNext) {
            // System.out.println("Ended: " + track.getInfo().title);
            guild.getAudioManager().closeAudioConnection();
        }
    }
}
