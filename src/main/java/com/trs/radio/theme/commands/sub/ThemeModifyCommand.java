package com.trs.radio.theme.commands.sub;

import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.core.provider.EmbedProvider;
import com.trs.radio.theme.entity.Theme;
import com.trs.radio.theme.repository.ThemeRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ThemeModifyCommand implements SubCommand {

    private final ThemeRepository themeRepository;
    String pattern = "<#([0-9]+)>";
    Pattern r = Pattern.compile(pattern);

    public ThemeModifyCommand(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        EmbedBuilder builder = EmbedProvider.getDefaultBuilder();
        // !theme modify <Theme> <ChannelId> <Name>
        if (args.length < 5) {
            builder.addField("Error", """
                    Not enough arguments!
                                        
                    `!theme modify <Theme> <option> <value>`""", false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        Theme theme = themeRepository.findByName(args[2]).orElse(null);
        if (theme == null) {
            builder.addField("Error", "Unable to find Theme `" + args[2] + "`!", false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        if (args[3].equalsIgnoreCase("name")) {
            String new_theme = args[4];
            if (exists(new_theme, themeRepository.findAll()) && !new_theme.equalsIgnoreCase(theme.getName())) {
                builder.addField("Error", """
                        Can't rename Theme!
                                            
                        New Theme name already exists!""", false);
                event.getChannel().sendMessageEmbeds(builder.build()).queue();
                return;
            }
            theme.setName(new_theme);
            themeRepository.save(theme);
            builder.addField("Success", "Succesfully renamed `" + args[2] + "` to `" + args[4] + "`!", false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }
        if (args[3].equalsIgnoreCase("description")) {
            StringBuilder sb = new StringBuilder();
            for (int i = 4; i < args.length; i++) {
                sb.append(args[i]).append(" ");
            }
            theme.setDescription(sb.toString().strip());
            themeRepository.save(theme);
            builder.addField("Success", "Succesfully renamed the description of`" + args[2] + "`!", false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        Matcher m = r.matcher(args[3]);
        long id;
        if (m.find()) {
            final String group = m.group(1);
            id = getId(group);
        } else {
            id = getId(args[3]);
        }

        if (event.getGuild().getGuildChannelById(id) == null) {
            event.getChannel().sendMessage("Provided ID isn't in this server!").queue();
            builder.addField("Error", "Unable to find channel `" + id + "`!", false);
            event.getChannel().sendMessageEmbeds(builder.build()).queue();
            return;
        }

        Theme.ChannelTheme channelTheme = theme.getChannel(id);
        if (channelTheme == null) {
            channelTheme = new Theme.ChannelTheme(id, theme, args[4]);
            theme.getChannelThemes().add(channelTheme);
        } else {
            channelTheme.setChannelName(args[4]);
        }
        themeRepository.save(theme);

        builder.addField("Success", "Modified `" + args[3] + "` to `" + args[4] + "` for `" + args[2] + "`!", false);
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

    private boolean exists(String name, List<Theme> themeList) {
        for (Theme theme : themeList) {
            if (theme.getName() != null && theme.getName().equalsIgnoreCase(name)) return true;
        }
        return false;
    }

}
