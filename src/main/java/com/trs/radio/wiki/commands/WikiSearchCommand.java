package com.trs.radio.wiki.commands;

import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.wiki.provider.WikiProvider;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WikiSearchCommand implements SubCommand {

    private static final Pattern pattern = Pattern.compile("%20-l%20+([a-zA-Z]{2})\\b");
    final WikiProvider provider;

    public WikiSearchCommand(WikiProvider provider) {
        this.provider = provider;
    }


    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        String message = event.getMessage().getContentStripped();

        int spaceIndex = message.indexOf(' ');
        WikiProvider.Article article;
        if (spaceIndex == -1) {
            article = provider.getWikiPageFrom("");

        } else {
            String rawQuery = message.substring(spaceIndex);
            String query = rawQuery.replace(" ", "%20");
            Matcher matcher = pattern.matcher(query);
            if (matcher.find()) {
                String lang = matcher.group().toLowerCase();
                final String substring = lang.substring(lang.length() - 2);
                article = provider.getWikiPageFrom(query.replaceAll(pattern.pattern(), ""), substring);
            } else {
                article = provider.getWikiPageFrom(query);
            }
        }
        event.getChannel().sendMessageEmbeds(article.toDiscordtoEmbed().build()).queue();
    }
}
