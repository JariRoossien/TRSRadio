package com.trs.radio.games.rps.listener;

import com.trs.radio.core.provider.BotProvider;
import com.trs.radio.games.rps.entity.RPSGame;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RPSButtonListener extends ListenerAdapter {

    List<Button> buttons;

    public RPSButtonListener(BotProvider provider) {
        buttons = List.of(Button.secondary("rps_rock", "Rock✊"),
                Button.success("rps_paper", "Paper✋"),
                Button.primary("rps_scissor", "Scissor✌")
        );
        provider.getJda().addEventListener(this);
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        if (event.getMember() == null) return;
        if (event.getButton() == null) return;
        if (event.getButton().getId() == null) return;
        final long member_id = event.getMember().getIdLong();

        if (event.getButton().getId().equalsIgnoreCase("rpsmatch")) {
            final MessageEmbed embed = event.getMessage().getEmbeds().get(0);
            RPSGame game = RPSGame.getFromEmbed(embed);
            if (validateIsNotInGame(event, game, member_id)) return;

            game.setTwo_result(0);
            game.setOne_result(0);
            event.getChannel().sendMessageEmbeds(game.generateEmbed().build()).setActionRow(buttons).queue();
            event.deferEdit().queue();
            return;
        }
        if (!event.getButton().getId().startsWith("rps_")) return;

        final MessageEmbed embed = event.getMessage().getEmbeds().get(0);
        RPSGame game = RPSGame.getFromEmbed(embed);
        if (validateIsNotInGame(event, game, member_id)) return;

        int result = getResult(event.getButton().getId().replace("rps_", ""));
        if (game.getOne_id() == member_id && game.getOne_result() == 0) {
            game.setOne_result(result);
        } else if (game.getTwo_id() == member_id && game.getTwo_result() == 0) {
            game.setTwo_result(result);
        } else {
            event.reply("You already replied!").setEphemeral(true).queue();
            return;
        }

        if (game.isFinished()) {
            List<Button> componentList = buttons
                    .stream()
                    .map(button -> button.withDisabled(true)).collect(Collectors.toList());
            componentList.add(Button.primary("rpsmatch", "Rematch"));
            event.getMessage().editMessageEmbeds(game.generateEmbed().build()).setActionRow(componentList).queue();
        }

        event.getMessage().editMessageEmbeds(game.generateEmbed().build()).queue();
        event.deferEdit().queue();
    }

    private int getResult(String name) {
        return switch (name) {
            case "rock" -> 1;
            case "paper" -> 2;
            case "scissor" -> 3;
            default -> 0;
        };
    }

    private boolean validateIsNotInGame(ButtonClickEvent event, RPSGame game, long member_id) {
        if (game.getOne_id() != member_id && game.getTwo_id() != member_id) {
            event.reply("You aren't in this game :(").setEphemeral(true).queue();
            return true;
        }
        return false;
    }
}
