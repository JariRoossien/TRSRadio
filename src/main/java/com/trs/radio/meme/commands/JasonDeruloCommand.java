package com.trs.radio.meme.commands;

import com.trs.radio.core.commands.SubCommand;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class JasonDeruloCommand implements SubCommand {

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        event.getChannel().sendMessage("\uD835\uDCD9\uD835\uDCEA\uD835\uDCFC\uD835\uDCF8\uD835\uDCF7 \uD835\uDCD3\uD835\uDCEE\uD835\uDCFB\uD835\uDCFE\uD835\uDCF5\uD835\uDCF8").queue();
    }
}
