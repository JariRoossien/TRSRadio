package com.trs.radio.music.listener;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.StageChannel;
import net.dv8tion.jda.api.entities.StageInstance;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Logger;

public class VoiceListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent event) {
        if (event.getChannelJoined().getType() != ChannelType.STAGE) return;
        if (event.getMember().getIdLong() != 887026153389424670L) return;
        Logger.getLogger(getClass().getName()).info("Trying to connect to channel...");
        StageChannel channel = (StageChannel) event.getChannelJoined();

        Logger.getLogger(getClass().getName()).info("Is Moderator: " + channel.isModerator(event.getMember()));
        StageInstance stageInstance = channel.getStageInstance();
        if (stageInstance == null) {
            stageInstance = channel.createStageInstance("TRS Radio").complete();
        }
        stageInstance.requestToSpeak().queue();

    }


}
