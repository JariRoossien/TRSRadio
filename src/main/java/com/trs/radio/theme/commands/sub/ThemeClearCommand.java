package com.trs.radio.theme.commands.sub;

import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.core.provider.EmbedProvider;
import com.trs.radio.theme.entity.Theme;
import com.trs.radio.theme.repository.ThemeRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ThemeClearCommand implements SubCommand {

    private final ThemeRepository themeRepository;

    public ThemeClearCommand(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public void execute(MessageReceivedEvent event, String... args) {
        EmbedBuilder builder = EmbedProvider.getDefaultBuilder();
        if (args.length < 3) {
            builder.addField("Error", "Please provide a theme.", false).build();
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        if (args.length < 4 || getId(args[3]) == -1) {
            builder.addField("Error", "Please provide a valid channel ID.", false).build();
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        long id = getId(args[3]);
        Theme theme = themeRepository.findByName(args[2]).orElse(null);
        if (theme == null) {
            builder.addField("Error", "Could not find Theme " + args[2] + ".", false).build();
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        if (theme.getChannel(id) == null) {
            builder.addField("Error", "Channel `" + id + "` hasn't been set up for `" + args[2] + "`.", false).build();
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        theme.getChannelThemes().remove(theme.getChannel(id));
        themeRepository.save(theme);

        builder.addField("Channel Removed", "Channel has been removed from the Theme", false);
        event.getChannel().sendMessageEmbeds(builder.build()).queue();
    }

    private long getId(String idText) {
        long id = -1;
        try {
            id = Long.parseLong(idText);
        } catch (NumberFormatException ignored) {
        }
        return id;
    }

}
