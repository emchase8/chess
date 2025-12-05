package model.results;

import model.GameListData;

import java.util.List;

public record MoveResult(String message,
                         String jsonGame,
                         String username,
                         int gameID,
                         boolean inCheck,
                         boolean inCheckmate,
                         boolean inStalemate) implements MostBasicResult {
    @Override
    public String jsonGame() {
        return jsonGame;
    }

    @Override
    public boolean inCheck() {
        return inCheck;
    }

    @Override
    public boolean inCheckmate() {
        return inCheckmate;
    }

    @Override
    public boolean inStalemate() {
        return inStalemate;
    }

    @Override
    public String message() {return message;}
    @Override
    public String authToken() {return "";}
    @Override
    public String username() {return username;}
    @Override
    public int gameID() {return gameID;}
    @Override
    public List<GameListData> games() {return List.of();}
}
