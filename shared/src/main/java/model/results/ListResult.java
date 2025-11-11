package model.results;

import model.GameListData;

import java.util.List;

public record ListResult(List<GameListData> games) implements MostBasicResult {
    @Override
    public String authToken() {
        return "";
    }

    @Override
    public String message() {
        return "";
    }

    @Override
    public String username() {
        return "";
    }

    @Override
    public List<GameListData> games() {
        return games;
    }

    @Override
    public int gameID() {
        return 0;
    }
}
