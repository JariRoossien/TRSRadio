package com.trs.radio.games.ttt;

import com.trs.radio.core.TrsModule;
import com.trs.radio.core.provider.BotProvider;
import com.trs.radio.games.ttt.command.TicTacToeCommand;
import com.trs.radio.games.ttt.listener.TicTacToeListener;
import com.trs.radio.games.ttt.repository.TTTRepository;
import org.springframework.stereotype.Service;

@Service
public class TTTModule extends TrsModule {

    final BotProvider provider;
    final TTTRepository repository;

    public TTTModule(BotProvider provider, TTTRepository repository) {
        super();
        this.provider = provider;
        this.repository = repository;
        this.setup();
    }

    @Override
    public void setup() {
        provider.getCommandListener().registerCommand("ttt", new TicTacToeCommand(repository));
        provider.getJda().addEventListener(new TicTacToeListener(repository));
    }
}
