package com.trs.radio.games.rps.entity;

import lombok.Data;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.trs.radio.core.provider.EmbedProvider.getDefaultBuilder;

@Data
public class RPSGame {

    static Pattern pattern = Pattern.compile("<@!([0-9]+)> - <@!([0-9]+)>");
    long one_id;
    long two_id;
    String playerOneName;
    String playerTwoName;
    int one_result;
    int two_result;

    public RPSGame(Member playerOne, Member playerTwo) {
        one_result = 0;
        two_result = 0;
        this.one_id = playerOne.getIdLong();
        this.two_id = playerTwo.getIdLong();
        this.playerOneName = playerOne.getEffectiveName();
        this.playerTwoName = playerTwo.getEffectiveName();
    }

    public RPSGame() {
    }

    public static RPSGame getFromEmbed(MessageEmbed embed) {
        RPSGame game = new RPSGame();
        MessageEmbed.Field names = embed.getFields().get(0);
        String result = names.getValue();

        // Set the users
        Matcher matcher = pattern.matcher(result);
        if (matcher.find()) {
            game.one_id = Long.parseLong(matcher.group(1));
            game.two_id = Long.parseLong(matcher.group(2));
        }

        MessageEmbed.Field result_one = embed.getFields().get(2);
        MessageEmbed.Field result_two = embed.getFields().get(3);
        game.one_result = Integer.parseInt(result_one.getValue().charAt(3) + "");
        game.two_result = Integer.parseInt(result_two.getValue().charAt(3) + "");
        game.setPlayerOneName(embed.getFields().get(2).getName());
        game.setPlayerTwoName(embed.getFields().get(3).getName());

        return game;
    }

    public EmbedBuilder generateEmbed() {
        EmbedBuilder builder = getDefaultBuilder()
                .setTitle("TRS Bot")
                .addField("Matchup",
                        "<@!%d> - <@!%d>".formatted(one_id, two_id), true)
                .addField("Rock Paper Scissor", """
                        Click on the buttons below to give your answer! When both players have entered, \
                        the bot will reveal the answers and show the winner!""", false);
        if (one_result == 0 || two_result == 0) {
            builder.addField(playerOneName, """
                    ```%d
                    -
                    ```""".formatted(one_result), true)
                    .addField(playerTwoName, """
                            ```%d
                            -
                            ```""".formatted(two_result), true);
        } else {
            long winner_id = getWinnerId();
            builder.addField(playerOneName, """
                    ```%d
                    %s
                    ```""".formatted(one_result, getResult(one_result)), true)
                    .addField(playerTwoName, """
                            ```%d
                            %s
                            ```""".formatted(two_result, getResult(two_result)), true);
            if (winner_id == -1) {
                builder.addField("Results", """
                        The game ended in a tie!""", false);
            } else {
                builder.addField("Results", """
                        <@!%d> has won the game!""".formatted(winner_id), false);

            }
        }

        return builder;
    }

    private long getWinnerId() {
        if (one_result == two_result) return -1;
        if (one_result == 1) {
            return (two_result == 3) ? one_id : two_id;
        }
        if (one_result == 2) {
            return (two_result == 1) ? one_id : two_id;
        }
        if (one_result == 3) {
            return (two_result == 2) ? one_id : two_id;
        }
        return two_id;
    }

    public String getResult(int result) {
        return switch (result) {
            case 1 -> "✊";
            case 2 -> "✋";
            case 3 -> "✌";
            default -> "-";
        };
    }

    @Override
    public String toString() {
        return "RPSGame{" +
                "one_id=" + one_id +
                ", two_id=" + two_id +
                ", one_result=" + one_result +
                ", two_result=" + two_result +
                '}';
    }

    public boolean isFinished() {
        return one_result != 0 && two_result != 0;
    }
}
