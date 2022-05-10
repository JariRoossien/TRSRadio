package com.trs.radio.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.core.provider.EmbedProvider;
import com.trs.radio.music.entity.TrackQueue;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Iterator;
import java.util.LinkedList;

public class RecentCommand implements SubCommand {

    private final TrackQueue queue;

    public RecentCommand(TrackQueue queue) {
        this.queue = queue;
    }

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        final LinkedList<AudioTrack> info = queue.getPlayedTracks();
        StringBuilder builder = new StringBuilder();
        Iterator<AudioTrack> iterator = info.iterator();
        builder.append("\n");
        for (int i = 0; i < 10 && iterator.hasNext(); i++) {
            AudioTrack next = iterator.next();
            builder.append(
                    String.format("%d. [%s - %s](%s)\n", i + 1, next.getInfo().author, next.getInfo().title, next.getInfo().uri)
            );
        }
        event.getChannel().sendMessageEmbeds(
                new EmbedBuilder(EmbedProvider.getDefaultBuilder())
                        .addField("Recently Played",
                                builder.toString(),
                                false)
                        .build()
        ).queue();

    }
}
