package com.trs.radio.music.manager;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import com.trs.radio.core.provider.BotProvider;
import com.trs.radio.music.entity.TrackQueue;
import com.trs.radio.music.handler.AudioPlayerSendHandler;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Service;

@Service
public class TRSAudioManager extends AudioEventAdapter {

    final AudioPlayer player;
    final VoiceChannel channel;
    final long TRS_CHANNEL_ID = 929041514779271188L;
    final long TEST_CHANNEL_ID = 927797412205068358L;
    final long CHANNEL_ID = TRS_CHANNEL_ID;
    AudioPlayerManager manager;
    BotProvider provider = null;
    TrackQueue trackQueue = null;
    private boolean looping = false;

    public TRSAudioManager(BotProvider provider, TrackQueue trackQueue) {
        manager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerLocalSource(manager);
        AudioSourceManagers.registerRemoteSources(manager);
        player = manager.createPlayer();

        channel = provider.getJda().getVoiceChannelById(CHANNEL_ID);
        if (channel == null) return;
        AudioManager guildManager = channel.getGuild().getAudioManager();

        if (!guildManager.isConnected()) {
            guildManager.openAudioConnection(channel);
            guildManager.setSendingHandler(getSendHandler());
        }


        player.addListener(this);
        this.trackQueue = trackQueue;
        looping = true;
        this.provider = provider;
    }

    public AudioSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }

    public AudioPlayerManager getManager() {
        return manager;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (track.getInfo().title.equalsIgnoreCase("TRSRadio")) return;
        if (looping) {
            trackQueue.addSong(track.makeClone());
        }
        final AudioTrack audioTrack = trackQueue.popNextSong();
        player.playTrack(audioTrack);

        provider.getJda().getPresence().setActivity(Activity.listening(track.getInfo().author + " - " + track.getInfo().title));
    }

    public BotProvider getProvider() {
        return provider;
    }

    public VoiceChannel getChannel() {
        return channel;
    }


}
