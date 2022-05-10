package com.trs.radio.theme.commands.sub;

import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.core.provider.EmbedProvider;
import com.trs.radio.theme.repository.ThemeRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ThemeListCommand implements SubCommand {

    private final ThemeRepository themeRepository;

    public ThemeListCommand(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        EmbedBuilder builder = EmbedProvider.getDefaultBuilder();
        StringBuilder sb = new StringBuilder();
        sb.append("```\n");
        themeRepository.findAll().forEach(theme -> {
            sb.append(theme.getName()).append(" - ").append(theme.getDescription()).append("\n");
        });
        sb.append("```");
        builder.addField("Themes", sb.toString(), false);
        event.getChannel().sendMessageEmbeds(builder.build()).queue();
    }
}
