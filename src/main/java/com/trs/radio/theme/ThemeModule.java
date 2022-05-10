package com.trs.radio.theme;

import com.trs.radio.core.TrsModule;
import com.trs.radio.core.provider.BotProvider;
import com.trs.radio.theme.commands.ThemeCommand;
import com.trs.radio.theme.repository.ThemeRepository;
import org.springframework.stereotype.Service;

@Service
public class ThemeModule extends TrsModule {

    private final BotProvider provider;
    private final ThemeRepository themeRepository;

    public ThemeModule(BotProvider provider, ThemeRepository themeRepository) {
        super();
        this.provider = provider;
        this.themeRepository = themeRepository;
        this.setup();
    }

    public void setup() {
        provider.getCommandListener().registerCommand("theme", new ThemeCommand(themeRepository));
    }
}
