package com.trs.radio.games.ttt.listener;

import com.trs.radio.core.provider.EmbedProvider;
import com.trs.radio.games.ttt.entity.AITTTGame;
import com.trs.radio.games.ttt.entity.TicTacToeGame;
import com.trs.radio.games.ttt.repository.TTTRepository;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;


public class TicTacToeListener extends ListenerAdapter {

    private final TTTRepository repository;

    private final static long IMAGE_CHANNEL = 973307423345500163L;
    public TicTacToeListener(TTTRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        if (!repository.hasGame(event.getMessageIdLong())) return;
        long member_id = event.getMember().getIdLong();
        TicTacToeGame ticTacToeGame = repository.getGame(event.getMessageIdLong());
        if (ticTacToeGame instanceof AITTTGame) {
            AITTTGame aiGame = (AITTTGame) ticTacToeGame;
            if (aiGame.getMember_id() != member_id) {
                event.reply("Please start your own game using !ttt").setEphemeral(true).queue();
                return;
            }
            handleAIGame(aiGame, event);
        }
    }

    private void handleAIGame(AITTTGame game, ButtonClickEvent event) {

        // Get the location of button the user clicked.
        int y = Integer.parseInt(event.getButton().getId().substring(0, 1));
        int x = Integer.parseInt(event.getButton().getId().substring(1, 2));

        //Update given value.
        game.updateGrid(x, y, -10);

        if (game.isFinished(game.copyGrid())) {
            handleFinish(game, event);
            return;
        }

        // Let AI predict next move
        game.nextMove();

        if (game.isFinished(game.copyGrid())) {
            handleFinish(game, event);
        } else {
            // Upload image, and then update the Embed and the buttons.
            event.getJDA().getTextChannelById(IMAGE_CHANNEL).sendFile(game.getAsImage(), "image.png").queue(success -> {
                event.editMessageEmbeds(
                        EmbedProvider.getTTTBuilder()
                                .setImage(success.getAttachments().get(0).getUrl())
                                .build()
                ).setActionRows(game.getActionRows())
                        .queue();
            });
        }

    }

    private void handleFinish(AITTTGame game, ButtonClickEvent event) {

        // Upload image, and then update the Embed and the buttons.
        event.getJDA().getTextChannelById(IMAGE_CHANNEL).sendFile(game.getAsImage(), "image.png").queue(success -> {
            event.editMessageEmbeds(
                    EmbedProvider.getTTTBuilder()
                            .setImage(success.getAttachments().get(0).getUrl())
                            .build()
            ).setActionRows(game.getActionRows())
                    .queue();

        });
    }
}
