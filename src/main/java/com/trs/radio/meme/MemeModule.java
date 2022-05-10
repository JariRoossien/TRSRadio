package com.trs.radio.meme;

import com.trs.radio.core.TrsModule;
import com.trs.radio.core.provider.BotProvider;
import com.trs.radio.meme.commands.EzCommand;
import com.trs.radio.meme.commands.JasonDeruloCommand;
import com.trs.radio.meme.provider.EzTextProvider;
import org.springframework.stereotype.Service;

@Service
public class MemeModule extends TrsModule {

    private final BotProvider botProvider;
    private final EzTextProvider textProvider;

    public MemeModule(BotProvider botProvider, EzTextProvider textProvider) {
        super();
        this.botProvider = botProvider;
        this.textProvider = textProvider;

        this.setup();
    }

    public void setup() {
        botProvider.getCommandListener().registerCommand("jd", new JasonDeruloCommand());
        botProvider.getCommandListener().registerCommand("ez", new EzCommand(textProvider));

    }
}
