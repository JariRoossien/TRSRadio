package com.trs.radio.games.ttt.command;

import com.trs.radio.core.commands.SubCommand;
import com.trs.radio.core.provider.EmbedProvider;
import com.trs.radio.games.ttt.entity.AITTTGame;
import com.trs.radio.games.ttt.repository.TTTRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class TicTacToeCommand implements SubCommand {

    private final TTTRepository repository;

    public TicTacToeCommand(TTTRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute(MessageReceivedEvent event, String... args) {
        EmbedBuilder embed = EmbedProvider.getTTTBuilder();
        AITTTGame game = new AITTTGame(event.getMember().getIdLong());

        embed.setImage("https://cdn.discordapp.com/attachments/854101450870751263/973315752964796436/test.png");
        event.getChannel().sendMessageEmbeds(embed.build())
                .setActionRows(game.getActionRows())
                .queue(success -> repository.addGame(game, success.getIdLong()));
    }
}
