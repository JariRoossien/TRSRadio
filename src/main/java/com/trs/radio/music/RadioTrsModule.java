package com.trs.radio.music;

import com.trs.radio.core.TrsModule;
import com.trs.radio.core.provider.BotProvider;
import com.trs.radio.core.repository.UserRepository;
import com.trs.radio.music.commands.CurrentCommand;
import com.trs.radio.music.commands.QueueCommand;
import com.trs.radio.music.commands.RecentCommand;
import com.trs.radio.music.entity.TrackQueue;
import com.trs.radio.music.listener.VoiceListener;
import com.trs.radio.music.repository.SongRepository;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class RadioTrsModule extends TrsModule {

    private final BotProvider botProvider;
    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final TrackQueue queue;

    public RadioTrsModule(BotProvider botProvider, UserRepository userRepository, SongRepository songRepository, TrackQueue queue) {
        super();
        this.botProvider = botProvider;
        this.userRepository = userRepository;
        this.songRepository = songRepository;
        this.queue = queue;

        this.setup();
    }

    public void setup() {

        botProvider.getCommandListener().registerCommand("recent", new RecentCommand(queue));

        final CurrentCommand current = new CurrentCommand(queue);
        botProvider.getCommandListener().registerCommand("current", current);
        botProvider.getCommandListener().registerCommand("curr", current);
        botProvider.getCommandListener().registerCommand("q", new QueueCommand(songRepository));
//        botProvider.getCommandListener().registerCommand("favorite", new FavoriteCommand(userRepository, songRepository, queue));

        botProvider.getJda().addEventListener(new VoiceListener());
    }

    @PreDestroy
    public void destroy() {
        botProvider.getJda().getAudioManagers().forEach(AudioManager::closeAudioConnection);
    }

}
