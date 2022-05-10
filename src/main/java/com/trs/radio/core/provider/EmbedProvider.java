package com.trs.radio.core.provider;

import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;


public class EmbedProvider {

    public static EmbedBuilder getDefaultBuilder() {
        return new EmbedBuilder()
                .setTitle("TRS Radio")
                .setFooter("Made by DizMizzer.")
                .setColor(Color.RED);
    }

    public static EmbedBuilder getTTTBuilder() {
        return getDefaultBuilder().setTitle("Tic Tac Toe").setDescription("Play a game of Tic Tac Toe against Diz's AI.");
    }

    public static EmbedBuilder getError() {
        return getDefaultBuilder().setDescription("An error has occured!");
    }
}
