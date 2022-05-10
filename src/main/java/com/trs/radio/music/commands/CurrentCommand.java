package com.trs.radio.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.core.provider.EmbedProvider;
import com.trs.radio.music.entity.TrackQueue;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;


public class CurrentCommand implements SubCommand {

    private final TrackQueue queue;

    public CurrentCommand(TrackQueue queue) {
        this.queue = queue;
    }

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        final AudioTrackInfo info = queue.getPlayedTracks().get(0).getInfo();
        event.getChannel().sendMessageEmbeds(
                new EmbedBuilder(EmbedProvider.getDefaultBuilder())
                        .addField("Currently Playing",
                                info.author + " - " + info.title + "\n" + info.uri,
                                false)
                        .setThumbnail(String.format("https://img.youtube.com/vi/%s/0.jpg", info.identifier))
                        .build()
        ).queue();
    }
}
