package com.trs.radio.music.entity;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class TrackQueue {

    Queue<AudioTrack> tracks;
    LinkedList<AudioTrack> playedTracks;

    public TrackQueue() {
        tracks = new LinkedBlockingQueue<>();
        playedTracks = new LinkedList<>();
    }

    public void addSong(AudioTrack track) {
        tracks.add(track);
    }

    public AudioTrack popNextSong() {
        if (tracks.peek() != null) {
            final AudioTrack poll = tracks.poll();
            playedTracks.addFirst(poll);
            return poll;
        }
        return null;
    }

    public LinkedList<AudioTrack> getPlayedTracks() {
        return playedTracks;
    }

    public void clear() {
        tracks.clear();
    }
}
