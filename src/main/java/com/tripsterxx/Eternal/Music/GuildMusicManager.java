package com.tripsterxx.Eternal.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

/**
 * Holder for both the player and a track scheduler for one guild.
 */
public class GuildMusicManager {
    // Audio player for the guild
    public final AudioPlayer player;
    //Track scheduler for the player
    public final TrackScheduler scheduler;

    private final AudioPlayerSendHandler sendHandler;

    public GuildMusicManager(AudioPlayerManager manager){
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
        sendHandler = new AudioPlayerSendHandler(player);
    }

    public AudioPlayerSendHandler getSendHandler(){
        return this.sendHandler;
    }
}
