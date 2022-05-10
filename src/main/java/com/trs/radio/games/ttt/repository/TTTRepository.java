package com.trs.radio.games.ttt.repository;

import com.trs.radio.games.ttt.entity.TicTacToeGame;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Set;

@Repository
public class TTTRepository {

    public HashMap<Long, TicTacToeGame> gameHashMap = new HashMap<>();

    public boolean hasGame(long id) {
        return gameHashMap.containsKey(id);
    }

    public TicTacToeGame getGame(long id) {
        return gameHashMap.get(id);
    }

    public void addGame(TicTacToeGame game, long id) {
         gameHashMap.put(id, game);
    }

    public void removeGame(long id) {
        gameHashMap.remove(id);
    }
}
