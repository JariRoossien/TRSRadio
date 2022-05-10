package com.trs.radio.theme.commands.sub;

import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.core.provider.EmbedProvider;
import com.trs.radio.theme.entity.Theme;
import com.trs.radio.theme.repository.ThemeRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ThemeShowCommand implements SubCommand {

    private final ThemeRepository themeRepository;

    public ThemeShowCommand(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        EmbedBuilder builder = EmbedProvider.getDefaultBuilder();

        // No theme provided
        if (args.length < 3) {
            builder.addField("Error", "No Theme Provided", false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        Theme theme = themeRepository.findByName(args[2]).orElse(null);

        // No theme found
        if (theme == null) {
            builder.addField("Error", "Unable to find Theme `" + args[2] + "`.", false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();

            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("```\n");
        event.getGuild().getChannels().forEach(channel -> {
            if (channel.getParent() == null && channel.getType() != ChannelType.CATEGORY && theme.getChannel(channel.getIdLong()) != null) {
                sb.append("# ").append(getChannelText(theme, channel)).append("\n");
            }
        });
        event.getGuild().getCategories().forEach(cat -> {
            sb.append("\n").append(cat.getName()).append("\n");
            cat.getChannels().forEach(channel -> {
                if (theme.getChannel(channel.getIdLong()) != null)
                    sb.append(" # ").append(getChannelText(theme, channel)).append("\n");
            });
        });
        sb.append("```");

        builder.addField("", sb.toString(), false);
        event.getChannel().sendMessageEmbeds(builder.build()).queue();
    }

    private String getChannelText(Theme theme, GuildChannel channel) {
        if (theme.getChannel(channel.getIdLong()) != null) {
            return theme.getChannel(channel.getIdLong()).getChannelName();
        } else {
            return channel.getName();
        }
    }

}
