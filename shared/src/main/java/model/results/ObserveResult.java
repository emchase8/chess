package model.results;

import model.GameListData;

import java.util.List;

public record ObserveResult(String jsonGame, String username, int gameID) implements MostBasicResult {
    public String jsonGame() {
        return jsonGame;
    }

    @Override
    public int gameID() {
        return gameID;
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public List<GameListData> games() {
        return List.of();
    }

    @Override
    public String message() {
        return "";
    }

    @Override
    public String authToken() {
        return "";
    }
}
