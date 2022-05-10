package com.trs.radio.core.commands;

import com.trs.radio.core.provider.EmbedProvider;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CommandListener extends ListenerAdapter {


    Map<String, SubCommand> subCommandMap = new HashMap<>();

    public CommandListener() {
    }

    public void registerCommand(String command, SubCommand subCommand) {
        subCommandMap.put(command, subCommand);
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getMember().getIdLong() == event.getJDA().getSelfUser().getIdLong()) return;
        final String contentRaw = event.getMessage().getContentRaw();
        if (!contentRaw.startsWith("!")) return;

        // Takes the first word and removes !, so "!test abc" -> "test"
        String command = contentRaw.split(" ")[0].replace("!", "");
        String[] args = event.getMessage().getContentRaw().substring(1).split(" ");
        if (subCommandMap.containsKey(command.toLowerCase())) {
            subCommandMap.get(command.toLowerCase()).execute(event, args);
        } else {
            event.getChannel().sendMessageEmbeds(
                    EmbedProvider.getDefaultBuilder().addField("Unknown Command", "Could not find the given command!", false).build()
            ).queue();
        }
    }


}
