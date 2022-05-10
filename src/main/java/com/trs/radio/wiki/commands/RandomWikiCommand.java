package com.trs.radio.wiki.commands;

import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.wiki.provider.WikiProvider;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class RandomWikiCommand implements SubCommand {

    WikiProvider provider;

    public RandomWikiCommand(WikiProvider provider) {
        this.provider = provider;
    }

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        WikiProvider.Article article = provider.getRandomWikiPage();
        event.getChannel().sendMessageEmbeds(article.toDiscordtoEmbed().build()).queue();
    }
}
