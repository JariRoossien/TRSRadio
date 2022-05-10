package com.trs.radio.meme.commands;

import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.meme.provider.EzTextProvider;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class EzCommand implements SubCommand {

    EzTextProvider provider;

    public EzCommand(EzTextProvider provider) {
        this.provider = provider;
    }

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        event.getChannel().sendMessage(provider.getNextEz()).queue();
    }
}
