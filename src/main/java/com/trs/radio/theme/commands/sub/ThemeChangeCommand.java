package com.trs.radio.theme.commands.sub;

import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.core.provider.EmbedProvider;
import com.trs.radio.theme.entity.Theme;
import com.trs.radio.theme.repository.ThemeRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ThemeChangeCommand implements SubCommand {

    private final ThemeRepository themeRepository;

    public ThemeChangeCommand(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }


    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        EmbedBuilder builder = EmbedProvider.getDefaultBuilder();
        if (args.length < 3) {
            builder.addField("Error", "No theme provided!", false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        Theme theme = themeRepository.findByName(args[2]).orElse(null);
        if (theme == null) {
            builder.addField("Error", "Unable to find Theme `" + args[2] + "`!", false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        theme.getChannelThemes().forEach(channelTheme -> {
            GuildChannel channel = event.getGuild().getGuildChannelById(channelTheme.getChannelId());
            if (channel != null) {
                channel.getManager().setName(channelTheme.getChannelName()).queue();
            }
        });
        builder.addField("Success", "Succesfully changed the theme to `" + args[2] + "`!", false);
        event.getChannel().sendMessageEmbeds(builder.build()).queue();

    }
}
