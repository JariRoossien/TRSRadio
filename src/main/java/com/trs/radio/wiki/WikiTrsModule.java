package com.trs.radio.wiki;

import com.trs.radio.core.TrsModule;
import com.trs.radio.core.provider.BotProvider;
import com.trs.radio.wiki.commands.RandomWikiCommand;
import com.trs.radio.wiki.commands.WikiSearchCommand;
import com.trs.radio.wiki.provider.WikiProvider;
import org.springframework.stereotype.Service;

@Service
public class WikiTrsModule extends TrsModule {

    private final BotProvider botProvider;
    private final WikiProvider wikiProvider;

    public WikiTrsModule(BotProvider botProvider, WikiProvider wikiProvider) {
        super();
        this.botProvider = botProvider;
        this.wikiProvider = wikiProvider;

        this.setup();
    }

    public void setup() {
        this.botProvider.getCommandListener().registerCommand("wiki", new WikiSearchCommand(wikiProvider));
        botProvider.getCommandListener().registerCommand("rwiki", new RandomWikiCommand(wikiProvider));
    }
}
