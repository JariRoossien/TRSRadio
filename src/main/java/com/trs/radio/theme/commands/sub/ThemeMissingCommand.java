package com.trs.radio.theme.commands.sub;

import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.core.provider.EmbedProvider;
import com.trs.radio.theme.entity.Theme;
import com.trs.radio.theme.repository.ThemeRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ThemeMissingCommand implements SubCommand {

    private final ThemeRepository themeRepository;

    public ThemeMissingCommand(ThemeRepository themeRepository) {
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


        Theme theme = themeRepository.findByName(args[2]).orElse(null);
        if (theme == null) {
            builder.addField("Error", "Could not find Theme " + args[2] + ".", false).build();
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("```\n");
        for (GuildChannel channel : event.getGuild().getChannels()) {
            if (channel.getType() == ChannelType.CATEGORY) {
                sb.append("\n").append(channel.getName()).append("\n");
            } else if (channel.getParent() == null) {
                if (theme.getChannel(channel.getIdLong()) == null) {
                    sb.append("# ").append(channel.getName()).append("\n");
                }
            } else {
                if (theme.getChannel(channel.getIdLong()) == null) {
                    sb.append(" # ").append(channel.getName()).append("\n");
                }
            }
        }
        sb.append("```");
        builder.addField("Missing Channels", sb.toString(), false);
        event.getChannel().sendMessageEmbeds(builder.build()).queue();
    }
}
