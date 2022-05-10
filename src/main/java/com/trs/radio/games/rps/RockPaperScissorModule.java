package com.trs.radio.games.rps;

import com.trs.radio.core.TrsModule;
import com.trs.radio.core.provider.BotProvider;
import com.trs.radio.games.rps.command.RPSCommand;
import org.springframework.stereotype.Service;

@Service
public class RockPaperScissorModule extends TrsModule {

    private final BotProvider botProvider;

    public RockPaperScissorModule(BotProvider botProvider) {
        super();
        this.botProvider = botProvider;

        this.setup();
    }

    public void setup() {
        botProvider.getCommandListener().registerCommand("rps", new RPSCommand());
    }
}
