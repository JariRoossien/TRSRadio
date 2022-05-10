package com.trs.radio.games.ttt.entity;

import lombok.Getter;

public class AITTTGame extends TicTacToeGame {

    @Getter
    private final long member_id;

    public AITTTGame(long id) {
        super();
        this.member_id = id;
    }

    public void nextMove() {
        int points = -1;
        int bestX = -1;
        int bestY = -1;

        /*
        Tic Tac Toe AI:
        1. Iterate each location.
        2. If the location is already filled in, continue.
        3. If the location will result in a win, place the stone there.
        4. If the location being open will result in an immediate loss, place stone there.
        5. If it doesn't result in a win or a loss, calculate the score for the position.
        6. Set the stone on the position with the highest absolute score.
         */
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (getValue(x, y) != EMPTY_VAL) continue;
                if (willWin(x, y, 10)) {
                    bestX = x;
                    bestY = y;
                    points = 1000;
                }

                // Determine if opponent will win the game.
                // If another stone will win the game, ignore.
                if (willWin(x, y, -10) && points != 1000) {
                    bestX = x;
                    bestY = y;
                    points = 800;
                }

                // If the score is bigger than the current score, update location.
                if (getScoreFor(x, y) > points) {
                    points = getScoreFor(x, y);
                    bestX = x;
                    bestY = y;
                }
            }
        }

        // Set value on best location on the grid.
        updateGrid(bestX, bestY, 10);
    }

    /**
     * Function will calculate the score at the given position.
     *
     * The score is determined by the row summed and column summed, and if the place
     * is on either diagonal, sum up the score of the diagonal.
     *
     * @param x X locationn
     * @param y Y location
     * @return Score of given value.
     */
    private int getScoreFor(int x, int y) {
        int[][] copy = copyGrid();
        copy[x][y] = 10;
        int points = 0;
        points += copy[x][0] + copy[x][1] + copy[x][2];
        points += copy[0][y] + copy[1][y] + copy[2][y];

        if (x == y) points += copy[0][0] + copy[1][1] + copy[2][2];
        if (x + y == 2) points += copy[0][2] + copy[1][1] + copy[2][0];
        return Math.abs(points);
    }

    /**
     * Determines if placing a stone will result in a win.
     *
     * @param x
     * @param y
     * @param player
     * @return If game is finished.
     */
    private boolean willWin(int x, int y, int player) {
        int[][] copy = copyGrid();
        copy[x][y] = player;
        return isFinished(copy);
    }


}
