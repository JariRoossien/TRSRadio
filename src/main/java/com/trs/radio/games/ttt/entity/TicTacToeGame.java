package com.trs.radio.games.ttt.entity;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public abstract class TicTacToeGame {

    private final int[][] grid = new int[3][3];

    private final static int WIDTH = 303;
    private final static int HEIGHT = 303;
    private final static int STROKE_SIZE = 2;
    private static final int CIRCLE_OFFSET = 10;
    static final int EMPTY_VAL = -1;

    public TicTacToeGame() {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                grid[x][y] = EMPTY_VAL;
            }
        }
    }

    public boolean isFinished(int[][] grid) {
        for (int i = 0; i < 3; i++) {
            if (grid[i][0] == grid[i][1] && grid[i][0] == grid[i][2] && grid[i][0] != EMPTY_VAL) {
                return true;
            }
            if (grid[0][i] == grid[1][i] && grid[0][i] == grid[2][i] && grid[0][i] != EMPTY_VAL) {
                return true;
            }
        }

        if (grid[0][0] == grid[1][1] && grid[0][0] == grid[2][2] && grid[0][0] != EMPTY_VAL) return true;
        if (grid[0][2] == grid[1][1] && grid[0][2] == grid[2][0]  && grid[2][0] != EMPTY_VAL) return true;

        //Check if Game has any empty spots left.
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (grid[x][y] == EMPTY_VAL) return false;
            }
        }
        return true;
    }

    public void updateGrid(int x, int y, int value) {
        this.grid[x][y] = value;
    }

    public int getValue(int x, int y) {
        if (x > 3 || y > 3 || x < 0 || y < 0) return -1;
        return grid[x][y];
    }

    public int[][] copyGrid() {
        int[][] copy = new int[3][3];
        for (int x = 0; x < 3; x++) {
            System.arraycopy(grid[x], 0, copy[x], 0, 3);
        }
        return copy;
    }

    private String getStringFor(int value) {
        return switch (value) {
            case 10 -> "X";
            case -10 -> "0";
            default -> " ";
        };
    }

    public void printGrid() {
        System.out.printf("""
                %s|%s|%s
                -----
                %s|%s|%s
                -----
                %s|%s|%s
                
                """,
                getStringFor(grid[0][0]),
                getStringFor(grid[1][0]),
                getStringFor(grid[2][0]),
                getStringFor(grid[0][1]),
                getStringFor(grid[1][1]),
                getStringFor(grid[2][1]),
                getStringFor(grid[0][2]),
                getStringFor(grid[1][2]),
                getStringFor(grid[2][2])
                );
    }

    public static void drawCircle(Graphics2D g, int x, int y) {
        g.setColor(Color.BLUE);
        g.drawArc(x * WIDTH / 3 + CIRCLE_OFFSET,
                y * HEIGHT / 3 + CIRCLE_OFFSET,
                WIDTH / 3 - STROKE_SIZE - CIRCLE_OFFSET * 2,
                HEIGHT / 3 - STROKE_SIZE - CIRCLE_OFFSET * 2,
                0,
                360);
    }

    public static void drawCross(Graphics2D g, int x, int y) {
        g.setColor(Color.RED);
        g.drawLine((x * WIDTH / 3) + CIRCLE_OFFSET,
                (y * HEIGHT / 3) + CIRCLE_OFFSET,
                (x + 1) * WIDTH / 3 - CIRCLE_OFFSET,
                (y + 1) * HEIGHT / 3 - CIRCLE_OFFSET);

        g.drawLine((x * WIDTH / 3) + CIRCLE_OFFSET,
                ((y + 1) * HEIGHT / 3) - CIRCLE_OFFSET,
                (( x + 1) * WIDTH / 3) - CIRCLE_OFFSET,
                (y * HEIGHT / 3) + CIRCLE_OFFSET);

    }

    public BufferedImage drawImage() {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setColor(Color.WHITE);

        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Set Grid Lines
        g.setColor(Color.BLACK);
        g.fillRect((WIDTH / 3) - STROKE_SIZE, 0, STROKE_SIZE * 2, HEIGHT);
        g.fillRect((2 * WIDTH / 3) - STROKE_SIZE, 0, STROKE_SIZE * 2, HEIGHT);
        g.fillRect(0, (HEIGHT / 3) - STROKE_SIZE, WIDTH, STROKE_SIZE * 2);
        g.fillRect(0, (2 * HEIGHT / 3) - STROKE_SIZE, WIDTH, STROKE_SIZE * 2);

        // Set Circles.
        g.setStroke(new BasicStroke(7));

        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (grid[x][y] == 10) {
                    drawCircle(g, x, y);
                }
                if (grid[x][y] == -10) {
                    drawCross(g, x, y);
                }
            }
        }

        return image;
    }

    public InputStream getAsImage() {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ImageIO.write(drawImage(), "png", os);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(os.toByteArray());
    }

    public ActionRow[] getActionRows() {
        ActionRow[] actionRows = new ActionRow[3];
        Button[][] buttons = new Button[3][3];

        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                buttons[y][x] = Button.primary(y + "" + x, y + "" + x).withDisabled(getValue(x, y) != EMPTY_VAL || isFinished(grid));
            }
        }
        for (int i = 0; i < 3; i++) {
            actionRows[i] = ActionRow.of(buttons[i][0], buttons[i][1], buttons[i][2]);
        }

        return actionRows;
    }

}
