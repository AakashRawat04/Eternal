package com.tripsterxx.Eternal.Music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MusicPlayer {
    private static  MusicPlayer INSTANCE;
    private final AudioPlayerManager audioPlayerManager;
    private final Map<Long, GuildMusicManager> musicManagers;

    public MusicPlayer(){
        this.musicManagers = new HashMap<>();

        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild){
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel channel, String trackUrl){
        final GuildMusicManager musicManager = getMusicManager(channel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
//                musicManager.scheduler.queue(audioTrack);
                musicManager.scheduler.queue(audioTrack);

                channel.sendMessage("Adding to queue ** '")
                        .append(audioTrack.getInfo().title)
                        .append("' ** by ** '")
                        .append(audioTrack.getInfo().author)
                        .append("' **")
                        .queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();

                if (!tracks.isEmpty()){
                    musicManager.scheduler.queue(tracks.get(0));
                    channel.sendMessage("Playing ** '")
                            .append(tracks.get(0).getInfo().title)
                            .append("' ** by ** '")
                            .append(tracks.get(0).getInfo().author)
                            .append("' **")
                            .queue();
                }
            }


            @Override
            public void noMatches() {
                channel.sendMessage("Nothing found by " + trackUrl).queue();
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                channel.sendMessage("Could not play: " + exception.getMessage()).queue();
            }
        });
    }

    public static void skipTrack(TextChannel channel){
        GuildMusicManager musicManager = getINSTANCE().getMusicManager(channel.getGuild());
        musicManager.scheduler.nextTrack();
    }

    public static MusicPlayer getINSTANCE(){
        if(INSTANCE == null){
            INSTANCE = new MusicPlayer();
        }
        return INSTANCE;
    }
}
