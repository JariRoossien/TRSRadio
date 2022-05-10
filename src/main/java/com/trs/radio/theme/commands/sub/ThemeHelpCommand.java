package com.trs.radio.theme.commands.sub;

import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.core.provider.EmbedProvider;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class ThemeHelpCommand implements SubCommand {

    @Override
    public void execute(MessageReceivedEvent event, String... args) {

//        subCommandMap.put("list", new ThemeListCommand(themeRepository));
//        subCommandMap.put("create", new ThemeCreateCommand(themeRepository));
//        subCommandMap.put("modify", new ThemeModifyCommand(themeRepository));
//        subCommandMap.put("change", new ThemeChangeCommand(themeRepository));
//        subCommandMap.put("save", new ThemeSaveCommand(themeRepository));
//        subCommandMap.put("preview", new ThemePreviewCommand(themeRepository));
//        subCommandMap.put("missing", new ThemeMissingCommand(themeRepository));
//        subCommandMap.put("show", new ThemeShowCommand(themeRepository));
//        subCommandMap.put("clear", new ThemeClearCommand(themeRepository));
//        subCommandMap.put("help", new ThemeHelpCommand());

        EmbedBuilder builder = EmbedProvider.getDefaultBuilder();
        builder.setDescription("All commands for themes! Use !theme <command> <args> to perform them.");
        builder.addField("help", """
                Shows all commands.
                                
                `help`""", true);
        builder.addField("list", """
                Shows every all created Themes.
                                
                `list`""", true);
        builder.addField("create", """
                Creates a new theme.
                                
                `create <theme> [description]`""", true);
        builder.addField("modify", """
                Modifies how the channel will look for given Theme.

                `modify <theme> <id> <name>`""", true);
        builder.addField("change", """
                Changes the channel to the given theme.
                                
                `change <theme>`""", true);
        builder.addField("save", """
                Saves the current channels to a new Theme.
                                
                `save <theme>`""", true);
        builder.addField("preview", """
                Shows how the discord will look for the given theme.
                                
                `preview <theme>`""", true);
        builder.addField("missing", """
                Shows which channels aren't given yet in the theme.
                                
                `missing <theme>`""", true);
        builder.addField("show", """
                Shows which channels have been updated and how they will look.
                                
                `show <theme>`""", true);
        builder.addField("clear", """
                Clears the given channel from a theme.
                                
                `clear <theme> <id>`""", true);
        builder.addField("clone", """
                Clones a Theme to a new Theme.
                                
                `clone <old> <new>`""", true);
        builder.addField("remove", """
                Removes the theme.
                                
                `remove <theme>`""", true);

        event.getChannel().sendMessageEmbeds(builder.build()).queue();
    }
}
