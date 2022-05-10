package com.trs.radio.music.handler;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.trs.radio.music.entity.Song;
import com.trs.radio.music.entity.TrackQueue;
import com.trs.radio.music.manager.TRSAudioManager;
import com.trs.radio.music.repository.SongRepository;
import net.dv8tion.jda.api.entities.Activity;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class will handle the result found from given lookup string.
 */
public class LoadAudioResultHandler implements AudioLoadResultHandler {

    private final TrackQueue queue;
    private final String lookup;
    private final TRSAudioManager manager;
    private final SongRepository repository;
    private final Song song;

    public LoadAudioResultHandler(Song song, TRSAudioManager manager, TrackQueue queue, String lookup, SongRepository repository) {
        this.manager = manager;
        this.queue = queue;
        this.lookup = lookup;
        this.repository = repository;
        this.song = song;
    }

    @Override
    public void trackLoaded(AudioTrack audioTrack) {

        if (song.getTitle() == null || song.getTitle().equals("")) {
            song.setTitle(audioTrack.getInfo().title);
            song.setSinger(audioTrack.getInfo().author);
            song.setDuration(audioTrack.getInfo().length);
            repository.saveAndFlush(song);
        }
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing.
        // If something is playing, it returns false and does nothing.
        if (!manager.getPlayer().startTrack(audioTrack, true)) {
            queue.addSong(audioTrack.makeClone());
        } else {
            manager.getProvider().getJda().getPresence().setActivity(Activity.listening(song.getSinger() + " - " + song.getTitle()));
            queue.getPlayedTracks().add(audioTrack);
        }
    }

    @Override
    public void playlistLoaded(AudioPlaylist audioPlaylist) {
        for (AudioTrack track : audioPlaylist.getTracks()) {
            trackLoaded(track);
        }
    }

    @Override
    public void noMatches() {
        Logger.getLogger("TRS Radio").warning("Can't find any song for: " + lookup);
    }

    @Override
    public void loadFailed(FriendlyException e) {

        Logger.getLogger(getClass().getName()).log(Level.FINEST, "Youtube blocked " + lookup);

    }
}
