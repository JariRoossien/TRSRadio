package com.trs.radio.music.commands;

import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.music.entity.Song;
import com.trs.radio.music.repository.SongRepository;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class QueueCommand implements SubCommand {

    private final SongRepository repository;

    public QueueCommand(SongRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        if (event.getChannel().getIdLong() != 927663793570447421L) return;
        final String contentRaw = event.getMessage().getContentRaw();
        final String[] s = contentRaw.split(" ");
        String catString = s[1].toUpperCase();
        Song.Category cat = null;
        try {
            cat = Song.Category.valueOf(catString);
        } catch (Exception ignored) {
        }
        if (cat == null) {
            event.getChannel().sendMessage("Unable to find Category: " + catString).queue();
            return;
        }
        String songURL = s[2];
        repository.saveAndFlush(new Song(cat, songURL));
    }
}
