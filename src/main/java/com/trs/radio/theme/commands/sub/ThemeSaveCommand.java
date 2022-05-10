package com.trs.radio.theme.commands.sub;

import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.core.provider.EmbedProvider;
import com.trs.radio.theme.entity.Theme;
import com.trs.radio.theme.repository.ThemeRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

public class ThemeSaveCommand implements SubCommand {

    private final ThemeRepository themeRepository;

    public ThemeSaveCommand(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        EmbedBuilder builder = EmbedProvider.getDefaultBuilder();
        if (args.length < 3) {
            builder.addField("Error", "No theme name provided!", false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        if (exists(args[2], themeRepository.findAll())) {
            builder.addField("Error", "Theme `" + args[2] + "` already exists!", false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        Theme newTheme = new Theme();
        for (GuildChannel channel : event.getGuild().getChannels()) {
            newTheme.getChannelThemes().add(new Theme.ChannelTheme(channel.getIdLong(), newTheme, channel.getName()));
        }

        newTheme.setName(args[2]);
        themeRepository.save(newTheme);
        builder.addField("Success", "Succesfully saved the current theme to `" + args[2] + "`!", false);
        event.getChannel().sendMessageEmbeds(builder.build()).queue();

    }

    private boolean exists(String name, List<Theme> themeList) {
        for (Theme theme : themeList) {
            if (theme.getName() != null && theme.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }
}
