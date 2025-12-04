package model.results;

import model.GameListData;

import java.util.List;

public record ResignResult(String username, int gameID) implements MostBasicResult {
    @Override
    public String message() {
        return "";
    }

    @Override
    public String username() {
        return "";
    }

    @Override
    public String authToken() {
        return "";
    }

    @Override
    public int gameID() {
        return 0;
    }

    @Override
    public List<GameListData> games() {
        return List.of();
    }
}
