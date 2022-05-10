package com.trs.radio;

import com.trs.radio.games.ttt.entity.AITTTGame;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class AITTTTest {

    public static void main(String[] args) {
        AITTTGame game = new AITTTGame(-1L);
        game.updateGrid(0, 0, -10);
        game.nextMove();
        game.updateGrid(2, 2, -10);
        game.nextMove();
        try {
            ImageIO.write(game.drawImage(), "png", new File("test.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
