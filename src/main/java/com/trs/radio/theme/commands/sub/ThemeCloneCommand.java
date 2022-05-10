package com.trs.radio.theme.commands.sub;

import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.core.provider.EmbedProvider;
import com.trs.radio.theme.entity.Theme;
import com.trs.radio.theme.repository.ThemeRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class ThemeCloneCommand implements SubCommand {

    private final ThemeRepository themeRepository;

    public ThemeCloneCommand(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public void execute(MessageReceivedEvent event, String... args) {
        EmbedBuilder builder = EmbedProvider.getDefaultBuilder();
        if (args.length < 4) {
            builder.addField("Error", "Not enough arguments! \n\n `!theme clone <old> <new>`", false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }
        if (exists(args[3], themeRepository.findAll())) {
            builder.addField("Error", "Name for new theme already exists!`", false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        Theme old = themeRepository.findByName(args[2]).orElse(null);
        if (old == null) {
            builder.addField("Error", "Given theme does not exist!", false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;

        }

        Theme new_theme = new Theme();
        new_theme.setName(args[3]);
        for (Theme.ChannelTheme theme : old.getChannelThemes()) {
            new_theme.getChannelThemes().add(new Theme.ChannelTheme(theme.getChannelId(), new_theme, theme.getChannelName()));
        }

        themeRepository.save(new_theme);

        builder.addField("Success", "Cloned Theme `" + args[2] + "` into `" + args[3] + "`!", false);
        event.getChannel().sendMessageEmbeds(builder.build()).queue();

    }

    private boolean exists(String name, List<Theme> themeList) {
        for (Theme theme : themeList) {
            if (theme.getName() != null && theme.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

}
