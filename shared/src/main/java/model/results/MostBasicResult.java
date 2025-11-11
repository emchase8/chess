package model.results;

import model.GameListData;
import java.util.List;

public interface MostBasicResult {
    String message();
    String authToken();
    String username();
    int gameID();
    List<GameListData> games();
}
