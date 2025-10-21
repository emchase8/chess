package service.results;

import model.GameListData;
import java.util.List;

public record RegisterResult(String username, String authToken) implements MostBasicResult {
    @Override
    public String message() {
        return "";
    }

    @Override
    public String username() {
        return username;
    }

    @Override
    public String authToken() {
        return authToken;
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
