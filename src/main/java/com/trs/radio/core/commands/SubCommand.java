package com.trs.radio.core.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public interface SubCommand {

    void execute(MessageReceivedEvent event, String... args);
}
