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
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                if (getValue(x, y) != EMPTY_VAL) continue;
                if (willWin(x, y)) {
                    bestX = x;
                    bestY = y;
                    points = 1000;
                }
                if (willLose(x, y) && points != 1000) {
                    bestX = x;
                    bestY = y;
                    points = 800;
                }
                System.out.printf("x:%d y:%d points:%d\n", x, y, getScoreFor(x, y));
                if (getScoreFor(x, y) > points) {
                    points = getScoreFor(x, y);
                    bestX = x;
                    bestY = y;
                }
            }
        }

        updateGrid(bestX, bestY, 10);
    }

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

    private boolean willWin(int x, int y) {
        int[][] copy = copyGrid();
        copy[x][y] = 10;
        return isFinished(copy);
    }

    private boolean willLose(int x, int y) {
        int[][] copy = copyGrid();
        copy[x][y] = -10;
        return isFinished(copy);
    }

}
