package com.trs.radio.theme.commands;

import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.theme.commands.sub.*;
import com.trs.radio.theme.repository.ThemeRepository;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class ThemeCommand implements SubCommand {

    Map<String, SubCommand> subCommandMap = new HashMap<>();

    public ThemeCommand(ThemeRepository themeRepository) {
        subCommandMap.put("list", new ThemeListCommand(themeRepository));
        subCommandMap.put("create", new ThemeCreateCommand(themeRepository));
        subCommandMap.put("modify", new ThemeModifyCommand(themeRepository));
        subCommandMap.put("change", new ThemeChangeCommand(themeRepository));
        subCommandMap.put("save", new ThemeSaveCommand(themeRepository));
        subCommandMap.put("preview", new ThemePreviewCommand(themeRepository));
        subCommandMap.put("missing", new ThemeMissingCommand(themeRepository));
        subCommandMap.put("show", new ThemeShowCommand(themeRepository));
        subCommandMap.put("clear", new ThemeClearCommand(themeRepository));
        subCommandMap.put("help", new ThemeHelpCommand());
        subCommandMap.put("clone", new ThemeCloneCommand(themeRepository));
    }

    @Override
    public void execute(MessageReceivedEvent event, String[] args) {
        if (!event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) return;
        String[] commands = event.getMessage().getContentRaw().substring(1).split(" ");
        if (commands.length == 1) {
            subCommandMap.get("help").execute(event, args);
            return;
        }
        if (subCommandMap.containsKey(commands[1].toLowerCase())) {
            subCommandMap.get(commands[1].toLowerCase()).execute(event, args);
        } else {
            subCommandMap.get("help").execute(event, args);
        }
    }
}
