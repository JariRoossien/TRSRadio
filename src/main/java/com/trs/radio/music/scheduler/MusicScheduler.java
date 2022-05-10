package com.trs.radio.music.scheduler;

import com.trs.radio.core.provider.BotProvider;
import com.trs.radio.music.entity.Song;
import com.trs.radio.music.entity.TrackQueue;
import com.trs.radio.music.handler.LoadAudioResultHandler;
import com.trs.radio.music.manager.TRSAudioManager;
import com.trs.radio.music.repository.SongRepository;
import net.dv8tion.jda.api.entities.StageChannel;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;


@Service
@EnableScheduling
public class MusicScheduler {

    final TRSAudioManager manager;

    final BotProvider provider;

    final TrackQueue trackQueue;

    final SongRepository repository;

    int counter = 1;

    public MusicScheduler(TRSAudioManager manager, BotProvider provider, TrackQueue trackQueue, SongRepository repository) {
        this.manager = manager;
        this.provider = provider;
        this.trackQueue = trackQueue;
        this.repository = repository;
    }

    @Scheduled(fixedDelay = 1000 * 60 * 60, initialDelay = 1000)
    public void loadMusic() {
        Song.Category[] cats = {
                Song.Category.EDM,
                Song.Category.POP,
                Song.Category.ROCK,
                Song.Category.TWOTHOUSANDS,
                Song.Category.HIPHOP,
                Song.Category.CHILL,
                Song.Category.DNB,
        };
        Song.Category category = cats[counter++ % cats.length];
        trackQueue.clear();
        List<Song> songs = repository.findAllByCategoryIs(category);
        Collections.shuffle(songs);
//        manager.getChannel().getManager().setName("TRS Radio - " + category.getDisplay()).complete();
        StageChannel channel = (StageChannel) manager.getChannel();
        if (channel == null) return;
        channel.getStageInstance().getManager().setTopic("TRS Radio - " + category.getDisplay()).complete();
        songs.forEach(song -> {
//            Logger.getLogger(getClass().getName()).info("Loading in " + song.getTitle());
            manager.getManager().loadItem(song.getSongUrl(), new LoadAudioResultHandler(song, manager, trackQueue, song.getSongUrl(), repository));
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }
}
