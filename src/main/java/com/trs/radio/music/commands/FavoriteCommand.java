package com.trs.radio.music.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.core.entity.TRSUser;
import com.trs.radio.core.provider.EmbedProvider;
import com.trs.radio.core.repository.UserRepository;
import com.trs.radio.music.entity.Song;
import com.trs.radio.music.entity.TrackQueue;
import com.trs.radio.music.repository.SongRepository;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class FavoriteCommand implements SubCommand {

    private final UserRepository userRepository;
    private final SongRepository songRepository;
    private final TrackQueue queue;

    public FavoriteCommand(UserRepository userRepository, SongRepository songRepository, TrackQueue queue) {
        this.userRepository = userRepository;
        this.songRepository = songRepository;
        this.queue = queue;
    }

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        final long userId = event.getAuthor().getIdLong();
        TRSUser user = userRepository.findById(userId).orElse(new TRSUser(userId));
        final AudioTrackInfo info = queue.getPlayedTracks().get(0).getInfo();
        Song currentSong = songRepository.findByTitle(info.title).orElse(null);

        if (currentSong == null) {
            event.getChannel().sendMessageEmbeds(
                    EmbedProvider
                            .getDefaultBuilder()
                            .addField("Error", "Unable to find " + info.title, false)
                            .build()
            ).queue();
        }
//        if (user.getFavoriteSongs().contains(currentSong)) {
//            user.getFavoriteSongs().remove(currentSong);
//            event.getChannel().sendMessageEmbeds(
//                    EmbedProvider
//                            .getDefaultBuilder()
//                            .addField("Removed from favorites", info.title + " has been removed from your favorites!", false)
//                            .build()
//            ).queue();
//
//        } else {
//            user.getFavoriteSongs().add(currentSong);
//            event.getChannel().sendMessageEmbeds(
//                    EmbedProvider
//                            .getDefaultBuilder()
//                            .addField("Added to favorites", info.title + " has been added to your favorites!", false)
//                            .build()
//            ).queue();
//        }
        userRepository.saveAndFlush(user);
    }
}
