package dataaccess;

import model.GameData;
import model.GameListData;

import java.util.ArrayList;
import java.util.List;

public class MemoryGameDAO implements GameDAO {

    private static List<GameData> games = new ArrayList<>();

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }

    @Override
    public List<GameListData> listGames() throws DataAccessException {
        List<GameListData> gameList = new ArrayList<>();
        for (int i = 0; i < games.size(); i++) {
            GameData gameData = games.get(i);
            gameList.add(new GameListData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName()));
        }
        return gameList;
    }
}
