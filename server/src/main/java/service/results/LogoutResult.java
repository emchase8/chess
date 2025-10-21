package service.results;

import model.GameListData;
import java.util.List;

public record LogoutResult() implements MostBasicResult {
    @Override
    public String authToken() {
        return "";
    }

    @Override
    public String username() {
        return "";
    }

    @Override
    public String message() {
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
