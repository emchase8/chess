package service.results;

import model.GameListData;
import java.util.List;

public record ErrorResult(String message) implements MostBasicResult {
    @Override
    public String authToken() {
        return "";
    }

    @Override
    public String username() {
        return "";
    }

    @Override
    public int gameID() {
        return 0;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public List<GameListData> games() {
        return null;
    }
}
